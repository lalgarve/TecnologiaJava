package br.edu.infnet.tecnologiajava.model.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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
                assertFalse(bebida.equals(new Bebida(20, "Cerveja1", "Brahma", 1.0f, true, 5.5f)))),
            dynamicTest("Mesma instancia", ()-> 
                assertTrue(bebida.equals(bebida))),
            dynamicTest("Null", ()-> 
                assertFalse(bebida.equals(null))),
            dynamicTest("Igual, instancia diferente", ()-> 
                assertTrue(bebida.equals(new Bebida(20, "Cerveja1", "Brahma", 1.0f, true, 10.5f)))),
            dynamicTest("Classe diferente", ()-> 
                assertFalse(bebida.equals(new Sobremesa("pudim", true, "sem gosto", 1.0f, 1.0f))))      
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
            dynamicTest("marca", () -> assertEquals("Brahma", bebida.getMarca())),
            dynamicTest("gelada", () -> assertEquals(true, bebida.isGelada()))
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

    private void testValidacao1Erro(String mensagem, int codigo, String nome, String marca, 
        float tamanho, boolean gelada, float valor){
        ValidadorException excecao = assertThrows(ValidadorException.class, () -> new Bebida(codigo, nome, marca, tamanho, gelada, valor));
        assertEquals("Há campos da bebida inválidos: "+mensagem+".", excecao.getMessage());
        assertTrue(excecao.getValidador().temErro());
        List<String> mensagens = excecao.getValidador().getMensagens();
        assertEquals(1, mensagens.size());
        assertEquals(mensagem.toLowerCase(), mensagens.get(0).toLowerCase());
    }

    @ParameterizedTest
    @ValueSource(floats = {0.1f, 10.0f})
    public void testeValidacaoTamanhoExtremos(float tamanho){
        try{
            new Bebida("Cerveja", "Brahma", tamanho, true, 10.5f);
        }catch(ValidadorException ex){
            fail(String.format("O valor do tamanho é válido: %.1f", tamanho), ex);
        }
    }   

    @Test
    public void testeValidacao2Erros(){
        ValidadorException excecao = assertThrows(ValidadorException.class,() -> new Bebida("Cerveja", " ", 0.1f, false, 0.0f));
        List<String> mensagens = excecao.getValidador().getMensagens();
        assertEquals(2, mensagens.size());      
        assertTrue(mensagens.contains("A marca não pode estar em branco"));
        assertTrue(mensagens.contains("O valor precisa ser maior que zero"));
        String mensagem = excecao.getMessage();
        assertTrue(mensagem.contains("a marca não pode estar em branco"));
        assertTrue(mensagem.contains("o valor precisa ser maior que zero"));
    } 
    
}
