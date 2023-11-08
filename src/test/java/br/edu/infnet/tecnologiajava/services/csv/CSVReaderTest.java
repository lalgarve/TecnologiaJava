
package br.edu.infnet.tecnologiajava.services.csv;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CSVReaderTest {

  private static final String CABECALHO_CORRETO = "codigo,existe,valor,descricao";
  private static final String CABECALHO_INCORRETO1 = "codigo1,existe,valor,descricao";
  private static final String LINHA_SIMPLES1 = "1,true,10.3,descricao1";
  private static final String LINHA_SIMPLES2 = "2,false,10.5,descricao2";
  private static final String LINHA_SIMPLES3 = "3,true,10.6,descricao3";
  private static final String LINHA_DESCRICAO_COM_VIRGULA_FIM = "1,true,10.6,\"descricao1, virgula\"";

  private static final Map<String, ModeloTeste> mapaEsperados = new HashMap<>();

  public CSVReaderTest() {
  }

  @BeforeAll
  public static void preencheObjetosEsperados() {
    ModeloTeste modeloTeste;

    modeloTeste = new ModeloTeste();
    modeloTeste.codigo = 1;
    modeloTeste.descricao = "descricao1";
    modeloTeste.valor = 10.3f;
    modeloTeste.existe = true;
    mapaEsperados.put(LINHA_SIMPLES1, modeloTeste);

    modeloTeste = new ModeloTeste();
    modeloTeste.codigo = 2;
    modeloTeste.descricao = "descricao2";
    modeloTeste.valor = 10.5f;
    modeloTeste.existe = false;
    mapaEsperados.put(LINHA_SIMPLES2, modeloTeste);

    modeloTeste = new ModeloTeste();
    modeloTeste.codigo = 3;
    modeloTeste.descricao = "descricao3";
    modeloTeste.valor = 10.6f;
    modeloTeste.existe = true;
    mapaEsperados.put(LINHA_SIMPLES3, modeloTeste);

    modeloTeste = new ModeloTeste();
    modeloTeste.codigo = 1;
    modeloTeste.descricao = "descricao1, virgula";
    modeloTeste.valor = 10.6f;
    modeloTeste.existe = true;
    mapaEsperados.put(LINHA_DESCRICAO_COM_VIRGULA_FIM, modeloTeste);
    
  }

  @Test
  public void testLeDados() throws CSVMapperException, IOException {
    StringBuilder builder = new StringBuilder();
    builder.append(CABECALHO_CORRETO).append('\n')
            .append(LINHA_SIMPLES1).append('\n');
    ByteArrayInputStream bis = new ByteArrayInputStream(builder.toString().getBytes());
    try (CSVReader<ModeloTeste> instance = new CSVReader<>(new InputStreamReader(bis), new TestMapper())) {
      ModeloTeste esperado = mapaEsperados.get(LINHA_SIMPLES1);
      ModeloTeste resultado = instance.leDados().findFirst().get();
      assertEquals(esperado, resultado);
    }
  }

  @Test
  public void testLeDadosCabecalhoIncorreto1() throws CSVMapperException, IOException {
    StringBuilder builder = new StringBuilder();
    builder.append(CABECALHO_INCORRETO1).append('\n')
            .append(LINHA_SIMPLES1).append('\n');
    ByteArrayInputStream bis = new ByteArrayInputStream(builder.toString().getBytes());
    try (CSVReader<ModeloTeste> instance = new CSVReader<>(new InputStreamReader(bis), new TestMapper())) {
      ModeloTeste resultado = instance.leDados().findFirst().get();
      fail("Excecao esperada");
    } catch (CSVMapperException ex) {
      assertEquals("codigo1 não existe.", ex.getMessage());
    }
  }

  @Test
  public void testLeDadosApenasCabecalho() throws IOException {
    StringBuilder builder = new StringBuilder();
    builder.append(CABECALHO_CORRETO).append('\n');
    ByteArrayInputStream bis = new ByteArrayInputStream(builder.toString().getBytes());
    try (CSVReader<ModeloTeste> instance = new CSVReader<>(new InputStreamReader(bis), new TestMapper())) {
      Optional<ModeloTeste> resultado = instance.leDados().findFirst();
      assertTrue(resultado.isEmpty(), "Stream deve estar vazia.");
    }
  }

  @Test
  public void testLeDadosVazio() throws IOException {
    StringBuilder builder = new StringBuilder();
    builder.append('\n');
    ByteArrayInputStream bis = new ByteArrayInputStream(builder.toString().getBytes());
    try (CSVReader<ModeloTeste> instance = new CSVReader<>(new InputStreamReader(bis), new TestMapper())) {
      Optional<ModeloTeste> resultado = instance.leDados().findFirst();
      assertTrue(resultado.isEmpty(), "Stream deve estar vazia.");
    }
  }

  @Test
  public void testLeDados3Linhas() throws CSVMapperException, IOException {
    StringBuilder builder = new StringBuilder();
    builder.append(CABECALHO_CORRETO).append('\n')
            .append(LINHA_SIMPLES1).append('\n')
            .append(LINHA_SIMPLES2).append('\n')
            .append(LINHA_SIMPLES3).append('\n');
    ByteArrayInputStream bis = new ByteArrayInputStream(builder.toString().getBytes());
    try (CSVReader<ModeloTeste> instance = new CSVReader<>(new InputStreamReader(bis), new TestMapper())) {
      Object[] esperado = new Object[]{
        mapaEsperados.get(LINHA_SIMPLES1),
        mapaEsperados.get(LINHA_SIMPLES2),
        mapaEsperados.get(LINHA_SIMPLES3)
      };

      Object[] resultado = instance.leDados().toArray();
      assertArrayEquals(esperado, resultado);
    }
  }

  @Test
  public void testLeDados3LinhasComLinhasEmBranco() throws CSVMapperException, IOException {
    StringBuilder builder = new StringBuilder();
    builder.append(CABECALHO_CORRETO).append('\n')
            .append(LINHA_SIMPLES1).append('\n').append('\n')
            .append(LINHA_SIMPLES2).append('\n')
            .append("       ").append('\n')
            .append(LINHA_SIMPLES3).append('\n');
    ByteArrayInputStream bis = new ByteArrayInputStream(builder.toString().getBytes());
    try (CSVReader<ModeloTeste> instance = new CSVReader<>(new InputStreamReader(bis), new TestMapper())) {
      Object[] esperado = new Object[]{
        mapaEsperados.get(LINHA_SIMPLES1),
        mapaEsperados.get(LINHA_SIMPLES2),
        mapaEsperados.get(LINHA_SIMPLES3)
      };

      Object[] resultado = instance.leDados().toArray();
      assertArrayEquals(esperado, resultado);
    }
 
  }
  
  @Test
  public void testLeDadosDescricaoComVirgula() throws CSVMapperException, IOException {
        StringBuilder builder = new StringBuilder();
    builder.append(CABECALHO_CORRETO).append('\n')
            .append(LINHA_DESCRICAO_COM_VIRGULA_FIM).append('\n')
            .append(LINHA_SIMPLES2).append('\n')
            .append(LINHA_SIMPLES3).append('\n');
    ByteArrayInputStream bis = new ByteArrayInputStream(builder.toString().getBytes());
    try (CSVReader<ModeloTeste> instance = new CSVReader<>(new InputStreamReader(bis), new TestMapper())) {
      Object[] esperado = new Object[]{
        mapaEsperados.get(LINHA_DESCRICAO_COM_VIRGULA_FIM),
        mapaEsperados.get(LINHA_SIMPLES2),
        mapaEsperados.get(LINHA_SIMPLES3)
      };

      Object[] resultado = instance.leDados().toArray();
      assertArrayEquals(esperado, resultado);
    }
  }
  
  private static class ModeloTeste {

    private int codigo;
    private boolean existe;
    private float valor;
    private String descricao;

    @Override
    public int hashCode() {
      int hash = 3;
      hash = 73 * hash + this.codigo;
      hash = 73 * hash + (this.existe ? 1 : 0);
      hash = 73 * hash + Float.floatToIntBits(this.valor);
      hash = 73 * hash + Objects.hashCode(this.descricao);
      return hash;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final ModeloTeste other = (ModeloTeste) obj;
      if (this.codigo != other.codigo) {
        return false;
      }
      if (this.existe != other.existe) {
        return false;
      }
      if (Float.floatToIntBits(this.valor) != Float.floatToIntBits(other.valor)) {
        return false;
      }
      return Objects.equals(this.descricao, other.descricao);
    }

  }

  private static class TestMapper implements CSVMapper<ModeloTeste> {

    private ModeloTeste testModel;

    @Override
    public void setValor(String campo, String valorComoString) throws CSVMapperException {
      switch (campo) {
        case "codigo" ->
          testModel.codigo = converteInt(valorComoString);
        case "existe" ->
          testModel.existe = Boolean.parseBoolean(valorComoString);
        case "valor" ->
          testModel.valor = converteFloat(valorComoString);
        case "descricao" ->
          testModel.descricao = valorComoString;
        default ->
          throw new CSVMapperException(campo + " não existe.");
      }
    }

    @Override
    public ModeloTeste build() throws CSVMapperException {
      return testModel;
    }

    private int converteInt(String valorComoString) throws CSVMapperException {
      try {
        return Integer.parseInt(valorComoString);
      } catch (NumberFormatException ex) {
        throw new CSVMapperException("Valor inteiro esperado. Valor recebido: " + valorComoString);
      }
    }

    private float converteFloat(String valorComoString) throws CSVMapperException {
      try {
        return Float.parseFloat(valorComoString);
      } catch (NumberFormatException ex) {
        throw new CSVMapperException("Valor inteiro esperado. Valor recebido: " + valorComoString);
      }
    }

    @Override
    public void reset() {
      testModel = new ModeloTeste();
    }

  }
}
