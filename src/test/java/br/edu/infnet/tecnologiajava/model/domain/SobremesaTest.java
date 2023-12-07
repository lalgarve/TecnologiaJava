package br.edu.infnet.tecnologiajava.model.domain;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class SobremesaTest {

    @TestFactory
    Collection<DynamicTest> testHascode() throws ValidadorException {
        Sobremesa sobremesa = new Sobremesa(1, "Pudim", true, "sem glútem", 1.0f, 1.0f);
        int hascode = sobremesa.hashCode();
        return Arrays.asList(
                dynamicTest("Código diferente", () ->
                        assertNotEquals(hascode, new Sobremesa(2, "Pudim", true, "sem glútem", 1.0f, 1.0f).hashCode())),
                dynamicTest("Nome diferente", () ->
                        assertNotEquals(hascode, new Sobremesa(1, "Pudim Leite", true, "sem glútem", 1.0f, 1.0f).hashCode())),
                dynamicTest("Informação diferente", () ->
                        assertNotEquals(hascode, new Sobremesa(1, "Pudim", true, "com glútem", 1.0f, 1.0f).hashCode())),
                dynamicTest("Quantidade diferente", () ->
                        assertNotEquals(hascode, new Sobremesa(1, "Pudim", true, "sem glútem", 2.0f, 1.0f).hashCode())),
                dynamicTest("Valor diferente", () ->
                        assertNotEquals(hascode, new Sobremesa(1, "Pudim", true, "sem glútem", 1.0f, 2.0f).hashCode())),
                dynamicTest("Doce diferente", () ->
                        assertNotEquals(hascode, new Sobremesa(1, "Pudim", false, "sem glútem", 1.0f, 1.0f).hashCode())),
                dynamicTest("Igual, instância diferente", () ->
                        assertEquals(hascode, new Sobremesa(1, "Pudim", true, "sem glútem", 1.0f, 1.0f).hashCode()))
        );
    }

    @TestFactory
    Collection<DynamicTest> testGetDetalhes() throws ValidadorException {
        Sobremesa sobremesaDoce = new Sobremesa("Pudim", true, "sem glútem", 1.5f, 10.5f);
        Sobremesa sobremesaSalgada = new Sobremesa("Pudim", false, "sem glútem", 1.5f, 10.5f);
        Sobremesa sobremesaTresCasas = new Sobremesa("Pudim", true, "sem glútem", 1.534f, 10.5f);
        return Arrays.asList(
                dynamicTest("Sobremesa doce", () ->
                        assertEquals("doce sem glútem - 1,50 Kg", sobremesaDoce.getDetalhe())),
                dynamicTest("Sobremesa salgada", () ->
                        assertEquals("salgada sem glútem - 1,50 Kg", sobremesaSalgada.getDetalhe())),
                dynamicTest("Sobremesa doce", () ->
                        assertEquals("doce sem glútem - 1,53 Kg", sobremesaTresCasas.getDetalhe()))
        );
    }

    @Test
    void testToString() throws ValidadorException {
        Sobremesa sobremesa = new Sobremesa(20, "Pudim", true, "sem glútem", 1.5f, 10.5f);
        assertEquals("Sobremesa: codigo=20, nome=Pudim, informacao=sem glútem, doce=true, quantidade=1.50, valor=10.50", sobremesa.toString());
    }

    @TestFactory
    Collection<DynamicTest> testValidacao() {

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
                        testValidacao("O código precisa ser maior que zero", sobremesaCodigoZero)),
                dynamicTest("Nome nulo", () ->
                        testValidacao("O nome não pode ser nulo", sobremesaNomeNulo)),
                dynamicTest("Nome em branco", () ->
                        testValidacao("O nome não pode estar em branco", sobremesaNomeEmBranco)),
                dynamicTest("Informção Nula", () ->
                        testValidacao("A informação não pode ser nula", sobremesaInformacaoNula)),
                dynamicTest("Informação em branco", () ->
                        testValidacao("A informação não pode estar em branco", sobremesaInformacaoEmBranco)),
                dynamicTest("Quantidade zero", () ->
                        testValidacao("A quantidade precisa ser maior que zero", sobremesaQuantidadeZero)),
                dynamicTest("Quantidade negativa", () ->
                        testValidacao("A quantidade precisa ser maior que zero", sobremesaQuantidadeNegativa)),
                dynamicTest("Valor zero", () ->
                        testValidacao("O valor precisa ser maior que zero", sobremesaValorZero)),
                dynamicTest("Valor negativo", () ->
                        testValidacao("O valor precisa ser maior que zero", sobremesaValorNegativo))
        );
    }

    private void testValidacao(String mensagemEsperada, Executable executable) {
        ValidadorException excecao = assertThrows(ValidadorException.class, executable);
        List<String> mensagens = excecao.getValidador().getMensagens();
        assertEquals(1, mensagens.size());
        assertEquals(mensagemEsperada, mensagens.get(0));
    }

    @Test
    void testJson() throws ValidadorException, IOException {
        Sobremesa sobremesa = new Sobremesa(20, "Pudim", true, "sem glútem", 1.5f, 10.5f);
        ObjectMapper objectMapper = new ObjectMapper();
        assertTrue(objectMapper.canSerialize(Sobremesa.class));
        String sobremesaString = objectMapper.writeValueAsString(sobremesa);
        Sobremesa copiaSobremesa = objectMapper.readValue(sobremesaString, Sobremesa.class);
        assertEquals(sobremesa, copiaSobremesa);
    }


}
