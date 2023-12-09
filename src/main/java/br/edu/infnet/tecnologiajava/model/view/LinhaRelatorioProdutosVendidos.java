package br.edu.infnet.tecnologiajava.model.view;

import br.edu.infnet.tecnologiajava.model.domain.Produto;
import lombok.Data;

@Data
public class LinhaRelatorioProdutosVendidos {
    private int codigo;
    private String tipo;
    private int quantidade;
    private float valor;
    private float valorTotal;
    private String nome;

    public LinhaRelatorioProdutosVendidos(Produto produto, int quantidade) {
        this.codigo = produto.getCodigo();
        this.quantidade = quantidade;
        this.tipo = produto.getClass().getSimpleName();
        this.valor = produto.getValor();
        this.valorTotal = produto.getValor() * quantidade;
        this.nome = produto.getNome();
    }

    public LinhaRelatorioProdutosVendidos() {

    }

}
