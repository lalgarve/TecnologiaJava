package br.edu.infnet.tecnologiajava.model.view;

import lombok.Data;

import java.util.List;

@Data
public class RelatorioProdutosVendidos {
    private int mes;
    private int ano;
    private List<LinhaRelatorioProdutosVendidos> dadosVenda;
}
