package br.edu.infnet.tecnologiajava.model.domain;

import br.edu.infnet.tecnologiajava.Validador;
import br.edu.infnet.tecnologiajava.ValidadorException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class SolicitanteTest {

    @TestFactory
    Collection<DynamicTest> testValidaCPF() {
        return Arrays.asList(
                dynamicTest("CPF inválido os dois dígitos", () -> testValidaCPF("062.427.708-44")),
                dynamicTest("CPF inválido digito1", () -> testValidaCPF("062.427.708-80")),
                dynamicTest("CPF inválido digito2", () -> testValidaCPF("062.427.708-91"))

        );
    }

    @TestFactory
    Collection<DynamicTest> testValidaFormatoCPF() {
        return Arrays.asList(
                dynamicTest("CPF sem pontuação", () -> testValidaFormatoCPF("12312323433")),
                dynamicTest("Número errado dígitos", () -> testValidaFormatoCPF("123.123.234-333")));
    }

    @ParameterizedTest
    @MethodSource("forneceCPFValido")
    void testCPFValido(String cpf) {
        try {
            new Solicitante(cpf, "João", "joao@dominio.com.br");
        } catch (ValidadorException ex) {
            fail(cpf + " é válido.", ex);
        }
    }

    static Stream<String> forneceCPFValido() {
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
    Collection<DynamicTest> testValidacoesSimples() {
        Executable nomeNulo = () -> new Solicitante("062.427.708-90", null, "joao@google.com");
        Executable nomeEmBranco = () -> new Solicitante("062.427.708-90", "  ", "joao@google.com");
        Executable emailNulo = () -> new Solicitante("062.427.708-90", "João", null);
        Executable emailEmBranco = () -> new Solicitante("062.427.708-90", "João", "  ");
        Executable emailSemArroba = () -> new Solicitante("062.427.708-90", "João", "joaogmail.com");
        Executable emailSemUsuario = () -> new Solicitante("062.427.708-90", "João", "@gmail.com");
        Executable emailDuasArrobas = () -> new Solicitante("062.427.708-90", "João", "joao@@gmail.com");
        Executable emailDominioInvalido = () -> new Solicitante("062.427.708-90", "João", "joao@gmail");
        return Arrays.asList(
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

    @Test
    void testToString() throws ValidadorException {
        Solicitante solicitante = new Solicitante("062.427.708-90", "João", "joao@yahoo.com.br");
        assertEquals("Solicitante: cpf=062.427.708-90, nome=João, email=joao@yahoo.com.br", solicitante.toString());
    }

    @Test
    void testToStringVazio() {
        assertEquals("Solicitante vazio", Solicitante.getVazio().toString());
    }

    @Test
    void testCriaSolicitanteCPFValido() {
        try {
            Solicitante solicitante = new Solicitante("062.427.708-90");
            assertFalse(solicitante.podeSerGravadoNoBanco());
        } catch (ValidadorException ex) {
            fail(ex);
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {149, 150, 151, 8000})
    void testLimiteEmail(int tamanhoEmail) {
        String email = geraEmail(tamanhoEmail);
        assertEquals(tamanhoEmail, email.length());
        try {
            new Solicitante("929.204.815-50", "João", email);
            assertTrue(tamanhoEmail <= 150);
        } catch (ValidadorException ex) {
            if (tamanhoEmail <= 150) {
                fail("Email tem " + email.length() + " caracteres");
            }
            Validador validador = ex.getValidador();
            assertEquals("O email pode ter no máximo 150 caracteres", validador.getMensagens().get(0));
        }

    }

    private String geraEmail(int numeroCaracteres) {
        int tamanhoLogin = ((numeroCaracteres / 3) - 2) * 3;
        int tamanhoDominio = numeroCaracteres - tamanhoLogin - 5;
        StringBuilder email = new StringBuilder();
        for (int i = 0; i < tamanhoLogin; i += 3) {
            email.append('a');
            email.append('.');
            email.append('b');
        }
        email.append('@');
        for (int i = 0; i < tamanhoDominio; i++) {
            email.append('b');
        }
        email.append(".com");
        return email.toString();
    }

    @ParameterizedTest
    @MethodSource("valoresVaziosValidos")
    void validaVazioOkConstrutorFull(String cpf, String nome, String email) {
        try {
            Solicitante solicitante = new Solicitante(cpf, nome, email);
            assertEquals(Solicitante.getVazio(), solicitante);
        } catch (ValidadorException ex) {
            fail(ex);
        }
    }

    private static Stream<Arguments> valoresVaziosValidos() {
        return Stream.of(
                Arguments.of("000.000.000-00", "", ""),
                Arguments.of("000.000.000-00", null, ""),
                Arguments.of("000.000.000-00", "", null),
                Arguments.of("000.000.000-00", "  ", "  "),
                Arguments.of(null, null, null),
                Arguments.of("", "", ""),
                Arguments.of("", null, ""),
                Arguments.of("", "", null),
                Arguments.of("  ", "  ", "  "),
                Arguments.of("  ", "", ""),
                Arguments.of("  ", null, ""),
                Arguments.of("  ", "", null),
                Arguments.of("  ", "  ", "  ")
        );
    }

    @ParameterizedTest
    @MethodSource("valoresVaziosInvalidos")
    void validaVazioNotOkConstrutorFull(String cpf, String nome, String email, String mensagem) {
        ValidadorException excecao = assertThrows(ValidadorException.class, () -> new Solicitante(cpf, nome, email));
        Validador validador = excecao.getValidador();
        assertEquals(mensagem, validador.getMensagens().get(0));
    }

    private static Stream<Arguments> valoresVaziosInvalidos() {
        return Stream.of(
                Arguments.of("000.000.000-00", "", "joao@gmail.com",
                        "Se CPF é 000.000.000-00, em branco ou nulo, o email precisa ser em branco ou nulo"),
                Arguments.of("000.000.000-00", "joão", null,
                        "Se CPF é 000.000.000-00, em branco ou nulo, o nome precisa ser em branco ou nulo"),
                Arguments.of("000.000.000-00", "  ", "joao@gmail.com",
                        "Se CPF é 000.000.000-00, em branco ou nulo, o email precisa ser em branco ou nulo"),
                Arguments.of("", "joão", "",
                        "Se CPF é 000.000.000-00, em branco ou nulo, o nome precisa ser em branco ou nulo"),
                Arguments.of("  ", "", "joao@gmail.com",
                        "Se CPF é 000.000.000-00, em branco ou nulo, o email precisa ser em branco ou nulo"),
                Arguments.of("  ", null, "joao@gmail.com",
                        "Se CPF é 000.000.000-00, em branco ou nulo, o email precisa ser em branco ou nulo"),
                Arguments.of("  ", "maria", "  ",
                        "Se CPF é 000.000.000-00, em branco ou nulo, o nome precisa ser em branco ou nulo")
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "", "000.000.000-00"})
    void testConstrutorApenasCPFBranco(String cpf) {
        try {
            Solicitante solicitante = new Solicitante(cpf);
            assertTrue(solicitante.podeSerGravadoNoBanco());
            assertEquals(Solicitante.getVazio(), solicitante);
        } catch (ValidadorException ex) {
            fail(ex);
        }
    }

    @Test
    void testConstrutorApenasCPFNulo() {
        try {
            Solicitante solicitante = new Solicitante(null);
            assertEquals(Solicitante.getVazio(), solicitante);
        } catch (ValidadorException ex) {
            fail(ex);
        }
    }

    @Test
    void testJson() throws ValidadorException, IOException {
        Solicitante solicitante = new Solicitante("929.204.815-50", "Maria Fátima", "fatima@gmail.com");
        ObjectMapper objectMapper = new ObjectMapper();
        assertTrue(objectMapper.canSerialize(Solicitante.class));
        String solicitanteString = objectMapper.writeValueAsString(solicitante);
        Solicitante copiaSolicitante = objectMapper.readValue(solicitanteString, Solicitante.class);
        assertEquals(solicitante, copiaSolicitante);
    }

}
