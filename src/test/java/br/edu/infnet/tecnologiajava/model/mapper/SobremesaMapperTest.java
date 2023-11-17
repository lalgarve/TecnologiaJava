package br.edu.infnet.tecnologiajava.model.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.edu.infnet.tecnologiajava.model.domain.Sobremesa;
import br.edu.infnet.tecnologiajava.model.domain.ValidadorException;
import br.edu.infnet.tecnologiajava.services.csv.CSVMapperException;

public class SobremesaMapperTest {
    @BeforeEach
    public void resetaCodigo() {
        Sobremesa.inicializaContadorCodigo();
    }

    @Test
    public void testBuild() throws ValidadorException {
        SobremesaMapper mapper = new SobremesaMapper();
        mapper.reset();
        mapper.setValor("nome", "pudim");
        mapper.setValor("informacao", "possui leite");
        mapper.setValor("valor", "10.5");
        mapper.setValor("quantidade", "0.5");
        mapper.setValor("doce", "true");
        Sobremesa resultado = mapper.build();
        Sobremesa esperado = new Sobremesa(1, "pudim", true, "possui leite", 0.5f, 10.5f);
        assertEquals(esperado, resultado);
    }

    @Test
    public void testBuildCampoDuplicado() throws ValidadorException {
        SobremesaMapper mapper = new SobremesaMapper();
        mapper.reset();
        mapper.setValor("nome", "pudim");
        mapper.setValor("informacao", "possui leite");
        CSVMapperException excecao = assertThrows(CSVMapperException.class,
                () -> mapper.setValor("informacao", "possui açucar"));
        assertEquals("O campo informacao já foi setado.", excecao.getMessage());
    }

    @Test
    public void testBuildUmCampoFaltando() throws ValidadorException {
        SobremesaMapper mapper = new SobremesaMapper();
        mapper.reset();
        mapper.setValor("nome", "pudim");
        mapper.setValor("informacao", "possui leite");
        mapper.setValor("quantidade", "0.5");
        mapper.setValor("doce", "true");
        CSVMapperException excecao = assertThrows(CSVMapperException.class,
                () -> mapper.build());
        assertEquals("O campo valor não foi setado.", excecao.getMessage());
    }

    @Test
    public void testBuildTrêsCampoaFaltando() throws ValidadorException {
        SobremesaMapper mapper = new SobremesaMapper();
        mapper.reset();
        mapper.setValor("nome", "pudim");
        mapper.setValor("doce", "true");
        CSVMapperException excecao = assertThrows(CSVMapperException.class,
                () -> mapper.build());
        assertEquals("Os seguintes campos não foram setados: informacao, valor, quantidade.", excecao.getMessage());
    }

    @Test
    public void testResetNaoAcionado() {
        SobremesaMapper mapper = new SobremesaMapper();
        CSVMapperException excecao = assertThrows(CSVMapperException.class,
                () -> mapper.setValor("nome", "pudim"));
        assertEquals("O método reset não foi chamado.", excecao.getMessage());        
    }

    @Test
    public void testResetNaoAcionadoDepoisBuild() {
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
    
    @Test
    public void testSetValorCampoInexistente(){
        SobremesaMapper mapper = new SobremesaMapper();
        mapper.reset();
        CSVMapperException excecao = assertThrows(CSVMapperException.class,
            () -> mapper.setValor("Nome", "pudim"));
        assertEquals("O campo Nome não existe.", excecao.getMessage());     
    }

    @Test
    public void testSetValorFloatInvalido(){
        SobremesaMapper mapper = new SobremesaMapper();
        mapper.reset();
        CSVMapperException excecao = assertThrows(CSVMapperException.class,
            () -> mapper.setValor("valor", "aaaa"));
        assertEquals("aaaa não é um número ponto flutuante.", excecao.getMessage());
    }

 
    @Test
    public void testSetValorBooleanoInvalido(){
        SobremesaMapper mapper = new SobremesaMapper();
        mapper.reset();
        CSVMapperException excecao = assertThrows(CSVMapperException.class,
            () -> mapper.setValor("doce", "sim"));
        assertEquals("sim não é um valor booleano.", excecao.getMessage());
    }   

    @Test
    public void testBuildErroValidacao(){
        SobremesaMapper mapper = new SobremesaMapper();
        mapper.reset();
        mapper.setValor("nome", "pudim");
        mapper.setValor("informacao", "    ");
        mapper.setValor("valor", "10.5");
        mapper.setValor("quantidade", "0.5");
        mapper.setValor("doce", "true");     
        CSVMapperException excecao = assertThrows(CSVMapperException.class,
                () -> mapper.build());
        assertEquals("Informação mapeada inválida.", excecao.getMessage()); 
        assertEquals(ValidadorException.class, excecao.getCause().getClass());
    }
    
}
