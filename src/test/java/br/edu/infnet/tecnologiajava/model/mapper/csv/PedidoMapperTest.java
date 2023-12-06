package br.edu.infnet.tecnologiajava.model.mapper.csv;

import br.edu.infnet.tecnologiajava.model.domain.*;
import br.edu.infnet.tecnologiajava.model.mapper.csv.PedidoMapper;
import br.edu.infnet.tecnologiajava.model.mapper.csv.ProdutoDesconhecido;
import br.edu.infnet.tecnologiajava.services.mapper.csv.CSVMapperException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PedidoMapperTest {

    @BeforeEach
    void resetaCodigo() {
        Pedido.inicializaContadorCodigo();
    }

    @Test
    void testResetObrigatoriedade() {
        PedidoMapper pedidoMapper = new PedidoMapper();
        CSVMapperException excecao = assertThrows(CSVMapperException.class,
                () -> pedidoMapper.setValor("descricao", "Pedido"));
        assertEquals("O método reset não foi chamado.", excecao.getMessage());
    }

    @Test
    void testBuild() throws ValidadorException {
        PedidoMapper pedidoMapper = new PedidoMapper();
        pedidoMapper.reset();
        pedidoMapper.setValor("descricao", "Pedido 1");
        pedidoMapper.setValor("web", "true");
        pedidoMapper.setValor("data", "2023-02-10T12:30");
        pedidoMapper.setValor("cpfSolicitante", "318.308.309-45");
        pedidoMapper.setValor("produtos", "10,3,4");
        Pedido pedido = pedidoMapper.build();
        Pedido pedidoEsperado = new Pedido(1, "Pedido 1", LocalDateTime.of(2023, 02,
                10, 12, 30), true, new Solicitante("318.308.309-45"));
        pedidoEsperado.setProdutos(criaProdutos(10,3,4));
        assertEquals(pedidoEsperado, pedido);
    }

    @Test
    void testBuildSolicitanteVazio() throws ValidadorException {
        PedidoMapper pedidoMapper = new PedidoMapper();
        pedidoMapper.reset();
        pedidoMapper.setValor("descricao", "Pedido 1");
        pedidoMapper.setValor("web", "true");
        pedidoMapper.setValor("data", "2023-02-10T12:30");
        pedidoMapper.setValor("cpfSolicitante", null);
        pedidoMapper.setValor("produtos", "10,3,4");
        Pedido pedido = pedidoMapper.build();
        Pedido pedidoEsperado = new Pedido(1, "Pedido 1", LocalDateTime.of(2023, 02,
                10, 12, 30), true, Solicitante.getVazio());
        pedidoEsperado.setProdutos(criaProdutos(10,3,4));
        assertEquals(pedidoEsperado, pedido);
    }

    @Test
    void testBuildCodigoInvalido() throws ValidadorException {
        PedidoMapper pedidoMapper = new PedidoMapper();
        pedidoMapper.reset();
        CSVMapperException excecao = assertThrows(CSVMapperException.class,
                () -> pedidoMapper.setValor("produtos", "10, -3, 4"));
        assertEquals("Código de produto inválido.", excecao.getMessage());
    }

    @Test
    void testBuildCPFInvalido() throws ValidadorException {
            PedidoMapper pedidoMapper = new PedidoMapper();
            pedidoMapper.reset();
            pedidoMapper.setValor("descricao", "Pedido 1");
            pedidoMapper.setValor("web", "true");
            pedidoMapper.setValor("data", "2023-02-10T12:30");
            pedidoMapper.setValor("cpfSolicitante", "318.308.309-44");
            pedidoMapper.setValor("produtos", "10,3,4");
            CSVMapperException excecao = assertThrows(CSVMapperException.class,
                    pedidoMapper::build);
            assertEquals("Informação mapeada inválida.", excecao.getMessage());
    }

    @ParameterizedTest
    @CsvSource (value={"data;11/02/23;11/02/23 não está no formato ano-mês-diaThora:minuto, por exemplo: 2023-01-10T13:30.",
            "Data;2023-10-10;O campo Data não existe.",
            "produtos;aaaa;aaaa não é um número inteiro."},
            delimiter = ';')
    void testCamposInvalidos(String campo, String valor, String mensagem){
        PedidoMapper pedidoMapper = new PedidoMapper();
        pedidoMapper.reset();
        CSVMapperException excecao = assertThrows(CSVMapperException.class, () -> pedidoMapper.setValor(campo, valor));
        assertEquals(mensagem, excecao.getMessage());
    }


    List<Produto> criaProdutos(int... codigos) throws ValidadorException {
       List<Produto> produtos = new ArrayList<>();
       for(int codigo:codigos){
           produtos.add(new ProdutoDesconhecido(codigo));
       }
       return produtos;
    }
}