package br.edu.infnet.tecnologiajava.services.bancodados;

/**
 * Interface implementada por classes que serão salvas no banco de dados.
 * @author leila
 */
public interface ValorBD<C>{
  C getChave();

  /**
   * Retorna uma instancia do objeto que não permita
   * que os campos da instancia original sejam alterados.
   * 
   * @return 
   */
  ValorBD<C> getInstanciaCopiaSegura();
}
