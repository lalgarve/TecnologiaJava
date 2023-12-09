package br.edu.infnet.tecnologiajava.model.domain;

import br.edu.infnet.tecnologiajava.ValidadorException;

/**
 * Classe usada quando se conhece apenas o código do produto ou
 * se que armazenar uma referência ao produto armazenado.
 */
public final class ProdutoCodigo extends Produto {
    public ProdutoCodigo(int codigo) throws ValidadorException {
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
        ProdutoCodigo produto = (ProdutoCodigo) obj;
        return produto.getCodigo() == getCodigo();
    }
}
