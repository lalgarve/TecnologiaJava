package br.edu.infnet.tecnologiajava.controller;

import br.edu.infnet.tecnologiajava.model.domain.Produto;
import br.edu.infnet.tecnologiajava.model.domain.Sobremesa;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProdutoControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void consultaProdutoPorChave() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/produto/11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value(11))
                .andExpect(jsonPath("$.nome").value("Coca-Cola"))
                .andExpect(jsonPath("$.gelada").value(false));
    }

    @Test
    void adicionaSobremesa() throws Exception {
        Sobremesa sobremesa = new Sobremesa("Sobremesa", true, "sobremesa",
                1.0f, 10.0f);
        int codigoEsperado = sobremesa.getCodigo()+1;
        String novaSobremesa = "{\"nome\": \"sobremesa\", \"doce\": true, \"informacao\": \"sobremesa\", " +
                "\"quantidade\": 1, \"valor\": 10}";
        mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/produto/sobremesa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(novaSobremesa))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chave").value(codigoEsperado));
    }

    @Test
    void adicionaSobremesaCampoErrado() throws Exception {
        String novaSobremesa = "{\"Nome\": \"sobremesa\", \"doce\": true, \"informacao\": \"sobremesa\", " +
                "\"quantidade\": 1, \"valor\": 10}";
        mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/produto/sobremesa")
                .contentType(MediaType.APPLICATION_JSON)
                .content(novaSobremesa))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Erro processando JSON."))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.detail").value("O campo Nome não existe."))
                .andExpect(jsonPath("$.instance").value("/produto/sobremesa"));
    }

    @Test
    void adicionaSobremesaErroValidacao() throws Exception {
        String novaSobremesa = "{\"nome\": \" \", \"doce\": true, \"informacao\": \"sobremesa\", " +
                "\"quantidade\": -1, \"valor\": 10}";
        mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/produto/sobremesa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(novaSobremesa))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Erro validação."))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.detail").value("o nome não pode estar em branco, a quantidade precisa ser maior que zero"))
                .andExpect(jsonPath("$.instance").value("/produto/sobremesa"));
    }

    @Test
    void alteraSobremesa () throws Exception {
        String sobremesa = "   {\n" +
                "      \"codigo\": 4,\n" +
                "      \"nome\": \"Brigadeiro Branco com Uva\",\n" +
                "      \"doce\": true,\n" +
                "      \"informacao\": \"Uma variação do brigadeiro tradicional que é feita com uvas Itália.\",\n" +
                "      \"quantidade\": 0.1,\n" +
                "      \"valor\": 3.5\n" +
                "   }";
        mvc.perform(MockMvcRequestBuilders.put("http://localhost:8080/produto/sobremesa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sobremesa))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("A sobremesa com chave 4 foi alterada com sucesso."));
    }

    @Test
    void alteraSobremesaValidacaoErrada () throws Exception {
        String novaSobremesa = "   {\n" +
                "      \"codigo\": 4,\n" +
                "      \"nome\": \"Brigadeiro Branco com Uva\",\n" +
                "      \"doce\": true,\n" +
                "      \"informacao\": \"Uma variação do brigadeiro tradicional que é feita com uvas Itália.\",\n" +
                "      \"quantidade\": -0.1,\n" +
                "      \"valor\": 3.5\n" +
                "   }";
        mvc.perform(MockMvcRequestBuilders.put("http://localhost:8080/produto/sobremesa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(novaSobremesa))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Erro validação."))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.detail").value("a quantidade precisa ser maior que zero"))
                .andExpect(jsonPath("$.instance").value("/produto/sobremesa"));
    }

    @Test
    void apagaProduto() throws Exception{
        mvc.perform(MockMvcRequestBuilders.delete("http://localhost:8080/produto/40")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("O produto com chave 40 foi apagado com sucesso."));
    }
    @Test
    void buscaPorProduto() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/produto/busca/doce_tradicional"))
                .andExpect(status().isOk())
                .andReturn();
        String resultado = mvcResult.getResponse().getContentAsString();
        Sobremesa[] produtos = objectMapper.readValue(resultado, Sobremesa[].class);
        assertEquals(2, produtos.length);
        assertEquals(4, produtos[0].getChave());
        assertEquals(6, produtos[1].getChave());
    }

    @Test
    void deletaProdutoSendoUsado() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("http://localhost:8080/produto/6"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Erro acessando banco de dados."))
                .andExpect(jsonPath("$.detail").value("O valor está sendo usado. Chave: 6"))
                .andExpect(jsonPath("$.status").value(HttpStatus.CONFLICT.value()))
                .andExpect(jsonPath("$.instance").value("/produto/6"));
    }

}
