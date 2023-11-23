package br.edu.infnet.tecnologiajava.services.bancodados;

import java.util.*;
import java.util.function.Predicate;

/**
 * @param <C> Classe da chave do valor
 * @param <V> Classe do valor armazenado
 * @author leila
 */
public class TabelaImpl<C, V extends ValorBD<C, V>> implements TabelaBD<C, V> {

    public static final String A_CHAVE_NAO_PODE_SER_NULA = "A chave não pode ser nula.";
    private final Map<C, V> dadosTabela = new LinkedHashMap<>();
    private final String nome;

    public TabelaImpl(String nome) {
        Objects.requireNonNull(nome, "O nome não pode ser nulo.");
        if (nome.isBlank()) {
            throw new IllegalArgumentException("O nome não pode estar vazio ou em branco.");
        }
        this.nome = nome;
    }

    @Override
    public void adiciona(V valor) throws BancoDadosException {
        Objects.requireNonNull(valor, "O valor não pode ser nulo.");
        Objects.requireNonNull(valor.getChave(), A_CHAVE_NAO_PODE_SER_NULA);
        if (!valor.podeSerGravadoNoBanco()) {
            throw new BancoDadosException("O valor não pode ser adicionado.");
        }
        V valorExistente = dadosTabela.get(valor.getChave());
        if (valorExistente != null) {
            throw new BancoDadosException("A chave " + valor.getChave() + " já existe na tabela " + nome + ".");
        }
        dadosTabela.put(valor.getChave(), valor.criaInstanciaCopiaSegura());
    }

    @Override
    public void removePorId(C chave) throws BancoDadosException {
        Objects.requireNonNull(chave, A_CHAVE_NAO_PODE_SER_NULA);
        V valor = dadosTabela.remove(chave);
        if (valor == null) {
            throw new BancoDadosException("A chave " + chave + " não existe na tabela " + nome + ".");
        }
    }

    @Override
    public void altera(V valor) throws BancoDadosException {
        Objects.requireNonNull(valor, "O valor não pode ser nulo.");
        Objects.requireNonNull(valor.getChave(), A_CHAVE_NAO_PODE_SER_NULA);
        if (!valor.podeSerGravadoNoBanco()) {
            throw new BancoDadosException("Novo valor não pode ser posto no banco de dados.");
        }
        V valorOriginal = dadosTabela.get(valor.getChave());
        if (valorOriginal == null) {
            throw new BancoDadosException("Não exite chave " + valor.getChave() + " para ser alterada.");
        }
        dadosTabela.put(valor.getChave(), valor.criaInstanciaCopiaSegura());
    }

    @Override
    public Optional<V> consultaPorId(C chave) throws BancoDadosException {
        Objects.requireNonNull(chave, A_CHAVE_NAO_PODE_SER_NULA);
        V valor = dadosTabela.get(chave);
        valor = valor == null ? null : valor.criaInstanciaCopiaSegura();
        return Optional.ofNullable(valor);
    }

    @Override
    public List<V> getValores() throws BancoDadosException {
        return dadosTabela.values().stream()
                .map(ValorBD::criaInstanciaCopiaSegura)
                .toList();
    }

    @Override
    public List<V> getValores(Predicate<V> filtro) throws BancoDadosException {
        Objects.requireNonNull(filtro, "O filtro não pode ser nulo.");
        return dadosTabela.values().stream()
                .filter(filtro)
                .map(ValorBD::criaInstanciaCopiaSegura)
                .toList();
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.nome);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TabelaImpl<?, ?> other = (TabelaImpl<?, ?>) obj;
        return Objects.equals(this.nome, other.nome);
    }

}
