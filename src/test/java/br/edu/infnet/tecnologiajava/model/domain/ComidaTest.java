package br.edu.infnet.tecnologiajava.model.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;

public class ComidaTest {
    @TestFactory
    public Collection<DynamicTest> testEquals() throws ValidadorException {
        Comida comida = new Comida(1, "Escondidinho", "batata, carne, azeitonas", 0.5f, false, 20.5f);
        return Arrays.asList(
                dynamicTest("Código diferente",
                        () -> assertFalse(comida.equals(
                                new Comida(2, "Escondidinho", "batata, carne, azeitonas", 0.5f, false, 20.5f)))),
                dynamicTest("Nome diferente",
                        () -> assertFalse(comida.equals(
                                new Comida(1, "Escondidinho 2", "batata, carne, azeitonas", 0.5f, false, 20.5f)))),
                dynamicTest("Ingredientes diferentes",
                        () -> assertFalse(comida.equals(
                                new Comida(1, "Escondidinho", "batata, frango, azeitonas", 0.5f, false, 20.5f)))),
                dynamicTest("Peso diferente",
                        () -> assertFalse(comida.equals(
                                new Comida(1, "Escondidinho", "batata, carne, azeitonas", 1.5f, false, 20.5f)))),
                dynamicTest("Vegano diferente",
                        () -> assertFalse(comida
                                .equals(new Comida(1, "Escondidinho", "batata, carne, azeitonas", 0.5f, true, 20.5f)))),
                dynamicTest("Valor diferente",
                        () -> assertFalse(comida.equals(
                                new Comida(1, "Escondidinho", "batata, carne, azeitonas", 0.5f, false, 10.5f)))),
                dynamicTest("Mesma Instancia", () -> assertTrue(comida.equals(comida))),
                dynamicTest("Igual, instancia diferente",
                        () -> assertTrue(comida.equals(
                                new Comida(1, "Escondidinho", "batata, carne, azeitonas", 0.5f, false, 20.5f)))),
                dynamicTest("Null", () -> assertFalse(comida.equals(null))),
                dynamicTest("Classe diferente",
                        () -> assertFalse(comida.equals(new Bebida("Cerveja", "Brahma", 1.0f, false, 10.2f)))));
    }

    @TestFactory
    public Collection<DynamicTest> testGetDetalhes() throws ValidadorException {
        Comida vegano = new Comida("Hambuger", "pão, carne de soja", 0.5f, true, 20.5f);
        Comida naoVegano = new Comida("Escondidinho", "batata, carne, azeitonas", 0.5f, false, 20.5f);
        return Arrays.asList(
                dynamicTest("vegano",
                        () -> assertEquals("vegano, ingredientes: pão, carne de soja - 0,50 kg", vegano.getDetalhe())),
                dynamicTest("não vegano", () -> assertEquals("ingredientes: batata, carne, azeitonas - 0,50 kg",
                        naoVegano.getDetalhe())));
    }

    @Test
    public void testToString() throws ValidadorException {
        Comida comida = new Comida(10, "Hamburger", "pão, carne de soja", 0.5f, true, 20.5f);
        assertEquals(
                "Comida: codigo=10, nome=Hamburger, ingredientes=pão, carne de soja, vegano=true, peso=0.50, valor=20.50",
                comida.toString());
    }

    @TestFactory
    public Collection<DynamicTest> testGetters() throws ValidadorException {
        Comida comida = new Comida(10, "Hamburger", "pão, carne de soja", 0.5f, true, 20.5f);
        return Arrays.asList(
                dynamicTest("Código", () -> assertEquals(10, comida.getCodigo())),
                dynamicTest("Nome", () -> assertEquals("Hamburger", comida.getNome())),
                dynamicTest("Ingredientes", () -> assertEquals("pão, carne de soja", comida.getIngredientes())),
                dynamicTest("Peso", () -> assertEquals(0.5f, comida.getPeso())),
                dynamicTest("Vegano", () -> assertEquals(true, comida.isVegano())),
                dynamicTest("Valor", () -> assertEquals(20.5f, comida.getValor()))
        );
    }

    @TestFactory
    public Collection<DynamicTest> testValidacao(){
        Executable codigoNegativo = () -> new Comida(-10, "Hamburger", "pão, carne de soja", 0.5f, true, 20.5f); 
        Executable nomeNulo = () -> new Comida(10, null, "pão, carne de soja", 0.5f, true, 20.5f); 
        Executable nomeEmBranco = () -> new Comida(10, "   ", "pão, carne de soja", 0.5f, true, 20.5f);
        Executable ingredientesNulo = () -> new Comida(10, "Hamburger", null, 0.5f, true, 20.5f);
        Executable ingredientesEmBranco = () -> new Comida(10, "Hamburger", "  ", 0.5f, true, 20.5f);
        Executable pesoZero =  () -> new Comida(10, "Hamburger", "pão, carne de soja", 0.0f, true, 20.5f);
        Executable valorZero =  () -> new Comida(10, "Hamburger", "pão, carne de soja", 0.5f, true, 0.0f);
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

    private void testValidacao(String mensagemEsperada, Executable criadorComida){
        ValidadorException excecao = assertThrows(ValidadorException.class, criadorComida);
        assertEquals("Há campos da comida inválidos: "+mensagemEsperada.toLowerCase()+".", excecao.getMessage());
        assertEquals(mensagemEsperada, excecao.getValidador().getMensagens().get(0));
    }

}