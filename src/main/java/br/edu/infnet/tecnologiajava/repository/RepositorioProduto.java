package br.edu.infnet.tecnologiajava.repository;

import br.edu.infnet.tecnologiajava.model.domain.Produto;
import br.edu.infnet.tecnologiajava.services.bancodados.TabelaDependente;

public class RepositorioProduto extends TabelaDependente<Integer, Produto> {

    public RepositorioProduto() {
        super("produto");
    }


}
