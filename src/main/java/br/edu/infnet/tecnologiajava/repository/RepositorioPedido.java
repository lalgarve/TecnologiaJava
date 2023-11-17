package br.edu.infnet.tecnologiajava.repository;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import br.edu.infnet.tecnologiajava.model.domain.Pedido;
import br.edu.infnet.tecnologiajava.model.domain.Produto;
import br.edu.infnet.tecnologiajava.services.bancodados.BancoDadosException;
import br.edu.infnet.tecnologiajava.services.bancodados.TabelaBD;
import br.edu.infnet.tecnologiajava.services.bancodados.TabelaImpl;

public class RepositorioPedido implements TabelaBD<Integer, Pedido>{

    private static RepositorioPedido instance;
    private final TabelaImpl<Integer, Pedido> tabelaPedido;

    private RepositorioPedido(){
        tabelaPedido = new TabelaImpl<>("pedido");
    }

    public static RepositorioPedido getInstance() throws BancoDadosException{
        if(instance == null){
            throw new BancoDadosException("Repositorio pedido não foi criado");
        }
        return instance;
    }

    static void criaRepositorio(){
        instance = new RepositorioPedido();
    }

    public void adiciona(Pedido pedido) throws BancoDadosException {     
        RepositorioProduto repositorioProduto = RepositorioProduto.getInstance();   
        tabelaPedido.adiciona(pedido);
        Iterator<Produto> iterator = pedido.getProdutos().iterator();
        while(iterator.hasNext()){
            Produto produto = iterator.next();
            repositorioProduto.adicionaUso(produto.getChave(), this);
        }
    }

    public void removePorId(Integer chave) throws BancoDadosException {
        tabelaPedido.removePorId(chave);
    }

    public void altera(Pedido pedido) throws BancoDadosException {
        RepositorioProduto repositorioProduto = RepositorioProduto.getInstance(); 
        Pedido pedidoAnterior = tabelaPedido.consultaPorId(pedido.getChave()).orElse(null);
        tabelaPedido.altera(pedido);
        
        Iterator<Produto> iterator = pedidoAnterior.getProdutos().iterator();
        while(iterator.hasNext()){
            Produto produto = iterator.next();
            repositorioProduto.removeUso(produto.getChave(), this);
        }          
        
        iterator = pedido.getProdutos().iterator();
        while(iterator.hasNext()){
            Produto produto = iterator.next();
            repositorioProduto.adicionaUso(produto.getChave(), this);
        }        
    }

    public Optional<Pedido> consultaPorId(Integer chave) throws BancoDadosException {
        return tabelaPedido.consultaPorId(chave);
    }

    public List<Pedido> getValores() throws BancoDadosException {
        return tabelaPedido.getValores();
    }

    public List<Pedido> getValores(Predicate<Pedido> filtro) throws BancoDadosException {
        return tabelaPedido.getValores(filtro);
    }

    public String getNome() {
        return tabelaPedido.getNome();
    }

    public boolean equals(Object obj) {
        return tabelaPedido.equals(obj);
    }
    
    
}
