package br.edu.infnet.tecnologiajava.services.csv;

/**
 * Excecao não checada usada dentro de streams
 * @author leila
 */
public class UncheckedCSVMapperException extends RuntimeException{

  public UncheckedCSVMapperException() {
  }

  public UncheckedCSVMapperException(String message) {
    super(message);
  }

  public UncheckedCSVMapperException(String message, Throwable cause) {
    super(message, cause);
  }

  public UncheckedCSVMapperException(Throwable cause) {
    super(cause);
  }

  public UncheckedCSVMapperException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
  
}
