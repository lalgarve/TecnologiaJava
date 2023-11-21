package br.edu.infnet.tecnologiajava.services.bancodados;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Usa o padrão decorator pois os valores são encapsulados para conseguir
 * saber se o valor está sendo referenciado por outra tabela ou não.
 *
 * @param <C> Classe da chave dos valores armazenados na tabela
 * @param <V> Classe dos valores armazenados na tabela
 * @author leila
 */
public class TabelaDependente<C, V extends ValorBD<C, V>> implements TabelaBD<C, V> {
    private final String nome;
    private final TabelaImpl<C, DecoradorValor> tabelaImpl;

    public TabelaDependente(String nome) {
        this.nome = nome;
        this.tabelaImpl = new TabelaImpl<>(nome);
    }

    @Override
    public void adiciona(V valor) throws BancoDadosException {
        tabelaImpl.adiciona(valor == null ? null : new DecoradorValor(valor));
    }

    @Override
    public void removePorId(C chave) throws BancoDadosException {
        Optional<DecoradorValor> decoradorOption = tabelaImpl.consultaPorId(chave);
        if (decoradorOption.map(DecoradorValor::estaSendoUsado).orElse(Boolean.FALSE)) {
            throw new BancoDadosException("O valor está sendo usado. Chave: " + chave);
        }
        tabelaImpl.removePorId(chave);
    }

    @Override
    public void altera(V valor) throws BancoDadosException {
        tabelaImpl.altera(valor == null ? null : new DecoradorValor(valor));
    }

    @Override
    public Optional<V> consultaPorId(C chave) throws BancoDadosException {
        return tabelaImpl.consultaPorId(chave).map(decorador -> decorador.valor);
    }

    @Override
    public String getNome() {
        return nome;
    }

    public void adicionaUso(C chave, TabelaBD<?, ?> tabela) throws BancoDadosException {
        Optional<DecoradorValor> decoradorOptional = tabelaImpl.consultaPorId(chave);
        if (decoradorOptional.isEmpty()) {
            throw new BancoDadosException("A chave não existe: " + chave);
        }
        decoradorOptional.get().adicionaUso(tabela);
        tabelaImpl.altera(decoradorOptional.get());
    }

    public void removeUso(C chave, TabelaBD<?, ?> tabela) throws BancoDadosException {
        Optional<DecoradorValor> decoradorOptional = tabelaImpl.consultaPorId(chave);
        DecoradorValor valor = decoradorOptional.orElseThrow(trowExcecaoNaoEncontrouChave(chave, tabela));
        if (!valor.removeUso(tabela)) {
            throw new BancoDadosException(String.format("Não há uso para a chave %s na tabela %s.", chave, tabela.getNome()));
        }
        tabelaImpl.altera(valor);
    }

    private static <C> Supplier<BancoDadosException> trowExcecaoNaoEncontrouChave(C chave, TabelaBD<?, ?> tabela) {
        return () -> new BancoDadosException(String.format("Não há uso para a chave %s na tabela %s.",
                chave, tabela.getNome()));
    }

    @Override
    public List<V> getValores() throws BancoDadosException {
        return tabelaImpl.getValores().stream()
                .map(decorator -> decorator.valor).toList();
    }

    @Override
    public List<V> getValores(Predicate<V> filtro) throws BancoDadosException {
        Objects.requireNonNull(filtro, "O filtro não pode ser nulo.");
        return tabelaImpl.getValores(decorador -> filtro.test(decorador.valor)).stream()
                .map(decorator -> decorator.valor).toList();
    }

    private class DecoradorValor implements ValorBD<C, DecoradorValor> {
        private final V valor;
        private Map<TabelaBD<?, ?>, Integer> contador = new HashMap<>();

        DecoradorValor(V valor) {
            this.valor = valor;
        }

        @Override
        public C getChave() {
            return valor.getChave();
        }

        @Override
        public DecoradorValor getInstanciaCopiaSegura() {
            V clone = valor.getInstanciaCopiaSegura();
            DecoradorValor decorador = new DecoradorValor(clone);
            decorador.contador.putAll(contador);
            return decorador;
        }

        void adicionaUso(TabelaBD<?, ?> tabela) {
            Integer numeroUsos = contador.getOrDefault(tabela, 0);
            contador.put(tabela, numeroUsos + 1);
        }

        boolean removeUso(TabelaBD<?, ?> tabela) {
            Integer numeroUsos = contador.get(tabela);
            if (numeroUsos == null) {
                return false;
            }
            if (numeroUsos == 1) {
                contador.remove(tabela);
            } else {
                contador.put(tabela, numeroUsos - 1);
            }
            return true;
        }

        boolean estaSendoUsado() {
            return !contador.isEmpty();
        }

        @Override
        public boolean podeSerGravadoNoBanco() {
            return valor.podeSerGravadoNoBanco();
        }
    }

}
