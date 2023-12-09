package br.edu.infnet.tecnologiajava.model.view;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RespostaInclusao<T> {
    private T chave;
}
