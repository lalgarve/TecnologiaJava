package br.edu.infnet.tecnologiajava.repository;

import br.edu.infnet.tecnologiajava.ExcecaoInesperada;
import br.edu.infnet.tecnologiajava.ValidadorException;
import br.edu.infnet.tecnologiajava.model.domain.Pedido;
import br.edu.infnet.tecnologiajava.model.domain.Produto;
import br.edu.infnet.tecnologiajava.model.domain.ProdutoCodigo;
import br.edu.infnet.tecnologiajava.model.domain.Solicitante;
import br.edu.infnet.tecnologiajava.services.bancodados.BancoDadosException;
import br.edu.infnet.tecnologiajava.services.bancodados.TabelaBD;
import br.edu.infnet.tecnologiajava.services.bancodados.TabelaImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Representa o repositório de um pedido.
 * Se um produto ou solicitante estiver em um pedido, ele será marcado como não podendo ser apagado.
 * O produto ou solicitante será liberado para exclusão se não houver mais pedidos referenciando eles.
 * No momento de adicionar ou alterar um pedido, os produtos e solicitantes terão apenas as
 * respectivas chaves verificadas, ou seja, o código e o CPF.
 */
public class RepositorioPedido implements TabelaBD<Integer, Pedido> {

    private final TabelaImpl<Integer, Pedido> tabelaPedido;

    private final RepositorioSolicitante repositorioSolicitante;

    private final RepositorioProduto repositorioProduto;

    public RepositorioPedido(RepositorioProduto repositorioProduto, RepositorioSolicitante repositorioSolicitante) {
        this.repositorioSolicitante = repositorioSolicitante;
        this.repositorioProduto = repositorioProduto;
        tabelaPedido = new TabelaImpl<>("pedido");
    }

    public void adiciona(Pedido pedido) throws BancoDadosException {
        if (pedido.getNumeroProdutos() == 0) {
            throw new BancoDadosException("A lista de produtos não pode ser vazia.");
        }

        List<Produto> produtos = getProdutosParaBanco(pedido);
        Solicitante solicitante = getSolicitanteParaBanco(pedido);

        Pedido pedidoParaBanco = new Pedido(pedido);
        pedidoParaBanco.setProdutos(produtos);
        pedidoParaBanco.setSolicitante(solicitante);
        tabelaPedido.adiciona(pedidoParaBanco);

        Iterator<Produto> iterator = pedido.getProdutos().iterator();
        while (iterator.hasNext()) {
            Produto produto = iterator.next();
            repositorioProduto.adicionaUso(produto.getChave(), this);
        }

        repositorioSolicitante.adicionaUso(solicitante.getChave(), this);
    }

    private Solicitante getSolicitanteParaBanco(Pedido pedido) throws BancoDadosException {
        Optional<Solicitante> solicitanteBanco = repositorioSolicitante.consultaPorId(pedido.getSolicitante().getChave());
        solicitanteBanco = solicitanteBanco.map(solicitante -> {
            try {
                return new Solicitante(solicitante.getCpf());
            } catch (ValidadorException e) {
                throw new ExcecaoInesperada("O CPF deveria ser válido", e);
            }
        });
        return solicitanteBanco.orElseThrow(() -> new BancoDadosException("O solicitante com CPF " + pedido.getSolicitante().getCpf() + " não existe no banco."));
    }

