package br.edu.infnet.tecnologiajava.controller;

import br.edu.infnet.tecnologiajava.model.domain.Sobremesa;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProdutoControllerIT {

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
                .andExpect(status().is(422))
                .andExpect(jsonPath("$.mensagem").value("Conteúdo JSON inválido."))
                .andExpect(jsonPath("$.detalhes[*]").value("O campo Nome não existe."));
    }

    @Test
    void adicionaSobremesaCamposInvalidos() throws Exception {
        String novaSobremesa = "{\"nome\": \" \", \"doce\": true, \"informacao\": \"sobremesa\", " +
                "\"quantidade\": -1, \"valor\": 10}";
        mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/produto/sobremesa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(novaSobremesa))
                .andExpect(status().is(422))
                .andExpect(jsonPath("$.mensagem").value("Conteúdo JSON inválido."))
                .andExpect(jsonPath("$.detalhes[0]").value("O nome não pode estar em branco"))
                .andExpect(jsonPath("$.detalhes[1]").value("A quantidade precisa ser maior que zero"));
    }


}
