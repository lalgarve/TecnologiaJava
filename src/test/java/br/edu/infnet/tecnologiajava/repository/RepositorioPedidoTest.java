package br.edu.infnet.tecnologiajava.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.edu.infnet.tecnologiajava.model.domain.Bebida;
import br.edu.infnet.tecnologiajava.model.domain.Pedido;
import br.edu.infnet.tecnologiajava.model.domain.Produto;
import br.edu.infnet.tecnologiajava.model.domain.Sobremesa;
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
        try (InputStream is = RepositorioPedidoTest.class.getResourceAsStream("/bebida.csv")) {
            ControladorRepositorio.carregaBebida(new InputStreamReader(is));
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

        for (Produto produto : produtos) {
            assertThrows(BancoDadosException.class, () -> repositorioProduto.removePorId(produto.getChave()),
                    "O produto está sendo usado, não poderia ter sido removido.");
        }
    }

    @Test
    public void testAdicionaProdutoNaoExiste() throws ValidadorException, BancoDadosException {
        List<Produto> produtos = new ArrayList<>();
        adicionaBebida(produtos, 3, "bebida");

        Pedido pedido = new Pedido("Pedido sobremesa", false, Solicitante.getVazio());
        pedido.setProdutos(produtos);

        RepositorioPedido repositorioPedido = RepositorioPedido.getInstance();

        BancoDadosException excecao = assertThrows(BancoDadosException.class, () -> repositorioPedido.adiciona(pedido));
        assertEquals("Há produtos que não estão no banco de dados.", excecao.getMessage());
    }

    @Test
    public void testAcionaPedidoJaExiste() throws ValidadorException, BancoDadosException {
        List<Produto> produtos = getAlgunsProdutos(3);

        Pedido pedido = new Pedido("Pedido sobremesa", false, Solicitante.getVazio());
        pedido.setProdutos(produtos);

        RepositorioPedido repositorioPedido = RepositorioPedido.getInstance();
        repositorioPedido.adiciona(pedido);
        BancoDadosException excecao = assertThrows(BancoDadosException.class,
                () -> repositorioPedido.adiciona(pedido));
        assertEquals("A chave 1 já existe na tabela pedido.", excecao.getMessage());        
    }

    @Test
    public void testAltera() throws BancoDadosException, ValidadorException {
        RepositorioProduto repositorioProduto = RepositorioProduto.getInstance();
        List<Produto> produtos = getAlgunsProdutos(3);

        Pedido pedido = new Pedido("Pedido sobremesa", false, Solicitante.getVazio());
        pedido.setProdutos(produtos);

        RepositorioPedido repositorioPedido = RepositorioPedido.getInstance();
        repositorioPedido.adiciona(pedido);

        List<Produto> novosProdutos = getAlgunsProdutos(2);
        pedido.setProdutos(novosProdutos);
        repositorioPedido.altera(pedido);

        Pedido pedidoBanco = repositorioPedido.consultaPorId(1).get();

        assertEquals(pedido, pedidoBanco);
        assertFalse(pedido == pedidoBanco, "Pedido não é imutável, clone deveria ter sido retornado");

        assertEquals(pedido, pedidoBanco);
        assertFalse(pedido == pedidoBanco, "Pedido não é imutável, clone deveria ter sido retornado");

        List<Produto> listaPodeDeletar = new ArrayList<>(produtos);
        listaPodeDeletar.removeAll(novosProdutos);

        for (Produto produto : listaPodeDeletar) {
            try {
                repositorioProduto.removePorId(produto.getChave());
            } catch (BancoDadosException ex) {
                fail(ex);
            }
        }

        for (Produto produto : novosProdutos) {
            assertThrows(BancoDadosException.class, () -> repositorioProduto.removePorId(produto.getChave()),
                    "O produto está sendo usado, não poderia ter sido removido.");
        }

    }

    @Test
    public void testConsultaPorIdNaoExiste() throws BancoDadosException, ValidadorException {
        RepositorioPedido repositorioPedido = RepositorioPedido.getInstance();
        Optional<Pedido> pedidoOptional = repositorioPedido.consultaPorId(2);
        assertTrue(pedidoOptional.isEmpty());
    }

    @Test
    public void testAlteraProdutosNaoExiste() throws BancoDadosException, ValidadorException {
        List<Produto> produtos = getAlgunsProdutos(3);

        Pedido pedido = new Pedido("Pedido sobremesa", false, Solicitante.getVazio());
        pedido.setProdutos(produtos);

        RepositorioPedido repositorioPedido = RepositorioPedido.getInstance();
        repositorioPedido.adiciona(pedido);

        List<Produto> novosProdutos = new ArrayList<>();
        adicionaSobremesa(novosProdutos, 5, "sobremesa");
        pedido.setProdutos(novosProdutos);
        BancoDadosException excecao = assertThrows(BancoDadosException.class, 
            () -> repositorioPedido.altera(pedido));
        assertEquals("Há produtos que não estão no banco de dados.", excecao.getMessage());    
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

    private void adicionaSobremesa(List<Produto> produtos, int quantidade, String nome) throws ValidadorException {
        for (int i = 0; i < quantidade; i++) {
            produtos.add(new Sobremesa(nome + " " + i, (i % 2) == 0, "informação " + i, i + 10, 10.f));
        }
    }

    private void adicionaBebida(List<Produto> produtos, int quantidade, String nome) throws ValidadorException {
        for (int i = 0; i < quantidade; i++) {
            produtos.add(new Bebida(nome + " " + i, "marca " + i, 3.0f, (i % 2) == 0, 10.0f));
        }
    }

}
