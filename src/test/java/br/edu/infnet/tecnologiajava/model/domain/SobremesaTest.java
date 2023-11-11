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
import org.junit.jupiter.api.function.Executable;

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

    @Test
    public void testToString(){
        Sobremesa sobremesa = new Sobremesa(20, "Pudim", true, "sem glútem", 1.5f, 10.5f);
        assertEquals("Sobremesa: codigo=20, nome=Pudim, informacao=sem glútem, doce=true, quantidade=1.50, valor=10.50", sobremesa.toString());
    } 

    @TestFactory
    public Collection<DynamicTest> testValidacao(){
        
        Executable sobremesaCodigoNegativo = () -> new Sobremesa(-20, "Pudim", true, "sem glútem", 1.5f, 10.5f);
        Executable sobremesaCodigoZero = () -> new Sobremesa(0, "Pudim", true, "sem glútem", 1.5f, 10.5f);
        Executable sobremesaNomeNulo = () -> new Sobremesa(20, null, true, "sem glútem", 1.5f, 10.5f);
        Executable sobremesaNomeEmBranco = () -> new Sobremesa(20, "  ", true, "sem glútem", 1.5f, 10.5f);
        Executable sobremesaInformacaoNula = () -> new Sobremesa(20, "Pudim", true, null, 1.5f, 10.5f);
        Executable sobremesaInformacaoEmBranco = () -> new Sobremesa(20, "Pudim", true, "   ", 1.5f, 10.5f);
        Executable sobremesaQuantidadeZero = () -> new Sobremesa(20, "Pudim", true, "sem glútem", 0.0f, 10.5f);
        Executable sobremesaQuantidadeNegativa = () -> new Sobremesa(20, "Pudim", true, "sem glútem", -1.5f, 10.5f);
        Executable sobremesaValorZero = () -> new Sobremesa(20, "Pudim", true, "sem glútem", 1.5f, 0.0f);
        Executable sobremesaValorNegativo = () -> new Sobremesa(20, "Pudim", true, "sem glútem", 1.5f, -10.5f);

        return Arrays.asList(
            dynamicTest("Codigo negativo", () -> 
               testValidacao("O código precisa ser maior que zero", sobremesaCodigoNegativo)),
            dynamicTest("Codigo zero", () -> 
               testValidacao("O código precisa ser maior que zero", sobremesaCodigoZero)) ,
            dynamicTest("Nome nulo", () -> 
               testValidacao("O nome não pode ser nulo", sobremesaNomeNulo)) ,
            dynamicTest("Nome em branco", () -> 
               testValidacao("O nome não pode estar em branco", sobremesaNomeEmBranco)) ,
            dynamicTest("Informção Nula", () -> 
               testValidacao("A informação não pode ser nula", sobremesaInformacaoNula)) ,
            dynamicTest("Informação em branco", () -> 
               testValidacao("A informação não pode estar em branco", sobremesaInformacaoEmBranco)) ,
            dynamicTest("Quantidade zero", () -> 
               testValidacao("A quantidade precisa ser maior que zero", sobremesaQuantidadeZero)) ,
            dynamicTest("Quantidade negativa", () -> 
               testValidacao("A quantidade precisa ser maior que zero", sobremesaQuantidadeNegativa)) ,
            dynamicTest("Valor zero", () -> 
               testValidacao("O valor precisa ser maior que zero", sobremesaValorZero)),
            dynamicTest("Valor negativo", () -> 
               testValidacao("O valor precisa ser maior que zero", sobremesaValorNegativo))                           
        );
    }

    private void testValidacao(String mensagemEsperada, Executable executable){
        ValidadorException excecao = assertThrows(ValidadorException.class, executable);
        List<String> mensagens = excecao.getValidador().getMensagens();
        assertEquals(1, mensagens.size());
        assertEquals(mensagemEsperada, mensagens.get(0));
    }


}