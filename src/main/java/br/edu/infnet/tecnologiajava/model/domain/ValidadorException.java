package br.edu.infnet.tecnologiajava.model.domain;

public class ValidadorException extends RuntimeException {

    private final Validador validador;

    public ValidadorException(String mensagem, Validador validador){
        super(mensagem+": " + validador.getMensagensConcatenadas()+".");
        this.validador = validador;
    }

    public Validador getValidador(){
        return validador;
    }
}
