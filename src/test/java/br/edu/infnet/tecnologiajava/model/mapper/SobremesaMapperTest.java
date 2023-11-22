package br.edu.infnet.tecnologiajava.model.mapper;

import br.edu.infnet.tecnologiajava.model.domain.Produto;
import br.edu.infnet.tecnologiajava.model.domain.Sobremesa;
import br.edu.infnet.tecnologiajava.model.domain.ValidadorException;
import br.edu.infnet.tecnologiajava.services.csv.CSVMapperException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SobremesaMapperTest {
    @BeforeEach
    public void resetaCodigo() {
        Sobremesa.inicializaContadorCodigo();
    }

    @Test
    void testBuild() throws ValidadorException {
        SobremesaMapper mapper = new SobremesaMapper();
        mapper.reset();
        mapper.setValor("nome", "pudim");
        mapper.setValor("informacao", "possui leite");
        mapper.setValor("valor", "10.5");
        mapper.setValor("quantidade", "0.5");
        mapper.setValor("doce", "true");
        Produto resultado = mapper.build();
        Produto esperado = new Sobremesa(1, "pudim", true, "possui leite", 0.5f, 10.5f);
        assertEquals(esperado, resultado);
    }

    @Test
    void testBuildCampoDuplicado() {
        SobremesaMapper mapper = new SobremesaMapper();
        mapper.reset();
        mapper.setValor("nome", "pudim");
        mapper.setValor("informacao", "possui leite");
        CSVMapperException excecao = assertThrows(CSVMapperException.class,
                () -> mapper.setValor("informacao", "possui açucar"));
        assertEquals("O campo informacao já foi setado.", excecao.getMessage());
    }

    @Test
    void testBuildUmCampoFaltando() {
        SobremesaMapper mapper = new SobremesaMapper();
        mapper.reset();
        mapper.setValor("nome", "pudim");
        mapper.setValor("informacao", "possui leite");
        mapper.setValor("quantidade", "0.5");
        mapper.setValor("doce", "true");
        CSVMapperException excecao = assertThrows(CSVMapperException.class, mapper::build);
        assertEquals("O campo valor não foi setado.", excecao.getMessage());
    }

    @Test
    void testBuildTresCamposFaltando() {
        SobremesaMapper mapper = new SobremesaMapper();
        mapper.reset();
        mapper.setValor("nome", "pudim");
        mapper.setValor("doce", "true");
        CSVMapperException excecao = assertThrows(CSVMapperException.class, mapper::build);
        assertEquals("Os seguintes campos não foram setados: informacao, valor, quantidade.", excecao.getMessage());
    }

    @Test
    void testResetNaoAcionado() {
        SobremesaMapper mapper = new SobremesaMapper();
        CSVMapperException excecao = assertThrows(CSVMapperException.class,
                () -> mapper.setValor("nome", "pudim"));
        assertEquals("O método reset não foi chamado.", excecao.getMessage());
    }

    @Test
    void testResetNaoAcionadoDepoisBuild() {
        SobremesaMapper mapper = new SobremesaMapper();
        mapper.reset();
        mapper.setValor("nome", "pudim");
        mapper.setValor("informacao", "possui leite");
        mapper.setValor("valor", "10.5");
        mapper.setValor("quantidade", "0.5");
        mapper.setValor("doce", "true");
        mapper.build();
        CSVMapperException excecao = assertThrows(CSVMapperException.class,
                () -> mapper.setValor("nome", "pudim"));
        assertEquals("O método reset não foi chamado.", excecao.getMessage());
    }

    @ParameterizedTest
    @CsvSource (value = {"doce:sim:sim não é um valor booleano.",
                         "valor:aaaa:aaaa não é um número ponto flutuante.",
                         "Nome:pudim:O campo Nome não existe."}, delimiter = ':')
    void testValoresECamposInvalido(String campo, String valor, String mensagem) {
        SobremesaMapper mapper = new SobremesaMapper();
        mapper.reset();
        CSVMapperException excecao = assertThrows(CSVMapperException.class,
                () -> mapper.setValor(campo, valor));
        assertEquals(mensagem, excecao.getMessage());
    }



    @Test
    void testBuildErroValidacao() {
        SobremesaMapper mapper = new SobremesaMapper();
        mapper.reset();
        mapper.setValor("nome", "pudim");
        mapper.setValor("informacao", "    ");
        mapper.setValor("valor", "10.5");
        mapper.setValor("quantidade", "0.5");
        mapper.setValor("doce", "true");
        CSVMapperException excecao = assertThrows(CSVMapperException.class, mapper::build);
        assertEquals("Informação mapeada inválida.", excecao.getMessage());
        assertEquals(ValidadorException.class, excecao.getCause().getClass());
    }

}
