package br.edu.infnet.tecnologiajava.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SolicitanteControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void adicionaSolicitante() throws Exception {
        String novoSolicitante = """
                {
                      "cpf": "313.053.449-01",
                      "nome": "Carlos Oliveira",
                      "email": "carlos.oliveira@yahoo.com"
                   }""";
        mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/solicitante")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(novoSolicitante))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chave").value("313.053.449-01"));
    }

    @Test
    void alteraSolicitante() throws Exception {
        String novoSolicitante = """
                {
                      "cpf": "186.033.558-60",
                      "nome": "Ana Oliveira da Silva",
                      "email": "ana.oliveira@gmail.com"
                   }""";
        mvc.perform(MockMvcRequestBuilders.put("http://localhost:8080/solicitante")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(novoSolicitante))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("Solicitante com cpf 186.033.558-60 alterado com sucesso."));
    }

    @Test
    void alteraSolicitanteEmailInvalido() throws Exception {
        String novoSolicitante = """
                {
                      "cpf": "186.033.558-60",
                      "nome": "Ana Oliveira da Silva",
                      "email": "ana.oliveira@gmail"
                   }""";
        mvc.perform(MockMvcRequestBuilders.put("http://localhost:8080/solicitante")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(novoSolicitante))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Erro validação."))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.detail").value("o email é inválido"))
                .andExpect(jsonPath("$.instance").value("/solicitante"));
    }

    @Test
    void apagaSolicitante() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("http://localhost:8080/solicitante/961.584.180-30")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("O solicitante com o cpf 961.584.180-30 foi apagado com sucesso."));
    }

}
