package br.edu.infnet.tecnologiajava.repository;

import br.edu.infnet.tecnologiajava.services.bancodados.TabelaImpl;

public class RepositorioPedido extends TabelaImpl{

    private static RepositorioPedido instance;

    private RepositorioPedido(){
        super("pedido");
    }

    public static RepositorioPedido getInstance(){
        return instance;
    }
    
    
}
