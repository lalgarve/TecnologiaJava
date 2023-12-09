package br.edu.infnet.tecnologiajava.controller;

import br.edu.infnet.tecnologiajava.model.view.RelatorioProdutosVendidos;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RelatoriosControllerIT {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void relatorioComResultado() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("http://localhost:8080//relatorio/vendas/2023/10"))
                .andExpect(status().isOk())
                .andReturn();
        String resultado = mvcResult.getResponse().getContentAsString();
        RelatorioProdutosVendidos relatorio = objectMapper.readValue(resultado, RelatorioProdutosVendidos.class);
        assertEquals(2023, relatorio.getAno());
        assertEquals(10, relatorio.getMes());
        assertFalse(relatorio.getDadosVenda().isEmpty());
        assertTrue(relatorio.getMensagem().startsWith("O valor total vendido"));
    }

    @Test
    void relatorioSemResultado() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("http://localhost:8080//relatorio/vendas/2023/05"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ano").value(2023))
                .andExpect(jsonPath("$.mes").value(05))
                .andExpect(jsonPath("$.mensagem").value("Não há dados de venda para o período."));
    }
}
