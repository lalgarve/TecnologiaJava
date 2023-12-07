package br.edu.infnet.tecnologiajava.model.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Validador implements Serializable {
    private final List<String> mensagens = new ArrayList<>();

    public void valida(String mensagem, boolean valido) {
        if (!valido) {
            mensagens.add(mensagem);
        }
    }

    public List<String> getMensagens() {
        return mensagens;
    }

    public boolean temErro() {
        return !mensagens.isEmpty();
    }

    public String getMensagensConcatenadas() {
        return mensagens.stream()
                .map(this::primeiraLetraMinuscula)
                .collect(Collectors.joining(", "));
    }

    private String primeiraLetraMinuscula(String mensagemOriginal) {
        String primeiraLetra = mensagemOriginal.substring(0, 1).toLowerCase();
        return primeiraLetra + mensagemOriginal.substring(1);
    }

    @Override
    public String toString() {
        if (temErro()) {
            return String.format("Validador com %d erro(s): %s.", mensagens.size(), getMensagensConcatenadas());
        } else {
            return "Validador sem erros.";
        }
    }
}
