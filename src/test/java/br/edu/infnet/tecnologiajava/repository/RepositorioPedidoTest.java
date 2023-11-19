package br.edu.infnet.tecnologiajava.repository;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.edu.infnet.tecnologiajava.model.domain.Bebida;
import br.edu.infnet.tecnologiajava.model.domain.Comida;
import br.edu.infnet.tecnologiajava.model.domain.Pedido;
import br.edu.infnet.tecnologiajava.model.domain.Produto;
import br.edu.infnet.tecnologiajava.model.domain.Sobremesa;
import br.edu.infnet.tecnologiajava.model.domain.Solicitante;
import br.edu.infnet.tecnologiajava.model.domain.ValidadorException;
import br.edu.infnet.tecnologiajava.services.bancodados.BancoDadosException;

import static org.junit.jupiter.api.Assertions.*;

public class RepositorioPedidoTest {

    @BeforeEach
    public void iniciatiozaRepositorio() throws IOException, BancoDadosException {
        ControladorRepositorio.inicializa();
        try (InputStream is = RepositorioPedidoTest.class.getResourceAsStream("/sobremesa.csv")) {
            //noinspection ConstantConditions
            ControladorRepositorio.carregaSobremesa(new InputStreamReader(is));
        }
        try (InputStream is = RepositorioPedidoTest.class.getResourceAsStream("/bebida.csv")) {
            //noinspection ConstantConditions
            ControladorRepositorio.carregaBebida(new InputStreamReader(is));
        }
        try (InputStream is = RepositorioPedidoTest.class.getResourceAsStream("/comida.csv")) {
            //noinspection ConstantConditions
            ControladorRepositorio.carregaComida(new InputStreamReader(is));
        }
        try (InputStream is = RepositorioPedidoTest.class.getResourceAsStream("/solicitante.csv")) {
            //noinspection ConstantConditions
            ControladorRepositorio.carregaSolicitante(new InputStreamReader(is));
        }
    }

