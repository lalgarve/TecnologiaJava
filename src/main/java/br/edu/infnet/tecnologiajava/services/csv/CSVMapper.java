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

  default float converteFloat(String valorComoString) throws CSVMapperException{
    try{
      return Float.parseFloat(valorComoString);
    }catch(NumberFormatException ex){
      throw new CSVMapperException(valorComoString + "não é um número ponto flutuante.", ex);
    }
  }

  
  default int converteInt(String valorComoString) throws CSVMapperException{
    try{
      return Integer.parseInt(valorComoString);
    }catch(NumberFormatException ex){
      throw new CSVMapperException(valorComoString + "não é um número inteiro.", ex);
    }
  }

  default boolean converteBoolean(String valorComoString) throws CSVMapperException {
    if("true".compareToIgnoreCase(valorComoString)==0){
      return true;
    }
    if("false".compareToIgnoreCase(valorComoString)==0){
      return false;
    }
    throw new CSVMapperException(valorComoString + "não é um valor booleano.");
  }
}
