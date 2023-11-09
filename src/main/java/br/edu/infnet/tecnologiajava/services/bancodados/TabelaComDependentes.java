package br.edu.infnet.tecnologiajava.services.bancodados;

import java.util.List;
import java.util.function.Predicate;

/**
 *
 * @author leila
 * @param <C> Classe da chave dos valores armazenados na tabela
 * @param <T> Classe dos valores armazenados
 */
public interface TabelaComDependentes<C, T extends ValorBD<C>> extends TabelaBD<C, T> {
  Relacionamento1ParaN getRelacionamento1ParaN(String nome) throws BancoDadosException;
  void adicionaRelacionamento1ParaN(String nome, Relacionamento1ParaN relacionamento) throws BancoDadosException;
  Relacionamento1Para1 getRelacionamento1Para1(String nome) throws BancoDadosException;
  void adicionaRelacionamento1Para1(String nome, Relacionamento1Para1 relacionamento) throws BancoDadosException;
  List<T> getValores(boolean carregaDependentes) throws BancoDadosException;
  List<T> getValores(Predicate<T> filtro, boolean carregaDependentes) throws BancoDadosException;
}
