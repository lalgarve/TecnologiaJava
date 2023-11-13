package br.edu.infnet.tecnologiajava.model.domain;

public record TipoProduto(String descricao, Class<? extends Produto> classe) implements Comparable {

    @Override
    public int compareTo(Object tipoProduto) {
        TipoProduto outro = (TipoProduto) tipoProduto;
        return outro.descricao().compareTo(outro.descricao());
    }
    
}