    public void altera(Pedido pedido) throws BancoDadosException {
        if (pedido.getNumeroProdutos() == 0) {
            throw new BancoDadosException("A lista de produtos não pode ser vazia.");
        }

        List<Produto> produtos = getProdutosParaBanco(pedido);

        Pedido pedidoAnterior = tabelaPedido.consultaPorId(pedido.getChave()).orElseThrow(() -> new BancoDadosException("Pedido com chave " + pedido.getChave() + " não foi encontrado."));

        Solicitante solicitante = getSolicitanteParaBanco(pedido);

        Pedido pedidoParaBanco = new Pedido(pedido);
        pedidoParaBanco.setProdutos(produtos);
        pedidoParaBanco.setSolicitante(solicitante);
        tabelaPedido.altera(pedidoParaBanco);

        Iterator<Produto> iterator = pedidoAnterior.getProdutos().iterator();
        while (iterator.hasNext()) {
            Produto produto = iterator.next();
            repositorioProduto.removeUso(produto.getChave(), this);
        }

        iterator = pedidoParaBanco.getProdutos().iterator();
        while (iterator.hasNext()) {
            Produto produto = iterator.next();
            repositorioProduto.adicionaUso(produto.getChave(), this);
        }

        repositorioSolicitante.removeUso(pedidoAnterior.getSolicitante().getChave(), this);
        repositorioSolicitante.adicionaUso(pedidoParaBanco.getSolicitante().getChave(), this);
    }

    private List<Produto> getProdutosParaBanco(Pedido pedido) throws BancoDadosException {
        List<Produto> produtosBanco = new ArrayList<>(pedido.getNumeroProdutos());
        Iterator<Integer> chaves = pedido.getProdutos().map(Produto::getChave).iterator();
        while (chaves.hasNext()) {
            Integer chave = chaves.next();
            Optional<Produto> produtoOpcional = repositorioProduto.consultaPorId(chave);
            if (produtoOpcional.isEmpty()) {
                throw new BancoDadosException("Há produtos que não estão no banco de dados.");
            } else {
                try {
                    produtosBanco.add(new ProdutoCodigo(produtoOpcional.get().getCodigo()));
                } catch (ValidadorException e) {
                    throw new ExcecaoInesperada("O código deveria ser válido.", e);
                }
            }
        }

        return produtosBanco;
    }

    public void removePorId(Integer chave) throws BancoDadosException {
        Pedido pedidoBanco = tabelaPedido.consultaPorId(chave).orElseThrow(() -> new BancoDadosException("Pedido com id " + chave + " não existe no banco."));
        tabelaPedido.removePorId(chave);
        repositorioSolicitante.removeUso(pedidoBanco.getSolicitante().getChave(), this);
        Iterator<Produto> produtoIterator = pedidoBanco.getProdutos().iterator();

        while (produtoIterator.hasNext()) {
            Produto produto = produtoIterator.next();
            repositorioProduto.removeUso(produto.getChave(), this);
        }
    }

    public Optional<Pedido> consultaPorId(Integer chave) throws BancoDadosException {
        Optional<Pedido> optionalPedido = tabelaPedido.consultaPorId(chave);
        optionalPedido.ifPresent(this::substituiSolicitante);
        optionalPedido.ifPresent(this::substituiProdutos);
        return optionalPedido;
    }

    public List<Pedido> getValores() throws BancoDadosException {
        List<Pedido> pedidos = tabelaPedido.getValores();
        pedidos.forEach(this::substituiSolicitante);
        pedidos.forEach(this::substituiProdutos);
        return pedidos;
    }

    public List<Pedido> getValores(Predicate<Pedido> filtro) throws BancoDadosException {
        List<Pedido> pedidos = tabelaPedido.getValores(filtro);
        pedidos.forEach(this::substituiSolicitante);
        pedidos.forEach(this::substituiProdutos);
        return pedidos;
    }

    private void substituiSolicitante(Pedido pedido) {
        try {
            pedido.setSolicitante(repositorioSolicitante.consultaPorId(pedido.getSolicitante().getChave()).orElseThrow());
        } catch (BancoDadosException e) {
            throw new ExcecaoInesperada("Não deveria ter erro na busca.", e);
        }
    }

    private void substituiProdutos(Pedido pedido) {
        List<Produto> listaProdutos = pedido.getProdutos().map(produto -> {
            try {
                return repositorioProduto.consultaPorId(produto.getChave()).orElseThrow();
            } catch (BancoDadosException e) {
                throw new ExcecaoInesperada("Não deveria dar erro procurando produto.", e);
            }
        }).toList();
        pedido.setProdutos(listaProdutos);
    }

    public String getNome() {
        return tabelaPedido.getNome();
    }

}
