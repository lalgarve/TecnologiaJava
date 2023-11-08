package br.edu.infnet.tecnologiajava.services.bancodados;

/**
 *
 * @author leila
 */
public interface Relacionamento1Para1<D extends Cloneable> {
  void setDependente(D dependente);
  D getDependente() throws BancoDadosException;
}
