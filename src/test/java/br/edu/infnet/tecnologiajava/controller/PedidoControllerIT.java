package br.edu.infnet.tecnologiajava.controller;

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
class PedidoControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void adicionaPedido() throws Exception {
        String novoPedido = """
                 {
                       "descricao": "Pedido adicionado",
                       "data": "2023-11-05T10:30:00",
                       "web": true,
                       "produtos": [3, 6, 10, 33, 40],
                       "cpfSolicitante":"666.395.597-73"
                }
               """;
        mvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/pedido")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(novoPedido))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chave").value(21));
    }

    @Test
    void alteraPedido() throws Exception {
        String novoPedido = """
                 {
                       "codigo": 1,
                       "descricao": "Pedido alterado",
                       "data": "2023-11-05T10:30:00",
                       "web": true,
                       "produtos": [3, 6, 10, 33, 40],
                       "cpfSolicitante":"666.395.597-73"
                }
               """;
        mvc.perform(MockMvcRequestBuilders.put("http://localhost:8080/pedido")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(novoPedido))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("O pedido com chave 1 foi alterado com sucesso."));
    }

    @Test
    void apagaPedido() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("http://localhost:8080/pedido/16")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("O pedido com chave 16 foi apagado com sucesso."));
    }
}
