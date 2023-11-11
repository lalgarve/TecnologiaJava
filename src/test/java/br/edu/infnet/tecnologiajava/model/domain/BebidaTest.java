package br.edu.infnet.tecnologiajava.model.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

public class BebidaTest {
    @TestFactory
    public Collection<DynamicTest> testEquals() {
        Bebida bebida = new Bebida(20, "Cerveja1", "Brahma", 1.0f, true, 10.5f);
        return Arrays.asList(
            dynamicTest("Codigo diferente", ()->
                assertFalse(bebida.equals(new Bebida(21, "Cerveja1", "Brahma", 1.0f, true, 10.5f)))),
            dynamicTest("Nome diferente", ()->
                assertFalse(bebida.equals(new Bebida(20, "Cerveja2", "Brahma", 1.0f, true, 10.5f)))),
            dynamicTest("Marca diferente", ()->
                assertFalse(bebida.equals(new Bebida(20, "Cerveja1", "Antartica", 1.0f, true, 10.5f)))),
            dynamicTest("Tamanho diferente", ()->
                assertFalse(bebida.equals(new Bebida(20, "Cerveja1", "Brahma", 0.5f, true, 10.5f)))),
            dynamicTest("Gelada diferente", ()->
                assertFalse(bebida.equals(new Bebida(20, "Cerveja1", "Brahma", 1.0f, false, 10.5f)))),
            dynamicTest("Valor diferente", ()->
                assertFalse(bebida.equals(new Bebida(20, "Cerveja1", "Brahma", 1.0f, true, 5.5f))))
        );

    }

    @TestFactory
    public Collection<DynamicTest> testGetDetalhes() {
        return Arrays.asList(
            dynamicTest("Bebida gelada", () -> 
               assertEquals( "Bebida Cerveja Brahma - 1,0 L - gelada",
               new Bebida("Cerveja", "Brahma", 1.0f, true, 10.5f).getDetalhe())),
            dynamicTest("Bebida quente", () -> 
               assertEquals( "Bebida Cerveja Brahma - 1,0 L - quente",
               new Bebida("Cerveja", "Brahma", 1.0f, false, 10.5f).getDetalhe())),
            dynamicTest("Duas casas decimais", () -> 
               assertEquals( "Bebida Cerveja Brahma - 1,0 L - quente",
               new Bebida("Cerveja", "Brahma", 1.02f, false, 10.5f).getDetalhe()))               
                
        );

    }

    @TestFactory
    public Collection<DynamicTest> testGetters(){
        Bebida bebida = new Bebida(20, "Cerveja1", "Brahma", 1.0f, true, 10.5f);
        return Arrays.asList(
            dynamicTest("nome", () -> assertEquals("Cerveja1", bebida.getNome())),
            dynamicTest("valor", () -> assertEquals(10.5f, bebida.getValor())),
            dynamicTest("codigo", () -> assertEquals(20, bebida.getCodigo())),
            dynamicTest("tamanho", () -> assertEquals(1.0f, bebida.getTamanho())),
            dynamicTest("marca", () -> assertEquals("Brahma", bebida.getMarca()))
        );
        
    }

    @Test
    public void testToString() {
        Bebida bebida = new Bebida(20, "Cerveja1", "Brahma", 1.0f, true, 10.5f);
        assertEquals("Bebida: codigo=20, nome=Cerveja1, marca=Brahma, tamanho=1.0, gelada=true, valor=10.50", bebida.toString());
    }

    @TestFactory
    public Collection<DynamicTest> testValidacao1Erro(){
        return Arrays.asList(
            dynamicTest("Codigo negativo", 
               () -> testValidacao1Erro("O código precisa ser maior que zero.", -1, "Cerveja", "Brahma", 1.0f, false, 10.5f))
        );
    }

    private void testValidacao1Erro(String mensagem, int codigo, String nome, String marca, 
        float tamanho, boolean gelada, float valor){
        ValidadorException excecao = assertThrows(ValidadorException.class, () -> new Bebida(codigo, nome, marca, tamanho, gelada, valor));
        assertEquals("Há campos da bebida inválidos: "+mensagem, excecao.getMessage());
        assertTrue(excecao.getValidador().temErro());
        List<String> mensagens = excecao.getValidador().getMensagens();
        assertEquals(1, mensagens.size());
        assertEquals(mensagem, mensagens.get(0));
    }

    
}
