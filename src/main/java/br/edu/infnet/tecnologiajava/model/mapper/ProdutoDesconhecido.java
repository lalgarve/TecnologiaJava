package br.edu.infnet.tecnologiajava.model.mapper;

import br.edu.infnet.tecnologiajava.model.domain.Produto;
import br.edu.infnet.tecnologiajava.model.domain.ValidadorException;

class ProdutoDesconhecido extends Produto {
    ProdutoDesconhecido(int codigo) throws ValidadorException {
        super(codigo);
    }

    @Override
    public String getDetalhe() {
        return "Desconhecido";
    }

    @Override
    public int hashCode() {
        return getCodigo();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProdutoDesconhecido produto = (ProdutoDesconhecido) obj;
        return produto.getCodigo() == getCodigo();
    }
}
