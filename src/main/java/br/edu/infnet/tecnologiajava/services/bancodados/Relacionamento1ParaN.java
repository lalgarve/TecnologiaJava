package br.edu.infnet.tecnologiajava.services.bancodados;

import java.util.List;


/**
 *
 * @author leila
 * @param <P> Classe da entidade principal 
 * @param <D> Classe da entidade dependente
 */
public interface Relacionamento1ParaN<P extends Cloneable,D extends Cloneable> {
  
  void adiciona(Object chaveDependente) throws BancoDadosException;
  
  void remove(Object chaveDependente) throws BancoDadosException;
  
  List<D> getDependentes() throws BancoDadosException;
 
}
