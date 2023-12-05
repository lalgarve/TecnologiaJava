package br.edu.infnet.tecnologiajava.controller;

import br.edu.infnet.tecnologiajava.model.domain.Bebida;
import br.edu.infnet.tecnologiajava.model.domain.Produto;
import br.edu.infnet.tecnologiajava.model.domain.ValidadorException;
import br.edu.infnet.tecnologiajava.repository.ConfiguracaoRepositorios;
import br.edu.infnet.tecnologiajava.repository.RepositorioProduto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


@RestClientTest(ProdutoController.class)
@ContextConfiguration(classes = {ConfiguracaoRepositorios.class, ProdutoController.class}, loader = AnnotationConfigContextLoader.class)
class ProdutoControllerTest {
    @Autowired
    private ProdutoController produtoController;

    private MockRestServiceServer server;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RepositorioProduto repositorioProduto;

    @BeforeEach
    public void setUp() {
        server= MockRestServiceServer.createServer(new RestTemplate());
    }

    @Test
    void consultaProdutoPorChave() throws ValidadorException, JsonProcessingException {
        Produto produto = new Bebida(11, "Coca-Cola", "Coca-Cola Company", 2.0f, false, 5.99f);
        String produtoString = objectMapper.writeValueAsString(produto);
        this.server.expect(requestTo("/produto/11"))
        .andRespond(withSuccess(produtoString, MediaType.APPLICATION_JSON));
    }
}