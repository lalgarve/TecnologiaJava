package br.edu.infnet.tecnologiajava.services.bancodados;

/**
 *
 * @author leila
 * @param <T>
 */
public interface TabelaBD<T extends ValorBD> {
  
  void adiciona(T valor);
  void removePorId(Object chave);
  void altera(T valor);
  void consultaPorId(Object chave);
  
}
