package br.edu.infnet.tecnologiajava.repository;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BuscaTextoTest {

    @Test
    void normalizaTexto() {
        BuscaTexto instancia = new BuscaTexto() {
            @Override
            public List<?> buscaPorTexto(String[] palavras) {
                return null;
            }
        };

        String esperado = "aaaaeeiooouc_aeia_s";
        String resultado = instancia.normalizaTexto("áàãâéêíóôõúç_aeiã_s");
        assertEquals(esperado, resultado);
    }


}