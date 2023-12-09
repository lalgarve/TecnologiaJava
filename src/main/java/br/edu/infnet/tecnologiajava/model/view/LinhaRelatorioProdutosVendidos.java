package br.edu.infnet.tecnologiajava.model.view;

import br.edu.infnet.tecnologiajava.model.domain.Produto;
import lombok.Getter;

@Getter
public class LinhaRelatorioProdutosVendidos {
    private final int codigo;
    private final String tipo;
    private final int quantidade;
    private final float valor;
    private final float valorTotal;
    private final String nome;

    public LinhaRelatorioProdutosVendidos(Produto produto, int quantidade){
        this.codigo = produto.getCodigo();
        this.quantidade = quantidade;
        this.tipo = produto.getClass().getSimpleName();
        this.valor = produto.getValor();
        this.valorTotal = produto.getValor() * quantidade;
        this.nome = produto.getNome();
    }

}