    @Test
    public void testAdiciona() throws ValidadorException, BancoDadosException {
        RepositorioProduto repositorioProduto = RepositorioProduto.getInstance();
        List<Produto> produtos = new ArrayList<>();
        produtos.add(repositorioProduto.consultaPorId(1).orElseThrow());

        Pedido pedido = new Pedido("Pedido sobremesa", false, Solicitante.getVazio());
        pedido.setProdutos(produtos);

        RepositorioPedido repositorioPedido = RepositorioPedido.getInstance();
        repositorioPedido.adiciona(pedido);
        Pedido pedidoBanco = repositorioPedido.consultaPorId(1).orElseThrow();

        assertEquals(pedido, pedidoBanco);
        assertNotSame(pedido, pedidoBanco, "Pedido não é imutável, clone deveria ter sido retornado");

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
        Pedido pedidoBanco = repositorioPedido.consultaPorId(1).orElseThrow();

        assertEquals(pedido, pedidoBanco);
        assertNotSame(pedido, pedidoBanco, "Pedido não é imutável, clone deveria ter sido retornado");

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
    public void testAdicionaVariosProdutoSoIdExsite() throws ValidadorException, BancoDadosException {
        List<Produto> produtosDiferenteDoBanco = new ArrayList<>();
        adicionaComida(produtosDiferenteDoBanco, 1, 5, "comida");

        Pedido pedido = new Pedido("Pedido sobremesa", false, Solicitante.getVazio());
        pedido.setProdutos(produtosDiferenteDoBanco);
        Pedido pedidoCopia = new Pedido(pedido);

        assertEquals(pedido, pedidoCopia);

        RepositorioPedido repositorioPedido = RepositorioPedido.getInstance();
        repositorioPedido.adiciona(pedido);
        Pedido pedidoBanco = repositorioPedido.consultaPorId(1).orElseThrow();

        assertEquals(produtosDiferenteDoBanco.size(), pedidoBanco.getNumeroProdutos());
        List<Produto> listProdutosIguais = pedidoBanco.getProdutos().filter(produtosDiferenteDoBanco::contains).toList();
        assertEquals(0, listProdutosIguais.size());   
        
        assertEquals(pedido, pedidoCopia, "Pedido gravado não deve ser modificado");        
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

        Pedido pedidoBanco = repositorioPedido.consultaPorId(1).orElseThrow();

        assertEquals(pedido, pedidoBanco);
        assertNotSame(pedido, pedidoBanco, "Pedido não é imutável, clone deveria ter sido retornado");

        assertEquals(pedido, pedidoBanco);
        assertNotSame(pedido, pedidoBanco, "Pedido não é imutável, clone deveria ter sido retornado");

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
    public void testConsultaPorIdNaoExiste() throws BancoDadosException {
        RepositorioPedido repositorioPedido = RepositorioPedido.getInstance();
        Optional<Pedido> pedidoOptional = repositorioPedido.consultaPorId(2);
        assertTrue(pedidoOptional.isEmpty());
    }

    @Test
    public void testAlteraVariosProdutosSoIdExiste() throws BancoDadosException, ValidadorException {
        List<Produto> produtos = getAlgunsProdutos(3);

        Pedido pedido = new Pedido("Pedido sobremesa", false, Solicitante.getVazio());
        pedido.setProdutos(produtos);

        RepositorioPedido repositorioPedido = RepositorioPedido.getInstance();
        repositorioPedido.adiciona(pedido);

        List<Produto> produtosDiferenteDoBanco = new ArrayList<>();
        adicionaComida(produtosDiferenteDoBanco, 1, 5, "comida");
        pedido.setProdutos(produtosDiferenteDoBanco);
        Pedido pedidoCopia = new Pedido(pedido);
        repositorioPedido.altera(pedido);

        Pedido pedidoBanco = repositorioPedido.consultaPorId(1).orElseThrow();

        assertEquals(produtosDiferenteDoBanco.size(), pedidoBanco.getNumeroProdutos());
        List<Produto> listProdutosIguais = pedidoBanco.getProdutos().filter(produtosDiferenteDoBanco::contains).toList();
        assertEquals(0, listProdutosIguais.size());   
        
        assertEquals(pedido, pedidoCopia, "Pedido gravado não deve ser modificado");      
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
    public void testAdicionaListProdutoVazia() throws BancoDadosException, ValidadorException {
        Pedido pedido = new Pedido("Pedido sobremesa", false, Solicitante.getVazio());

        RepositorioPedido repositorioPedido = RepositorioPedido.getInstance();
        BancoDadosException excecao = assertThrows(BancoDadosException.class, () -> repositorioPedido.adiciona(pedido));
        assertEquals("A lista de produtos não pode ser vazia.", excecao.getMessage());
    }

    @Test
    public void testAlteraListProdutosVazia() throws BancoDadosException, ValidadorException {
        List<Produto> produtos = getAlgunsProdutos(3);

        Pedido pedido = new Pedido("Pedido sobremesa", false, Solicitante.getVazio());
        pedido.setProdutos(produtos);

        RepositorioPedido repositorioPedido = RepositorioPedido.getInstance();
        repositorioPedido.adiciona(pedido);

        pedido.setProdutos(new ArrayList<>());
        BancoDadosException excecao = assertThrows(BancoDadosException.class, () ->repositorioPedido.altera(pedido));
        assertEquals("A lista de produtos não pode ser vazia.", excecao.getMessage());
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

    private void adicionaComida(List<Produto> produtos, int codigoInicial, int quantidade, String nome) throws ValidadorException {
        int codigo = codigoInicial;
        for (int i = 0; i < quantidade; i++) {
            produtos.add(new Comida(codigo++, nome + " " + i, "marca " + i, i + 10, (i % 2) == 0, 10.0f));
        }
    }

}
