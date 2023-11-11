package br.edu.infnet.tecnologiajava.model.domain;

import java.util.ArrayList;
import java.util.List;

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
}
