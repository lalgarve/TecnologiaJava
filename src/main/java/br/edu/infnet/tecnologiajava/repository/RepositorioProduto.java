package br.edu.infnet.tecnologiajava.repository;

import br.edu.infnet.tecnologiajava.model.domain.Produto;
import br.edu.infnet.tecnologiajava.services.bancodados.BancoDadosException;
import br.edu.infnet.tecnologiajava.services.bancodados.TabelaDependente;
import org.springframework.stereotype.Service;

public class RepositorioProduto extends TabelaDependente<Integer, Produto> {

    public RepositorioProduto() {
        super("produto");
    }


}
