package br.edu.infnet.tecnologiajava.services.bancodados;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 *
 * @author leila
 * @param <C> Classe da chave do valor
 * @param <V> Classe do valor armazenado
 */
public class TabelaImpl<C,V extends ValorBD<C>> implements TabelaBD<C, V> {

  private final Map<C,V> dadosTabela = new LinkedHashMap<>();
  private final String nome;
  
  public TabelaImpl(String nome){
    Objects.requireNonNull(nome,"O nome não pode ser nulo.");
    if(nome.isBlank()){
      throw new IllegalArgumentException("O nome não pode estar vazio ou em branco.");
    }
    this.nome = nome;
  }
  
  @Override
  public void adiciona(V valor) throws BancoDadosException {
    Objects.requireNonNull(valor, "O valor não pode ser nulo.");
    Objects.requireNonNull(valor.getChave(), "A chave não pode ser nula.");
    V valorExistente = dadosTabela.get(valor.getChave());
    if(valorExistente!=null){
      throw new BancoDadosException("A chave "+valor.getChave()+" já existe na tabela.");
    }
    dadosTabela.put(valor.getChave(), (V) valor.getInstanciaCopiaSegura());
  }

  @Override
  public void removePorId(C chave) throws BancoDadosException {
    Objects.requireNonNull(chave, "A chave não pode ser nula.");
    V valor = dadosTabela.remove(chave);
    if (valor==null){
      throw new BancoDadosException("A chave "+chave+" não existe na tabela.");
    }
  } 

  @Override
  public void altera(V valor) throws BancoDadosException {
    Objects.requireNonNull(valor, "O valor não pode ser nulo.");
    Objects.requireNonNull(valor.getChave(), "A chave não pode ser nula.");
    V valorOriginal = dadosTabela.get(valor.getChave());
    if(valorOriginal==null){
      throw new BancoDadosException("Não exite chave "+valor.getChave()+" para ser alterada.");
    }
    dadosTabela.put(valor.getChave(), (V) valor.getInstanciaCopiaSegura());
  }

  @Override
  public Optional<V> consultaPorId(C chave) throws BancoDadosException {
    Objects.requireNonNull(chave, "A chave não pode ser nula.");
    V valor = dadosTabela.get(chave);
    valor = valor==null?null:(V)valor.getInstanciaCopiaSegura();  
    return Optional.ofNullable(valor);
  }

  @Override
  public List<V> getValores() throws BancoDadosException {
    return dadosTabela.values().stream()
            .map((valor) -> (V)valor.getInstanciaCopiaSegura())
            .toList();
  }

  @Override
  public List<V> getValores(Predicate<V> filtro) throws BancoDadosException {
    Objects.requireNonNull(filtro, "O filtro não pode ser nulo.");
    return dadosTabela.values().stream()
            .filter(filtro)
            .map((valor) -> (V)valor.getInstanciaCopiaSegura())
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
