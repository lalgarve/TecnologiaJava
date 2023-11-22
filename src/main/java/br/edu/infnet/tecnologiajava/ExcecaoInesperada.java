package br.edu.infnet.tecnologiajava;

public class ExcecaoInesperada extends RuntimeException{
    public ExcecaoInesperada() {
    }

    public ExcecaoInesperada(String message) {
        super(message);
    }

    public ExcecaoInesperada(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcecaoInesperada(Throwable cause) {
        super(cause);
    }

    public ExcecaoInesperada(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
