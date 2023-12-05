package br.edu.infnet.tecnologiajava.repository;

import br.edu.infnet.tecnologiajava.controller.ProdutoController;
import br.edu.infnet.tecnologiajava.model.domain.Pedido;
import br.edu.infnet.tecnologiajava.model.domain.Produto;
import br.edu.infnet.tecnologiajava.model.domain.Solicitante;
import br.edu.infnet.tecnologiajava.model.mapper.BebidaMapper;
import br.edu.infnet.tecnologiajava.model.mapper.ComidaMapper;
import br.edu.infnet.tecnologiajava.model.mapper.SobremesaMapper;
import br.edu.infnet.tecnologiajava.model.mapper.SolicitanteMapper;
import br.edu.infnet.tecnologiajava.services.bancodados.BancoDadosException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.List;

import static br.edu.infnet.tecnologiajava.repository.ConfiguracaoRepositorios.carrega;
import static org.junit.jupiter.api.Assertions.*;

class RepositorioProdutoTest {

    private RepositorioProduto repositorioProduto;

    @BeforeEach
    void inicializaRepositorio() throws BancoDadosException {
        Produto.inicializaContadorCodigo();

        repositorioProduto = new RepositorioProduto();
        carrega("/sobremesa.csv", new SobremesaMapper(), repositorioProduto);
        carrega("/bebida.csv", new BebidaMapper(), repositorioProduto);
        carrega("/comida.csv", new ComidaMapper(), repositorioProduto);
    }

    @ParameterizedTest
    @CsvSource (value={"moca;1", "limao_ocasiao;1", "COCA-COLA;12", "COca-cola_quente;6"}, delimiter = ';')
    void buscaPorTexto(String palavrasJuntas, String quantidade) throws BancoDadosException {
        String[] palavras = palavrasJuntas.split("[ _]");
        List<Produto> produtos = repositorioProduto.buscaPorTexto(palavras);
        assertEquals(quantidade, produtos.size()+"");
    }
}