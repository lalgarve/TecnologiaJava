package br.edu.infnet.tecnologiajava.model.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class SolicitanteTest {

    @TestFactory
    public Collection<DynamicTest> testValidaCPF() {
        return Arrays.asList(
                dynamicTest("CPF inválido os dois dígitos", () -> testValidaCPF("062.427.708-44")),
                dynamicTest("CPF inválido digito1", () -> testValidaCPF("062.427.708-80")),
                dynamicTest("CPF inválido digito2", () -> testValidaCPF("062.427.708-91"))

        );
    }

    @TestFactory
    public Collection<DynamicTest> testValidaFormatoCPF() {
        return Arrays.asList(
                dynamicTest("CPF sem pontuação", () -> testValidaFormatoCPF("12312323433")),
                dynamicTest("Número errado dígitos", () -> testValidaFormatoCPF("123.123.234-333")));
    }

    @ParameterizedTest
    @MethodSource("forneceCPFValido")
    public void testCPFValido(String cpf) {
        try {
            new Solicitante(cpf, "João", "joao@dominio.com.br");
        } catch (ValidadorException ex) {
            fail(cpf + " é válido.", ex);
        }
    }

    public static Stream<String> forneceCPFValido() {
        return Stream.of(
                "062.427.708-90", "775.007.216-09", "307.137.992-77",
                "666.395.597-73", "929.204.815-50", "745.374.268-45",
                "256.382.736-11", "524.620.629-71", "740.373.745-87",
                "627.011.240-00", "943.861.323-41", "313.053.449-01",
                "186.033.558-60", "778.855.996-20", "318.308.309-45",
                "203.599.258-39", "006.572.898-09", "941.509.429-00",
                "054.202.451-91", "339.664.423-96");
    }

    @TestFactory
    public Collection<DynamicTest> testValidacoesSimples() {
        Executable cpfNulo = () -> new Solicitante(null, "João", "joao@google.com");
        Executable cpfEmBranco = () -> new Solicitante("   ", "João", "joao@google.com");
        Executable nomeNulo = () -> new Solicitante("062.427.708-90", null, "joao@google.com");
        Executable nomeEmBranco = () -> new Solicitante("062.427.708-90", "  ", "joao@google.com");
        Executable emailNulo = () -> new Solicitante("062.427.708-90", "João", null);
        Executable emailEmBranco = () -> new Solicitante("062.427.708-90", "João", "  ");
        Executable emailSemArroba = () -> new Solicitante("062.427.708-90", "João", "joaogmail.com");
        Executable emailSemUsuario = () -> new Solicitante("062.427.708-90", "João", "@gmail.com");
        Executable emailDuasArrobas = () -> new Solicitante("062.427.708-90", "João", "joao@@gmail.com");
        Executable emailDominioInvalido = () -> new Solicitante("062.427.708-90", "João", "joao@gmail");
        return Arrays.asList(
                dynamicTest("CPF Nulo",
                        () -> testValidaCampo("O CPF não pode ser nulo", cpfNulo)),
                dynamicTest("CPF em Branco",
                        () -> testValidaCampo("O CPF não pode estar em branco", cpfEmBranco)),
                dynamicTest("Nome Nulo",
                        () -> testValidaCampo("O nome não pode ser nulo", nomeNulo)),
                dynamicTest("Nome em Branco",
                        () -> testValidaCampo("O nome não pode estar em branco", nomeEmBranco)),
                dynamicTest("Email Nulo",
                        () -> testValidaCampo("O email não pode ser nulo", emailNulo)),
                dynamicTest("Email em Branco",
                        () -> testValidaCampo("O email não pode estar em branco", emailEmBranco)),
                dynamicTest("Email Sem Arroba",
                        () -> testValidaCampo("O email é inválido", emailSemArroba)),
                dynamicTest("Email Sem Usuário",
                        () -> testValidaCampo("O email é inválido", emailSemUsuario)),
                dynamicTest("Email Duas Arrobas",
                        () -> testValidaCampo("O email é inválido", emailDuasArrobas)),
                dynamicTest("Email Sem Usuário",
                        () -> testValidaCampo("O email é inválido", emailDominioInvalido))

        );

    }

    private void testValidaCPF(String cpf) {
        Executable criaSolicitante = () -> new Solicitante(cpf, "Luiz da Silva", "meuemail@domain.com");
        testValidaCampo("O CPF é inválido", criaSolicitante);
    }

    private void testValidaFormatoCPF(String cpf) {
        Executable criaSolicitante = () -> new Solicitante(cpf, "Luiz da Silva", "meuemail@domain.com");
        testValidaCampo("O formato do CPF é inválido", criaSolicitante);
    }

    private void testValidaCampo(String mensagemEsperada, Executable criaSolicitante) {
        ValidadorException excecao = assertThrows(ValidadorException.class, criaSolicitante);
        assertEquals(mensagemEsperada, excecao.getValidador().getMensagens().get(0));
        String mensagemExcecao = "Há erros nos dados do solicitante: " + mensagemEsperada + ".";
        assertEquals(mensagemExcecao.toLowerCase(), excecao.getMessage().toLowerCase());
    }

    @TestFactory
    public Collection<DynamicTest> testGetters() throws ValidadorException {
        Solicitante solicitante = new Solicitante("062.427.708-90", "João", "joao@yahoo.com.br");
        return Arrays.asList(
                dynamicTest("Nome", () -> assertEquals("João", solicitante.getNome())),
                dynamicTest("email", () -> assertEquals("joao@yahoo.com.br", solicitante.getEmail())),
                dynamicTest("cpf", () -> assertEquals("062.427.708-90", solicitante.getCPF())));
    }

    @Test
    public void testToString() throws ValidadorException {
        Solicitante solicitante = new Solicitante("062.427.708-90", "João", "joao@yahoo.com.br");
        assertEquals("Solicitante: cpf=062.427.708-90, nome=João, email=joao@yahoo.com.br", solicitante.toString());
    }

    @Test
    public void testToStringVazio() {
        assertEquals("Solicitante vazio", Solicitante.getVazio().toString());
    }

    @TestFactory
    public Collection<DynamicTest> testEquals() throws ValidadorException{
        Solicitante solicitante = new Solicitante("062.427.708-90", "João", "joao@yahoo.com.br");
        return Arrays.asList(
            dynamicTest("CPF diferente", 
               () -> assertFalse(solicitante.equals(new Solicitante("775.007.216-09", "João", "joao@yahoo.com.br")))),
            dynamicTest("Nome diferente", 
               () -> assertFalse(solicitante.equals(new Solicitante("062.427.708-90", "João Maria", "joao@yahoo.com.br")))) ,
            dynamicTest("Email diferente", 
               () -> assertFalse(solicitante.equals(new Solicitante("062.427.708-90", "João", "joao@yahoo2.com.br")))),
            dynamicTest("Mesma instance", 
                () -> assertTrue(solicitante.equals(solicitante))),                 
            dynamicTest("Igual, instancia diferente", 
                () -> assertTrue(solicitante.equals(new Solicitante("062.427.708-90", "João", "joao@yahoo.com.br")))),
            dynamicTest("Classe diferente", 
                () -> assertFalse(solicitante.equals(Boolean.FALSE))),
            dynamicTest("Instancia Vazia", 
                () -> assertFalse(solicitante.equals(Solicitante.getVazio()))),
            dynamicTest("Valor nulo", 
                () -> assertFalse(solicitante.equals(null)))                                      
                            
        );
    }

}
