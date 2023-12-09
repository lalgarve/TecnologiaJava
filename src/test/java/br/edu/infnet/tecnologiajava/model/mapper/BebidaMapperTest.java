package br.edu.infnet.tecnologiajava.model.mapper;

import br.edu.infnet.tecnologiajava.ValidadorException;
import br.edu.infnet.tecnologiajava.model.domain.Bebida;
import br.edu.infnet.tecnologiajava.model.domain.Produto;
import br.edu.infnet.tecnologiajava.services.mapper.MapperException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BebidaMapperTest {
    @BeforeEach
    void resetaCodigo() {
        Bebida.inicializaContadorCodigo();
    }

    @Test
    void testBuild() throws ValidadorException {
        BebidaMapper mapper = new BebidaMapper();
        mapper.reset();
        mapper.setValor("nome", "Cerveja");
        mapper.setValor("marca", "Ambev");
        mapper.setValor("valor", "10.5");
        mapper.setValor("tamanho", "0.5");
        mapper.setValor("gelada", "false");
        Produto resultado = mapper.build();
        Produto esperado = new Bebida(1, "Cerveja", "Ambev", 0.5f, false, 10.5f);
        assertEquals(esperado, resultado);
    }

    @Test
    void testBuildCampoDuplicado() {
        BebidaMapper mapper = new BebidaMapper();
        mapper.reset();
        mapper.setValor("nome", "Cerveja");
        MapperException excecao = assertThrows(MapperException.class,
                () -> mapper.setValor("nome", "Cerveja"));
        assertEquals("O campo nome já foi setado.", excecao.getMessage());
    }

    @Test
    void testBuildUmCampoFaltando() {
        BebidaMapper mapper = new BebidaMapper();
        mapper.reset();
        mapper.setValor("nome", "Cerveja");
        mapper.setValor("marca", "Ambev");
        mapper.setValor("valor", "10.5");
        mapper.setValor("tamanho", "0.5");
        MapperException excecao = assertThrows(MapperException.class, mapper::build);
        assertEquals("O campo gelada não foi setado.", excecao.getMessage());
    }

    @Test
    void testBuildTresCampoaFaltando() {
        BebidaMapper mapper = new BebidaMapper();
        mapper.reset();
        mapper.setValor("nome", "Cerveja");
        mapper.setValor("marca", "Ambev");
        MapperException excecao = assertThrows(MapperException.class, mapper::build);
        assertEquals("Os seguintes campos não foram setados: valor, tamanho, gelada.", excecao.getMessage());
    }

    @Test
    void testResetNaoAcionado() {
        BebidaMapper mapper = new BebidaMapper();
        MapperException excecao = assertThrows(MapperException.class,
                () -> mapper.setValor("nome", "Cerveja"));
        assertEquals("O método reset não foi chamado.", excecao.getMessage());
    }

    @Test
    void testResetNaoAcionadoDepoisBuild() {
        BebidaMapper mapper = new BebidaMapper();
        mapper.reset();
        mapper.setValor("nome", "Cerveja");
        mapper.setValor("marca", "Ambev");
        mapper.setValor("valor", "10.5");
        mapper.setValor("tamanho", "0.5");
        mapper.setValor("gelada", "false");
        mapper.build();
        MapperException excecao = assertThrows(MapperException.class,
                () -> mapper.setValor("nome", "Coca"));
        assertEquals("O método reset não foi chamado.", excecao.getMessage());
    }

    @Test
    void testSetValorCampoInexistente() {
        BebidaMapper mapper = new BebidaMapper();
        mapper.reset();
        MapperException excecao = assertThrows(MapperException.class,
                () -> mapper.setValor("Nome", "pudim"));
        assertEquals("O campo Nome não existe.", excecao.getMessage());
    }

    @Test
    void testBuildErroValidacao() {
        BebidaMapper mapper = new BebidaMapper();
        mapper.reset();
        mapper.setValor("nome", "Cerveja");
        mapper.setValor("marca", "Ambev");
        mapper.setValor("valor", "10.5");
        mapper.setValor("tamanho", "-0.5");
        mapper.setValor("gelada", "false");
        MapperException excecao = assertThrows(MapperException.class, mapper::build);
        assertEquals("Informação mapeada inválida.", excecao.getMessage());
        assertEquals(ValidadorException.class, excecao.getCause().getClass());
    }
}
