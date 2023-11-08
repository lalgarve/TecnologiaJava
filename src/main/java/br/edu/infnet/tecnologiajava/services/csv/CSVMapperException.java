package br.edu.infnet.tecnologiajava.services.csv;

/**
 * Identifica um erro durante o mapeamento dos dados.
 * @author leila
 */
public class CSVMapperException extends Exception {

  public CSVMapperException() {
  }

  public CSVMapperException(String message) {
    super(message);
  }

  public CSVMapperException(String message, Throwable cause) {
    super(message, cause);
  }

  public CSVMapperException(Throwable cause) {
    super(cause);
  }

  public CSVMapperException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
  
}
