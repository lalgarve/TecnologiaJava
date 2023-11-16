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
    public void resetaCodigo(){
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
        Sobremesa esperado = new Sobremesa(1,"pudim", true, "possui leite", 0.5f, 10.5f);
        assertEquals(esperado, resultado);
    }

    @Test
    public void testBuildCampoDuplicado() throws ValidadorException {
        SobremesaMapper mapper = new SobremesaMapper();
        mapper.reset();
        mapper.setValor("nome", "pudim");
        mapper.setValor("informacao", "possui leite");
        CSVMapperException execao = assertThrows(CSVMapperException.class, 
            () -> mapper.setValor("informacao", "possui açucar"));
        assertEquals("O campo informacao já foi setado.", execao.getMessage());
    }    

 
    @Test
    public void testBuildUmCampoFaltando() throws ValidadorException {
        SobremesaMapper mapper = new SobremesaMapper();
        mapper.reset();
        mapper.setValor("nome", "pudim");
        mapper.setValor("informacao", "possui leite");
        mapper.setValor("quantidade", "0.5");
        mapper.setValor("doce", "true");
        CSVMapperException execao = assertThrows(CSVMapperException.class, 
            () -> mapper.build());
        assertEquals("O campo valor não foi setado.", execao.getMessage());
    }  
    
     @Test
    public void testBuildTrêsCampoaFaltando() throws ValidadorException {
        SobremesaMapper mapper = new SobremesaMapper();
        mapper.reset();
        mapper.setValor("nome", "pudim");
        mapper.setValor("doce", "true");
        CSVMapperException execao = assertThrows(CSVMapperException.class, 
            () -> mapper.build());
        assertEquals("Os seguintes campos não foram setados: informacao, valor, quantidade.", execao.getMessage());
    }       

    @Test
    void testReset() {

    }
}
