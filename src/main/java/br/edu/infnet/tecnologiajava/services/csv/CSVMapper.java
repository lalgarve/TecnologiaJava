package br.edu.infnet.tecnologiajava.services.csv;

/**
 * Interface para realizar o mapeamento dos campos de um arquivo CSV
 * para a criação de um objeto.
 * @author leila
 * @param <T> Classe do objeto sendo mapeado
 */
public interface CSVMapper<T> {
  void reset();
  void setValor(String campo, String valorComoString) throws CSVMapperException;
  T build() throws CSVMapperException;
}
