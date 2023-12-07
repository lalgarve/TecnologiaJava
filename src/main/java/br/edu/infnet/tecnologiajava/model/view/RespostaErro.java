package br.edu.infnet.tecnologiajava.model.view;

import lombok.Data;
@Data
public class RespostaErro {
    private String title;
    private int status;
    private String detail;
    private String instance;
}
