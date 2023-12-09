package br.edu.infnet.tecnologiajava.model.domain;

import br.edu.infnet.tecnologiajava.ValidadorException;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class BebidaTest {
    @TestFactory
    Collection<DynamicTest> testEquals() throws ValidadorException {
        Bebida bebida = new Bebida(20, "Cerveja1", "Brahma", 1.0f, true, 10.5f);
        return Arrays.asList(
                dynamicTest("Codigo diferente", () ->
                        assertNotEquals(bebida, new Bebida(21, "Cerveja1", "Brahma", 1.0f, true, 10.5f))),
                dynamicTest("Nome diferente", () ->
                        assertNotEquals(bebida, new Bebida(20, "Cerveja2", "Brahma", 1.0f, true, 10.5f))),
                dynamicTest("Marca diferente", () ->
                        assertNotEquals(bebida, new Bebida(20, "Cerveja1", "Antartica", 1.0f, true, 10.5f))),
                dynamicTest("Tamanho diferente", () ->
                        assertNotEquals(bebida, new Bebida(20, "Cerveja1", "Brahma", 0.5f, true, 10.5f))),
                dynamicTest("Gelada diferente", () ->
                        assertNotEquals(bebida, new Bebida(20, "Cerveja1", "Brahma", 1.0f, false, 10.5f))),
                dynamicTest("Valor diferente", () ->
                        assertNotEquals(bebida, new Bebida(20, "Cerveja1", "Brahma", 1.0f, true, 5.5f))),
                dynamicTest("Mesma instancia", () ->
                        assertEquals(bebida, bebida)),
                dynamicTest("Null", () ->
                        assertNotEquals(null, bebida)),
                dynamicTest("Igual, instancia diferente", () ->
                        assertEquals(bebida, new Bebida(20, "Cerveja1", "Brahma", 1.0f, true, 10.5f))),
                dynamicTest("Classe diferente", () ->
                        assertNotEquals(bebida, new Sobremesa("pudim", true, "sem gosto", 1.0f, 1.0f)))
        );

    }

    @TestFactory
    Collection<DynamicTest> testHashCode() throws ValidadorException {
        Bebida bebida = new Bebida(20, "Cerveja1", "Brahma", 1.0f, true, 10.5f);
        int hashCode = bebida.hashCode();
        return Arrays.asList(
                dynamicTest("Codigo diferente", () ->
                        assertNotEquals(hashCode, new Bebida(21, "Cerveja1", "Brahma", 1.0f, true, 10.5f).hashCode())),
                dynamicTest("Nome diferente", () ->
                        assertNotEquals(hashCode, new Bebida(20, "Cerveja2", "Brahma", 1.0f, true, 10.5f).hashCode())),
                dynamicTest("Marca diferente", () ->
                        assertNotEquals(hashCode, new Bebida(20, "Cerveja1", "Antartica", 1.0f, true, 10.5f).hashCode())),
                dynamicTest("Tamanho diferente", () ->
                        assertNotEquals(hashCode, new Bebida(20, "Cerveja1", "Brahma", 0.5f, true, 10.5f).hashCode())),
                dynamicTest("Gelada diferente", () ->
                        assertNotEquals(hashCode, new Bebida(20, "Cerveja1", "Brahma", 1.0f, false, 10.5f).hashCode())),
                dynamicTest("Valor diferente", () ->
                        assertNotEquals(hashCode, new Bebida(20, "Cerveja1", "Brahma", 1.0f, true, 5.5f).hashCode())),
                dynamicTest("Igual, instancia diferente", () ->
                        assertEquals(hashCode, new Bebida(20, "Cerveja1", "Brahma", 1.0f, true, 10.5f).hashCode()))
        );

    }

    @TestFactory
    Collection<DynamicTest> testGetDetalhes() {
        return Arrays.asList(
                dynamicTest("Bebida gelada", () ->
                        assertEquals("Bebida Brahma - 1,0 L - gelada",
                                new Bebida("Cerveja", "Brahma", 1.0f, true, 10.5f).getDetalhe())),
                dynamicTest("Bebida quente", () ->
                        assertEquals("Bebida Brahma - 1,0 L - quente",
                                new Bebida("Cerveja", "Brahma", 1.0f, false, 10.5f).getDetalhe())),
                dynamicTest("Duas casas decimais", () ->
                        assertEquals("Bebida Brahma - 1,0 L - quente",
                                new Bebida("Cerveja", "Brahma", 1.02f, false, 10.5f).getDetalhe()))
        );

    }

    @Test
    void testToString() throws ValidadorException {
        Bebida bebida = new Bebida(20, "Cerveja1", "Brahma", 1.0f, true, 10.5f);
        assertEquals("Bebida: codigo=20, nome=Cerveja1, marca=Brahma, tamanho=1.0, gelada=true, valor=10.50", bebida.toString());
    }

    @TestFactory
    Collection<DynamicTest> testValidacao1Erro() {
        return Arrays.asList(
                dynamicTest("Codigo negativo",
                        () -> testValidacao1Erro("o código precisa ser maior que zero", -1, "Cerveja", "Brahma", 1.0f, false, 10.5f)),
                dynamicTest("Nome nulo",
                        () -> testValidacao1Erro("o nome não pode ser nulo", 1, null, "Brahma", 1.0f, false, 10.5f)),
                dynamicTest("Nome em branco",
                        () -> testValidacao1Erro("o nome não pode estar em branco", 1, "    ", "Brahma", 1.0f, false, 10.5f)),
                dynamicTest("Marca nula",
                        () -> testValidacao1Erro("a marca não pode ser nula", 1, "Cerveja", null, 1.0f, false, 10.5f)),
                dynamicTest("Marca em branco",
                        () -> testValidacao1Erro("a marca não pode estar em branco", 1, "Cerveja", "   ", 1.0f, false, 10.5f)),
                dynamicTest("Tamanho igual a zero.",
                        () -> testValidacao1Erro("o tamanho precisa estar entre 0,1 L e 10 L", 1, "Cerveja", "Brahma", 0f, false, 10.5f)),
                dynamicTest("Tamanho maior que 10.",
                        () -> testValidacao1Erro("o tamanho precisa estar entre 0,1 L e 10 L", 1, "Cerveja", "Brahma", 15f, false, 10.5f)),
                dynamicTest("Valor igual a zero.",
                        () -> testValidacao1Erro("o valor precisa ser maior que zero", 1, "Cerveja", "Brahma", 1.0f, false, 0.0f)),
                dynamicTest("Valor negativo.",
                        () -> testValidacao1Erro("o valor precisa ser maior que zero", 1, "Cerveja", "Brahma", 1.0f, false, -1.0f))
        );
    }

    void testValidacao1Erro(String mensagem, int codigo, String nome, String marca,
                            float tamanho, boolean gelada, float valor) {
        ValidadorException excecao = assertThrows(ValidadorException.class, () -> new Bebida(codigo, nome, marca, tamanho, gelada, valor));
        assertEquals("Há campos da bebida inválidos: " + mensagem + ".", excecao.getMessage());
        assertTrue(excecao.getValidador().temErro());
        List<String> mensagens = excecao.getValidador().getMensagens();
        assertEquals(1, mensagens.size());
        assertEquals(mensagem.toLowerCase(), mensagens.get(0).toLowerCase());
    }

    @ParameterizedTest
    @ValueSource(floats = {0.1f, 10.f})
    void testLimiteVolume(float limite) {
        try {
            new Bebida(20, "Cerveja1", "Brahma", limite, true, 10.5f);
        } catch (ValidadorException ex) {
            fail(limite + " é válido", ex);
        }
    }

    @ParameterizedTest
    @ValueSource(floats = {0.1f, 10.0f})
    void testeValidacaoTamanhoExtremos(float tamanho) {
        try {
            new Bebida("Cerveja", "Brahma", tamanho, true, 10.5f);
        } catch (ValidadorException ex) {
            fail(String.format("O valor do tamanho é válido: %.1f", tamanho), ex);
        }
    }

    @Test
    void testeValidacao2Erros() {
        ValidadorException excecao = assertThrows(ValidadorException.class, () -> new Bebida("Cerveja", " ", 0.1f, false, 0.0f));
        List<String> mensagens = excecao.getValidador().getMensagens();
        assertEquals(2, mensagens.size());
        assertTrue(mensagens.contains("A marca não pode estar em branco"));
        assertTrue(mensagens.contains("O valor precisa ser maior que zero"));
        String mensagem = excecao.getMessage();
        assertTrue(mensagem.contains("a marca não pode estar em branco"));
        assertTrue(mensagem.contains("o valor precisa ser maior que zero"));
    }

}
