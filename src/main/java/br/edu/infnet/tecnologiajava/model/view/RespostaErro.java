package br.edu.infnet.tecnologiajava.model.view;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
public class RespostaErro {
    private String mensagem;
    private List<String> detalhes;
}
