package br.edu.infnet.tecnologiajava.services.bancodados;

/**
 *
 * @author leila
 * @param <C> Classe da chave da entidade dependente
 * @param <D> Classe da entidade dependente
 */
public interface Relacionamento1Para1<C, D extends ValorBD<C>> {
  void setDependente(D dependente) throws BancoDadosException;
  D getDependente() throws BancoDadosException;
  void removeDependente() throws BancoDadosException; 
}
