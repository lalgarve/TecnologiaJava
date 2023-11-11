package br.edu.infnet.tecnologiajava.model.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Validador {
    private List<String> mensagens = new ArrayList<>();
    
    public void valida(String mensagem, boolean valido){
        if(!valido){
            mensagens.add(mensagem);
        }
    }

    public List<String> getMensagens(){
        return mensagens;
    }

    public boolean temErro(){
        return !mensagens.isEmpty();
    }

    public String getMensagensConcatenadas(){
        return mensagens.stream()
                        .map((mensagem) -> primeiraLetraMinuscula(mensagem))
                        .collect(Collectors.joining(", "));
    }

    private String primeiraLetraMinuscula(String mensagemOriginal){
        String primeiraLetra = mensagemOriginal.substring(0, 1).toLowerCase();
        return primeiraLetra + mensagemOriginal.substring(1, mensagemOriginal.length());
    }
}
