package br.edu.infnet.tecnologiajava.model.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.edu.infnet.tecnologiajava.model.domain.Bebida;
import br.edu.infnet.tecnologiajava.model.domain.ValidadorException;
import br.edu.infnet.tecnologiajava.services.csv.CSVMapperException;

public class BebidaMapperTest {
    @BeforeEach
    public void resetaCodigo() {
        Bebida.inicializaContadorCodigo();
    }

    @Test
    public void testBuild() throws ValidadorException {
        BebidaMapper mapper = new BebidaMapper();
        mapper.reset();
        mapper.setValor("nome", "Cerveja");
        mapper.setValor("marca", "Ambev");
        mapper.setValor("valor", "10.5");
        mapper.setValor("tamanho", "0.5");
        mapper.setValor("gelada", "false");
        Bebida resultado = mapper.build();
        Bebida esperado = new Bebida(1, "Cerveja", "Ambev", 0.5f, false, 10.5f);
        assertEquals(esperado, resultado);
    }

    @Test
    public void testBuildCampoDuplicado() throws ValidadorException {
        BebidaMapper mapper = new BebidaMapper();
        mapper.reset();
        mapper.setValor("nome", "Cerveja");
        CSVMapperException excecao = assertThrows(CSVMapperException.class,
                () -> mapper.setValor("nome", "Cerveja"));
        assertEquals("O campo nome já foi setado.", excecao.getMessage());
    }

    @Test
    public void testBuildUmCampoFaltando() throws ValidadorException {
        BebidaMapper mapper = new BebidaMapper();
        mapper.reset();
        mapper.setValor("nome", "Cerveja");
        mapper.setValor("marca", "Ambev");
        mapper.setValor("valor", "10.5");
        mapper.setValor("tamanho", "0.5");
        CSVMapperException excecao = assertThrows(CSVMapperException.class,
                () -> mapper.build());
        assertEquals("O campo gelada não foi setado.", excecao.getMessage());
    }

    @Test
    public void testBuildTrêsCampoaFaltando() throws ValidadorException {
        BebidaMapper mapper = new BebidaMapper();
        mapper.reset();
        mapper.setValor("nome", "Cerveja");
        mapper.setValor("marca", "Ambev");
        CSVMapperException excecao = assertThrows(CSVMapperException.class,
                () -> mapper.build());
        assertEquals("Os seguintes campos não foram setados: valor, tamanho, gelada.", excecao.getMessage());
    }

    @Test
    public void testResetNaoAcionado() {
        BebidaMapper mapper = new BebidaMapper();
        CSVMapperException excecao = assertThrows(CSVMapperException.class,
                () -> mapper.setValor("nome", "Cerveja"));
        assertEquals("O método reset não foi chamado.", excecao.getMessage());
    }

    @Test
    public void testResetNaoAcionadoDepoisBuild() {
        BebidaMapper mapper = new BebidaMapper();
        mapper.reset();
        mapper.setValor("nome", "Cerveja");
        mapper.setValor("marca", "Ambev");
        mapper.setValor("valor", "10.5");
        mapper.setValor("tamanho", "0.5");
        mapper.setValor("gelada", "false");
        mapper.build();
        CSVMapperException excecao = assertThrows(CSVMapperException.class,
                () -> mapper.setValor("nome", "Coca"));
        assertEquals("O método reset não foi chamado.", excecao.getMessage());         
    } 
    
    @Test
    public void testSetValorCampoInexistente(){
        BebidaMapper mapper = new BebidaMapper();
        mapper.reset();
        CSVMapperException excecao = assertThrows(CSVMapperException.class,
            () -> mapper.setValor("Nome", "pudim"));
        assertEquals("O campo Nome não existe.", excecao.getMessage());  
    }

    @Test
    public void testBuildErroValidacao() { 
        BebidaMapper mapper = new BebidaMapper();
        mapper.reset();
        mapper.setValor("nome", "Cerveja");
        mapper.setValor("marca", "Ambev");
        mapper.setValor("valor", "10.5");
        mapper.setValor("tamanho", "-0.5");
        mapper.setValor("gelada", "false");
        CSVMapperException excecao = assertThrows(CSVMapperException.class,
                () -> mapper.build());
        assertEquals("Informação mapeada inválida.", excecao.getMessage()); 
        assertEquals(ValidadorException.class, excecao.getCause().getClass());        
    }    
}
