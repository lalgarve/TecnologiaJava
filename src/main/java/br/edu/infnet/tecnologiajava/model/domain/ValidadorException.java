package br.edu.infnet.tecnologiajava.model.domain;

public class ValidadorException extends RuntimeException {

    private final Validador validador;

    public ValidadorException(String mensagem, Validador validador){
        super(mensagem+": "+String.join(",", validador.getMensagens()));
        this.validador = validador;
    }

    public Validador getValidador(){
        return validador;
    }
}
