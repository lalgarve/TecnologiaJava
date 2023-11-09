package br.edu.infnet.tecnologiajava.services.bancodados;

import java.util.List;
import java.util.function.Predicate;

/**
 *
 * @author leila
 * @param <C> Classe da chave dos valores armazenados na tabela
 * @param <V> Classe dos valores armazenados na tabela
 */
public class TabelaPrincipal<C, V extends ValorBD<C>> implements TabelaComDependentes<C, V> {

  @Override
  public Relacionamento1ParaN getRelacionamento1ParaN(String nome) throws BancoDadosException{
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

  @Override
  public Relacionamento1Para1 getRelacionamento1Para1(String nome) throws BancoDadosException{
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

  @Override
  public void adiciona(V valor) throws BancoDadosException{
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

  @Override
  public void removePorId(C chave) throws BancoDadosException{
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

  @Override
  public void altera(V valor) throws BancoDadosException{
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

  @Override
  public V consultaPorId(C chave) throws BancoDadosException{
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

  @Override
  public void adicionaRelacionamento1ParaN(String nome, Relacionamento1ParaN relacionamento) throws BancoDadosException{
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

  @Override
  public void adicionaRelacionamento1Para1(String nome, Relacionamento1Para1 relacionamento) throws BancoDadosException{
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

  @Override
  public String getNome() {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

  @Override
  public List<V> getValores() throws BancoDadosException {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

  @Override
  public List<V> getValores(Predicate<V> filtro) throws BancoDadosException {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

  @Override
  public List<V> getValores(boolean carregaDependentes) throws BancoDadosException {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

  @Override
  public List<V> getValores(Predicate<V> filtro, boolean carregaDependentes) throws BancoDadosException {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }
  
}
