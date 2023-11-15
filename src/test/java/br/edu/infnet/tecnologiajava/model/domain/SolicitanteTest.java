package br.edu.infnet.tecnologiajava.model.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
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
        try{
            new Solicitante(cpf, "João", "joao@dominio.com.br");
        }catch(ValidadorException ex){
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
    }
}
