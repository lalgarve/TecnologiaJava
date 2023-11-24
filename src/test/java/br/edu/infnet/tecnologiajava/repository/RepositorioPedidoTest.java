package br.edu.infnet.tecnologiajava.repository;

import br.edu.infnet.tecnologiajava.model.domain.*;
import br.edu.infnet.tecnologiajava.services.bancodados.BancoDadosException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RepositorioPedidoTest {

    @BeforeEach
    void iniciatializaRepositorio() throws IOException, BancoDadosException {
        InicializadorRepositorio.inicializa();
        try (InputStream is = RepositorioPedidoTest.class.getResourceAsStream("/sobremesa.csv")) {
            //noinspection ConstantConditions
            InicializadorRepositorio.carregaSobremesa(new InputStreamReader(is));
        }
        try (InputStream is = RepositorioPedidoTest.class.getResourceAsStream("/bebida.csv")) {
            //noinspection ConstantConditions
            InicializadorRepositorio.carregaBebida(new InputStreamReader(is));
        }
        try (InputStream is = RepositorioPedidoTest.class.getResourceAsStream("/comida.csv")) {
            //noinspection ConstantConditions
            InicializadorRepositorio.carregaComida(new InputStreamReader(is));
        }
        try (InputStream is = RepositorioPedidoTest.class.getResourceAsStream("/solicitante.csv")) {
            //noinspection ConstantConditions
            InicializadorRepositorio.carregaSolicitante(new InputStreamReader(is));
        }
    }

    @Test
    void testAdiciona() throws ValidadorException, BancoDadosException {
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
    void testAdicionaVariosProduto() throws ValidadorException, BancoDadosException {
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
    void testAdicionaProdutoNaoExiste() throws ValidadorException, BancoDadosException {
        List<Produto> produtos = new ArrayList<>();
        adicionaBebida(produtos, 3, "bebida");

        Pedido pedido = new Pedido("Pedido sobremesa", false, Solicitante.getVazio());
        pedido.setProdutos(produtos);

        RepositorioPedido repositorioPedido = RepositorioPedido.getInstance();

        BancoDadosException excecao = assertThrows(BancoDadosException.class, () -> repositorioPedido.adiciona(pedido));
        assertEquals("Há produtos que não estão no banco de dados.", excecao.getMessage());
    }

    @Test
    void testAcionaPedidoJaExiste() throws ValidadorException, BancoDadosException {
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
    void testAdicionaVariosProdutoSoIdExsite() throws ValidadorException, BancoDadosException {
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
    void testAltera() throws BancoDadosException, ValidadorException {
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
    void testConsultaPorIdNaoExiste() throws BancoDadosException {
        RepositorioPedido repositorioPedido = RepositorioPedido.getInstance();
        Optional<Pedido> pedidoOptional = repositorioPedido.consultaPorId(2);
        assertTrue(pedidoOptional.isEmpty());
    }

    @Test
    void testAlteraVariosProdutosSoIdExiste() throws BancoDadosException, ValidadorException {
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
    void testAlteraProdutosNaoExiste() throws BancoDadosException, ValidadorException {
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
    void testAdicionaListProdutoVazia() throws BancoDadosException, ValidadorException {
        Pedido pedido = new Pedido("Pedido sobremesa", false, Solicitante.getVazio());

        RepositorioPedido repositorioPedido = RepositorioPedido.getInstance();
        BancoDadosException excecao = assertThrows(BancoDadosException.class, () -> repositorioPedido.adiciona(pedido));
        assertEquals("A lista de produtos não pode ser vazia.", excecao.getMessage());
    }

    @Test
    void testAlteraListProdutosVazia() throws BancoDadosException, ValidadorException {
        List<Produto> produtos = getAlgunsProdutos(3);

        Pedido pedido = new Pedido("Pedido sobremesa", false, Solicitante.getVazio());
        pedido.setProdutos(produtos);

        RepositorioPedido repositorioPedido = RepositorioPedido.getInstance();
        repositorioPedido.adiciona(pedido);

        pedido.setProdutos(new ArrayList<>());
        BancoDadosException excecao = assertThrows(BancoDadosException.class, () -> repositorioPedido.altera(pedido));
        assertEquals("A lista de produtos não pode ser vazia.", excecao.getMessage());
    }

    @Test
    void testAdicionaComSolicitante() throws ValidadorException, BancoDadosException {
        RepositorioProduto repositorioProduto = RepositorioProduto.getInstance();
        List<Produto> produtos = new ArrayList<>();
        produtos.add(repositorioProduto.consultaPorId(1).orElseThrow());

        Solicitante solicitante = new Solicitante("775.007.216-09");
        Pedido pedido = new Pedido("Pedido sobremesa", false, solicitante);
        pedido.setProdutos(produtos);

        RepositorioPedido repositorioPedido = RepositorioPedido.getInstance();
        repositorioPedido.adiciona(pedido);
        Pedido pedidoBanco = repositorioPedido.consultaPorId(1).orElseThrow();

        RepositorioSolicitante repositorioSolicitante = RepositorioSolicitante.getInstance();
        Solicitante solicitanteBanco = repositorioSolicitante.consultaPorId("775.007.216-09").orElseThrow();
        pedido.setSolicitante(solicitanteBanco);

        assertEquals(pedido, pedidoBanco);
        assertNotSame(pedido, pedidoBanco, "Pedido não é imutável, clone deveria ter sido retornado");

        assertThrows(BancoDadosException.class, () -> repositorioSolicitante.removePorId("775.007.216-09"),
                "O solicitante está sendo usado, não poderia ter sido removido.");
        assertEquals("Paulo Rodrigues", pedidoBanco.getSolicitante().getNome());
    }

    @Test
    void testAdicionaSolicitanteNaoExiste() throws ValidadorException, BancoDadosException {
        RepositorioProduto repositorioProduto = RepositorioProduto.getInstance();
        List<Produto> produtos = new ArrayList<>();
        produtos.add(repositorioProduto.consultaPorId(1).orElseThrow());

        Solicitante solicitante = new Solicitante("307.137.992-77");
        Pedido pedido = new Pedido("Pedido sobremesa", false, solicitante);
        pedido.setProdutos(produtos);

        RepositorioPedido repositorioPedido = RepositorioPedido.getInstance();
        BancoDadosException excecao = assertThrows(BancoDadosException.class, () -> repositorioPedido.adiciona(pedido));
        assertEquals("O solicitante com CPF 307.137.992-77 não existe no banco.", excecao.getMessage());

    }

    @Test
    void testAlteraComSolicitante() throws ValidadorException, BancoDadosException {
        RepositorioProduto repositorioProduto = RepositorioProduto.getInstance();
        List<Produto> produtos = new ArrayList<>();
        produtos.add(repositorioProduto.consultaPorId(1).orElseThrow());

        Solicitante solicitante = new Solicitante("775.007.216-09");
        Pedido pedido = new Pedido("Pedido sobremesa", false, solicitante);
        pedido.setProdutos(produtos);

        RepositorioPedido repositorioPedido = RepositorioPedido.getInstance();
        repositorioPedido.adiciona(pedido);

        Solicitante novoSolicitante = new Solicitante("943.861.323-41");
        pedido.setSolicitante(novoSolicitante);
        repositorioPedido.altera(pedido);

        RepositorioSolicitante repositorioSolicitante = RepositorioSolicitante.getInstance();
        Solicitante solicitanteBanco = repositorioSolicitante.consultaPorId("943.861.323-41").orElseThrow();
        pedido.setSolicitante(solicitanteBanco);

        Pedido pedidoBanco = repositorioPedido.consultaPorId(1).orElseThrow();

        assertEquals(pedido, pedidoBanco);
        assertNotSame(pedido, pedidoBanco, "Pedido não é imutável, clone deveria ter sido retornado");

        try{
            repositorioSolicitante.removePorId("775.007.216-09");
        }catch (BancoDadosException ex){
            fail("Solicitante 775.007.216-09 deveria ter sido liberado para exclusão.");
        }
        assertThrows(BancoDadosException.class, () -> repositorioSolicitante.removePorId("943.861.323-41"),
                "O solicitante está sendo usado, não poderia ter sido removido.");
        assertEquals("Andréa Ferreira", pedidoBanco.getSolicitante().getNome());
    }

    @Test
    void testAlteraComSolicitanteNaoExiste() throws ValidadorException, BancoDadosException {
        RepositorioProduto repositorioProduto = RepositorioProduto.getInstance();
        List<Produto> produtos = new ArrayList<>();
        produtos.add(repositorioProduto.consultaPorId(1).orElseThrow());

        Solicitante solicitante = new Solicitante("775.007.216-09");
        Pedido pedido = new Pedido("Pedido sobremesa", false, solicitante);
        pedido.setProdutos(produtos);

        RepositorioPedido repositorioPedido = RepositorioPedido.getInstance();
        repositorioPedido.adiciona(pedido);

        Solicitante novoSolicitante = new Solicitante("307.137.992-77");
        pedido.setSolicitante(novoSolicitante);
        BancoDadosException excecao = assertThrows(BancoDadosException.class, () -> repositorioPedido.altera(pedido));
        assertEquals("O solicitante com CPF 307.137.992-77 não existe no banco.", excecao.getMessage());
    }

    @Test
    void testRemoveComSolicitante() throws ValidadorException, BancoDadosException {
        RepositorioProduto repositorioProduto = RepositorioProduto.getInstance();
        List<Produto> produtos = new ArrayList<>();
        produtos.add(repositorioProduto.consultaPorId(1).orElseThrow());

        Solicitante solicitante = new Solicitante("775.007.216-09");
        Pedido pedido = new Pedido("Pedido sobremesa", false, solicitante);
        pedido.setProdutos(produtos);

        RepositorioPedido repositorioPedido = RepositorioPedido.getInstance();
        repositorioPedido.adiciona(pedido);
        repositorioPedido.removePorId(1);

        RepositorioSolicitante repositorioSolicitante = RepositorioSolicitante.getInstance();

        try{
            repositorioSolicitante.removePorId("775.007.216-09");
        }catch(BancoDadosException ex){
            fail("O solicitante deveria ter sido liberado para exclusão.", ex);
        }

        try{
            repositorioProduto.removePorId(1);
        }catch(BancoDadosException ex){
            fail("O produto deveria ter sido liberado para exclusão.", ex);
        }

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
