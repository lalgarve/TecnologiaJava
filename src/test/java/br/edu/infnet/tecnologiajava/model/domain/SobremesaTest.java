package br.edu.infnet.tecnologiajava.model.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class SobremesaTest {
    @TestFactory
    public Collection<DynamicTest> testEquals() {
        Sobremesa sobremesa = new Sobremesa(1, "Pudim", true, "sem glútem", 1.0f,1.0f);
        return Arrays.asList(
            dynamicTest("Código diferente", () ->
                assertFalse(sobremesa.equals(new Sobremesa(2, "Pudim", true, "sem glútem", 1.0f,1.0f)))),
            dynamicTest("Nome diferente", () ->
                assertFalse(sobremesa.equals(new Sobremesa(1, "Pudim Leite", true, "sem glútem", 1.0f,1.0f)))),
            dynamicTest("Informação diferente", () ->
                assertFalse(sobremesa.equals(new Sobremesa(1, "Pudim", true, "com glútem", 1.0f,1.0f)))),
            dynamicTest("Quantidade diferente", () ->
                assertFalse(sobremesa.equals(new Sobremesa(1, "Pudim", true, "sem glútem", 2.0f,1.0f)))),
            dynamicTest("Valor diferente", () ->
                assertFalse(sobremesa.equals(new Sobremesa(1, "Pudim", true, "sem glútem", 1.0f,2.0f)))),
            dynamicTest("Doce diferente", () ->
                assertFalse(sobremesa.equals(new Sobremesa(1, "Pudim", false, "sem glútem", 1.0f,1.0f)))),    
            dynamicTest("Mesma instância", () ->
                assertTrue(sobremesa.equals(sobremesa))),
            dynamicTest("Igual, instância diferente", () ->
                assertTrue(sobremesa.equals(new Sobremesa(1, "Pudim", true, "sem glútem", 1.0f,1.0f)))),
            dynamicTest("Valor nulo", () ->
                assertFalse(sobremesa.equals(null))),
            dynamicTest("Classe diferente", () ->
                assertFalse(sobremesa.equals(new Bebida(20, "Cerveja1", "Brahma", 1.0f, true, 10.5f))))                              
        );
    }

    @TestFactory
    public Collection<DynamicTest> testGetDetalhes() {
        Sobremesa sobremesaDoce = new Sobremesa("Pudim", true, "sem glútem", 1.5f, 10.5f);
        Sobremesa sobremesaSalgada = new Sobremesa("Pudim", false, "sem glútem", 1.5f, 10.5f);
        Sobremesa sobremesaTresCasas = new Sobremesa("Pudim", true, "sem glútem", 1.534f, 10.5f);
        return Arrays.asList(
            dynamicTest("Sobremesa doce", () -> 
               assertEquals("Sobremesa doce sem glútem - 1,50 Kg", sobremesaDoce.getDetalhe())),
            dynamicTest("Sobremesa salgada", () -> 
               assertEquals("Sobremesa salgada sem glútem - 1,50 Kg", sobremesaSalgada.getDetalhe())),
            dynamicTest("Sobremesa doce", () -> 
               assertEquals("Sobremesa doce sem glútem - 1,53 Kg", sobremesaTresCasas.getDetalhe()))    
        );
    }

    @TestFactory
    public Collection<DynamicTest> testGetters(){
        Sobremesa sobremesa = new Sobremesa(20, "Pudim", true, "sem glútem", 1.5f, 10.5f);
        return Arrays.asList(
            dynamicTest("Código", () -> assertEquals(20, sobremesa.getCodigo())),
            dynamicTest("Nome", () -> assertEquals("Pudim", sobremesa.getNome())),
            dynamicTest("Doce", () -> assertEquals(true, sobremesa.isDoce())),
            dynamicTest("Informação", () -> assertEquals("sem glútem", sobremesa.getInformacao())),
            dynamicTest("Quantidade", () -> assertEquals(1.5f, sobremesa.getQuantidade())),
            dynamicTest("Valor", () -> assertEquals(10.5f, sobremesa.getValor()))
        );
    }


}
