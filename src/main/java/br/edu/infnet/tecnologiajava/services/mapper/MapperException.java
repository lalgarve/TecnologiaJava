package br.edu.infnet.tecnologiajava.services.mapper;

/**
 * Identifica um erro durante o mapeamento dos dados.
 *
 * @author leila
 */
public class MapperException extends RuntimeException {

    public MapperException() {
    }

    public MapperException(String message) {
        super(message);
    }

    public MapperException(String message, Throwable cause) {
        super(message, cause);
    }

    public MapperException(Throwable cause) {
        super(cause);
    }

    public MapperException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
