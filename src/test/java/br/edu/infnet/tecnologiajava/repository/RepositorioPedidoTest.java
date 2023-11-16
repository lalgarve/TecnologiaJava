package br.edu.infnet.tecnologiajava.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.edu.infnet.tecnologiajava.model.domain.Pedido;
import br.edu.infnet.tecnologiajava.model.domain.Produto;
import br.edu.infnet.tecnologiajava.model.domain.Solicitante;
import br.edu.infnet.tecnologiajava.model.domain.ValidadorException;
import br.edu.infnet.tecnologiajava.services.bancodados.BancoDadosException;

public class RepositorioPedidoTest {

    @BeforeEach
    public void iniciatiozaRepositorio() throws IOException, BancoDadosException {
        ControladorRepositorio.inicializa();
        try (InputStream is = RepositorioPedidoTest.class.getResourceAsStream("/sobremesa.csv")) {
            ControladorRepositorio.carregaSobremesa(new InputStreamReader(is));
        }
    }

    @Test
    public void testAdiciona() throws ValidadorException, BancoDadosException {
        RepositorioProduto repositorioProduto = RepositorioProduto.getInstance();
        List<Produto> produtos = new ArrayList<>();
        produtos.add(repositorioProduto.consultaPorId(1).get());

        Pedido pedido = new Pedido("Pedido sobremesa", false, Solicitante.getVazio());
        pedido.setProdutos(produtos);

        RepositorioPedido repositorioPedido = RepositorioPedido.getInstance();
        repositorioPedido.adiciona(pedido);
        Pedido pedidoBanco = repositorioPedido.consultaPorId(1).get();

        assertEquals(pedido, pedidoBanco);
        assertFalse(pedido == pedidoBanco, "Pedido não é imutável, clone deveria ter sido retornado");

        assertThrows(BancoDadosException.class, () -> repositorioProduto.removePorId(1),
                "O produto está sendo usado, não poderia ter sido removido.");
    }

    @Test
    public void testAdicionaVariosProduto() throws ValidadorException, BancoDadosException {
        RepositorioProduto repositorioProduto = RepositorioProduto.getInstance();
        List<Produto> produtos = getAlgunsProdutos(3);

        Pedido pedido = new Pedido("Pedido sobremesa", false, Solicitante.getVazio());
        pedido.setProdutos(produtos);

        RepositorioPedido repositorioPedido = RepositorioPedido.getInstance();
        repositorioPedido.adiciona(pedido);
        Pedido pedidoBanco = repositorioPedido.consultaPorId(1).get();

        assertEquals(pedido, pedidoBanco);
        assertFalse(pedido == pedidoBanco, "Pedido não é imutável, clone deveria ter sido retornado");

        for(Produto produto:produtos){
           assertThrows(BancoDadosException.class, () -> repositorioProduto.removePorId(produto.getChave()),
                "O produto está sendo usado, não poderia ter sido removido.");         
        }
    }

    @Test
    void testAltera() {

    }

    @Test
    void testConsultaPorId() {

    }

    @Test
    void testEquals() {

    }

    @Test
    void testGetInstance() {

    }

    @Test
    void testGetNome() {

    }

    @Test
    void testGetValores() {

    }

    @Test
    void testGetValores2() {

    }

    @Test
    void testRemovePorId() {

    }

    private List<Produto> getAlgunsProdutos(int modulo) throws BancoDadosException {
        RepositorioProduto repositorioProduto = RepositorioProduto.getInstance();
        return repositorioProduto.getValores((produto) -> (produto.getCodigo() % modulo) == 0);
    }
}
