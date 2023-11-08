package br.edu.infnet.tecnologiajava.services.bancodados;

/**
 *
 * @author leila
 */
public class TabelaDependente<T extends ValorBD> implements TabelaBD<T> {

  @Override
  public void adiciona(T valor) {
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  @Override
  public void removePorId(Object chave) {
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  @Override
  public void altera(T valor) {
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  @Override
  public void consultaPorId(Object chave) {
    throw new UnsupportedOperationException("Not supported yet."); 
  }
  
}
