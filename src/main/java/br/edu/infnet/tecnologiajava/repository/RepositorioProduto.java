package br.edu.infnet.tecnologiajava.repository;

import br.edu.infnet.tecnologiajava.model.domain.Produto;
import br.edu.infnet.tecnologiajava.services.bancodados.BancoDadosException;
import br.edu.infnet.tecnologiajava.services.bancodados.TabelaDependente;

public class RepositorioProduto extends TabelaDependente<Integer, Produto>{

    private static RepositorioProduto instance = new RepositorioProduto();

    RepositorioProduto() {
        super("produto");
    }

    public static RepositorioProduto getInstance() throws BancoDadosException{
        if(instance == null){
            throw new BancoDadosException("Repositorio produto não foi inicializado.");
        }
        return instance;
    }

    static void criaRepositorio(){
        instance = new RepositorioProduto();
    }
    
}
