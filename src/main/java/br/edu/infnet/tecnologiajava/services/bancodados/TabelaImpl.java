/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package br.edu.infnet.tecnologiajava.services.bancodados;

import java.util.HashMap;
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

  private final Map<C,V> dadosTabela = new HashMap<>();
  private final String nome;
  
  public TabelaImpl(String nome){
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
    dadosTabela.put(valor.getChave(), valor);
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
    dadosTabela.put(valor.getChave(), valor);
  }

  @Override
  public Optional<V> consultaPorId(C chave) throws BancoDadosException {
    Objects.requireNonNull(chave, "A chave não pode ser nula.");
    V valor = dadosTabela.get(chave);
    valor = valor==null?null:(V)valor.getClone();  
    return Optional.ofNullable(valor);
  }

  @Override
  public List<V> getValores() throws BancoDadosException {
    return dadosTabela.values().stream()
            .map((valor) -> (V)valor.getClone())
            .toList();
  }

  @Override
  public List<V> getValores(Predicate<V> filtro) throws BancoDadosException {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

  @Override
  public String getNome() {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }
  
}
