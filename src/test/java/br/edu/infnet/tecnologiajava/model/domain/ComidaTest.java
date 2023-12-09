package br.edu.infnet.tecnologiajava.model.domain;

import br.edu.infnet.tecnologiajava.ValidadorException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class ComidaTest {
    @TestFactory
    Collection<DynamicTest> testHashCode() throws ValidadorException {
        Comida comida = new Comida(1, "Escondidinho", "batata, carne, azeitonas", 0.5f, false, 20.5f);
        int hashCode = comida.hashCode();
        return Arrays.asList(
                dynamicTest("Código diferente",
                        () -> assertNotEquals(hashCode, new Comida(2, "Escondidinho", "batata, carne, azeitonas", 0.5f, false, 20.5f).hashCode())),
                dynamicTest("Nome diferente",
                        () -> assertNotEquals(hashCode, new Comida(1, "Escondidinho 2", "batata, carne, azeitonas", 0.5f, false, 20.5f).hashCode())),
                dynamicTest("Ingredientes diferentes",
                        () -> assertNotEquals(hashCode, new Comida(1, "Escondidinho", "batata, frango, azeitonas", 0.5f, false, 20.5f).hashCode())),
                dynamicTest("Peso diferente",
                        () -> assertNotEquals(hashCode, new Comida(1, "Escondidinho", "batata, carne, azeitonas", 1.5f, false, 20.5f).hashCode())),
                dynamicTest("Vegano diferente",
                        () -> assertNotEquals(hashCode, new Comida(1, "Escondidinho", "batata, carne, azeitonas", 0.5f, true, 20.5f).hashCode())),
                dynamicTest("Valor diferente",
                        () -> assertNotEquals(hashCode, new Comida(1, "Escondidinho", "batata, carne, azeitonas", 0.5f, false, 10.5f).hashCode())),
                dynamicTest("Igual, instancia diferente",
                        () -> assertEquals(hashCode, new Comida(1, "Escondidinho", "batata, carne, azeitonas", 0.5f, false, 20.5f).hashCode()))
        );
    }

    @TestFactory
    Collection<DynamicTest> testGetDetalhes() throws ValidadorException {
        Comida vegano = new Comida("Hambuger", "pão, carne de soja", 0.5f, true, 20.5f);
        Comida naoVegano = new Comida("Escondidinho", "batata, carne, azeitonas", 0.5f, false, 20.5f);
        return Arrays.asList(
                dynamicTest("vegano",
                        () -> assertEquals("vegano, ingredientes: pão, carne de soja - 0,50 kg", vegano.getDetalhe())),
                dynamicTest("não vegano", () -> assertEquals("ingredientes: batata, carne, azeitonas - 0,50 kg",
                        naoVegano.getDetalhe())));
    }

    @Test
    void testToString() throws ValidadorException {
        Comida comida = new Comida(10, "Hamburger", "pão, carne de soja", 0.5f, true, 20.5f);
        assertEquals(
                "Comida: codigo=10, nome=Hamburger, ingredientes=pão, carne de soja, vegano=true, peso=0.50, valor=20.50",
                comida.toString());
    }

    @TestFactory
    Collection<DynamicTest> testGetters() throws ValidadorException {
        Comida comida = new Comida(10, "Hamburger", "pão, carne de soja", 0.5f, true, 20.5f);
        return Arrays.asList(
                dynamicTest("Código", () -> assertEquals(10, comida.getCodigo())),
                dynamicTest("Nome", () -> assertEquals("Hamburger", comida.getNome())),
                dynamicTest("Ingredientes", () -> assertEquals("pão, carne de soja", comida.getIngredientes())),
                dynamicTest("Peso", () -> assertEquals(0.5f, comida.getPeso())),
                dynamicTest("Vegano", () -> assertTrue(comida.isVegano())),
                dynamicTest("Valor", () -> assertEquals(20.5f, comida.getValor()))
        );
    }

    @TestFactory
    Collection<DynamicTest> testValidacao() {
        Executable codigoNegativo = () -> new Comida(-10, "Hamburger", "pão, carne de soja", 0.5f, true, 20.5f);
        Executable nomeNulo = () -> new Comida(10, null, "pão, carne de soja", 0.5f, true, 20.5f);
        Executable nomeEmBranco = () -> new Comida(10, "   ", "pão, carne de soja", 0.5f, true, 20.5f);
        Executable ingredientesNulo = () -> new Comida(10, "Hamburger", null, 0.5f, true, 20.5f);
        Executable ingredientesEmBranco = () -> new Comida(10, "Hamburger", "  ", 0.5f, true, 20.5f);
        Executable pesoZero = () -> new Comida(10, "Hamburger", "pão, carne de soja", 0.0f, true, 20.5f);
        Executable valorZero = () -> new Comida(10, "Hamburger", "pão, carne de soja", 0.5f, true, 0.0f);
        return Arrays.asList(
                dynamicTest("Código Negativo", () -> testValidacao("O código precisa ser maior que zero", codigoNegativo)),
                dynamicTest("Nome nulo", () -> testValidacao("O nome não pode ser nulo", nomeNulo)),
                dynamicTest("Nome em branco", () -> testValidacao("O nome não pode estar em branco", nomeEmBranco)),
                dynamicTest("Ingredientes nulo", () -> testValidacao("Os ingredientes não podem ser nulo", ingredientesNulo)),
                dynamicTest("Ingredientes em branco", () -> testValidacao("Os ingredientes não podem estar em branco", ingredientesEmBranco)),
                dynamicTest("Peso Zero", () -> testValidacao("O peso precisa ser maior que zero", pesoZero)),
                dynamicTest("Valor Zero", () -> testValidacao("O valor precisa ser maior que zero", valorZero))
        );
    }

    private void testValidacao(String mensagemEsperada, Executable criadorComida) {
        ValidadorException excecao = assertThrows(ValidadorException.class, criadorComida);
        assertEquals("Há campos da comida inválidos: " + mensagemEsperada.toLowerCase() + ".", excecao.getMessage());
        assertEquals(mensagemEsperada, excecao.getValidador().getMensagens().get(0));
    }

    @Test
    void testJson() throws ValidadorException, IOException {
        Comida comida = new Comida(20, "Arroz", "arroz, sal", 1.5f, true, 10.5f);
        ObjectMapper objectMapper = new ObjectMapper();
        assertTrue(objectMapper.canSerialize(Comida.class));
        String comidaString = objectMapper.writeValueAsString(comida);
        Comida copiaComida = objectMapper.readValue(comidaString, Comida.class);
        assertEquals(comida, copiaComida);
    }
}
