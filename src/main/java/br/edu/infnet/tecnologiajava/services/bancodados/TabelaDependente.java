package br.edu.infnet.tecnologiajava.services.bancodados;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 *
 * @author leila
 * @param <C> Classe da chave dos valores armazenados na tabela
 * @param <V> Classe dos valores armazenados na tabela
 */
public class TabelaDependente<C, V extends ValorBD<C>> implements TabelaBD<C,V> {

  @Override
  public void adiciona(V valor) throws BancoDadosException{
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  @Override
  public void removePorId(C chave) throws BancoDadosException{
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  @Override
  public void altera(V valor) throws BancoDadosException{
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  @Override
  public Optional<V> consultaPorId(C chave) throws BancoDadosException{
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  @Override
  public String getNome() {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }
  
  public void adicionaRelacao(TabelaBD tabela) throws BancoDadosException{
    
  }
  
  public void adicionaUso(C chave, TabelaBD tabela) throws BancoDadosException{
    
  }
  
  public void removeUso(C chave, TabelaBD tabela){
    
  }

  @Override
  public List<V> getValores() throws BancoDadosException {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

  @Override
  public List<V> getValores(Predicate<V> filtro) throws BancoDadosException {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }
  
}
