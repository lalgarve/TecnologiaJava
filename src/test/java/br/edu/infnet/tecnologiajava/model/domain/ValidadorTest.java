package br.edu.infnet.tecnologiajava.model.domain;

import br.edu.infnet.tecnologiajava.Validador;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidadorTest {
    private final List<String> mensagens;

    ValidadorTest() {
        mensagens = Arrays.asList(
                "Mensagem 1", "Mensagem 2", "Mensagem 3", "Mensagem 4"
        );
    }


    @Test
    void testSemErros() {
        Validador validador = new Validador();
        mensagens.forEach((mensagem) -> validador.valida(mensagem, true));
        assertTrue(validador.getMensagens().isEmpty());
        assertEquals("", validador.getMensagensConcatenadas());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Mensagem 1", "Mensagem 2", "Mensagem 3", "Mensagem 4"})
    void testUmaErrada(String mensagemErrada) {
        Validador validador = new Validador();
        mensagens.forEach((mensagem) -> validador.valida(mensagem, !mensagem.equals(mensagemErrada)));
        assertEquals(1, validador.getMensagens().size());
        assertEquals(mensagemErrada, validador.getMensagens().get(0));
        assertEquals(mensagemErrada.toLowerCase(), validador.getMensagensConcatenadas());
    }

    @Test
    void testTodasErradas() {
        Validador validador = new Validador();
        mensagens.forEach((mensagem) -> validador.valida(mensagem, false));
        assertEquals(4, validador.getMensagens().size());
        assertArrayEquals(mensagens.toArray(), validador.getMensagens().toArray());
        assertEquals("mensagem 1, mensagem 2, mensagem 3, mensagem 4", validador.getMensagensConcatenadas());
    }

    @Test
    void testToStringSemErro() {
        Validador validador = new Validador();
        mensagens.forEach((mensagem) -> validador.valida(mensagem, true));
        assertEquals("Validador sem erros.", validador.toString());
    }


    @Test
    void testToStringComErro() {
        Validador validador = new Validador();
        mensagens.forEach((mensagem) -> validador.valida(mensagem, false));
        assertEquals("Validador com 4 erro(s): mensagem 1, mensagem 2, mensagem 3, mensagem 4.", validador.toString());
    }

}
