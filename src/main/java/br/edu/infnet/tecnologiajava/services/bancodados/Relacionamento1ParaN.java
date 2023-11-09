package br.edu.infnet.tecnologiajava.services.bancodados;

import java.util.List;


/**
 *
 * @author leila
 * @param <C> Classe da chave da entidade dependente
 * @param <D> Classe da entidade dependente
 */
public interface Relacionamento1ParaN<C, D extends ValorBD> {
  
  void adiciona(C chaveDependente) throws BancoDadosException;
  
  void remove(C chaveDependente) throws BancoDadosException;
  
  List<D> getDependentes() throws BancoDadosException;
 
}
