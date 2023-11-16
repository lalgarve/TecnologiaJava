package br.edu.infnet.tecnologiajava.repository;

import br.edu.infnet.tecnologiajava.model.domain.Produto;
import br.edu.infnet.tecnologiajava.services.bancodados.TabelaDependente;

public class RepositorioProduto extends TabelaDependente<Integer, Produto>{

    private static RepositorioProduto instance = new RepositorioProduto();

    private RepositorioProduto() {
        super("produto");
    }

    public static RepositorioProduto getInstance(){
        return instance;
    }
    
}
