package br.edu.infnet.tecnologiajava.services.csv;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class CSVReaderTest {

    private static final String CABECALHO_CORRETO = "codigo,existe,valor,descricao1";
    private static final String CABECALHO_INCORRETO1 = "codigo1,existe,valor,descricao1";
    private static final String LINHA_SIMPLES1 = "1,true,10.3,descricao1";
    private static final String LINHA_SIMPLES2 = "2,false,10.5,descricao2";
    private static final String LINHA_SIMPLES3 = "3,true,10.6,descricao3";
    private static final String LINHA_DESCRICAO_COM_VIRGULA_FIM = "1,true,10.6,\"descricao1, virgula\"";
    private static final String LINHA_VALORES_A_MENOS = "1,true,10.6";
    private static final String LINHA_VALORES_A_MAIS = "1,true,10.6,descricao1, virgula";
    private static final String CABECALHO_CORRETO_3_DESCRICOES = "descricao1,codigo,descricao2,existe,valor,descricao3";
    private static final String LINHA_3_DESCRICOES_1 = "descricao1-1,1,descricao2-1,true,10.6,descricao3-1";
    private static final String LINHA_3_DESCRICOES_2 = "\"descricao1-2\",2,\"descricao2-2\",true,10.6,\"descricao3-2\"";
    private static final String LINHA_3_DESCRICOES_3 = "\"descricao1-3, QQQQ\",3,descricao2-3,true,10.6,descricao3-3";
    private static final String LINHA_3_DESCRICOES_4 = "\",,,,descricao1-4\",4,\"descricao2-4,,,,\",true,10.6,descricao3-4";
    private static final String LINHA_3_DESCRICOES_5 = "descricao1-5,5,\"descricao2-5, QQQQ\",true,10.6,descricao3-5";
    private static final String LINHA_3_DESCRICOES_6 = "descricao1-6,6,descricao2-6,true,10.6,\"descricao3-6, QQQQ\"";
    private static final String LINHA_3_DESCRICOES_7 = "\"descricao1-7,QQQQ\",7,\"descricao2-7, \",true,10.6,\",descricao3-7, QQQQ\"";

    private static final Map<String, ModeloTeste> mapaEsperados = new HashMap<>();

    public CSVReaderTest() {
    }

    @BeforeAll
    public static void preencheObjetosEsperados() {
        ModeloTeste modeloTeste;

        modeloTeste = new ModeloTeste();
        modeloTeste.codigo = 1;
        modeloTeste.descricao1 = "descricao1";
        modeloTeste.valor = 10.3f;
        modeloTeste.existe = true;
        mapaEsperados.put(LINHA_SIMPLES1, modeloTeste);

        modeloTeste = new ModeloTeste();
        modeloTeste.codigo = 2;
        modeloTeste.descricao1 = "descricao2";
        modeloTeste.valor = 10.5f;
        modeloTeste.existe = false;
        mapaEsperados.put(LINHA_SIMPLES2, modeloTeste);

        modeloTeste = new ModeloTeste();
        modeloTeste.codigo = 3;
        modeloTeste.descricao1 = "descricao3";
        modeloTeste.valor = 10.6f;
        modeloTeste.existe = true;
        mapaEsperados.put(LINHA_SIMPLES3, modeloTeste);

        modeloTeste = new ModeloTeste();
        modeloTeste.codigo = 1;
        modeloTeste.descricao1 = "descricao1, virgula";
        modeloTeste.valor = 10.6f;
        modeloTeste.existe = true;
        mapaEsperados.put(LINHA_DESCRICAO_COM_VIRGULA_FIM, modeloTeste);

        modeloTeste = new ModeloTeste();
        modeloTeste.codigo = 1;
        modeloTeste.descricao1 = "descricao1-1";
        modeloTeste.descricao2 = "descricao2-1";
        modeloTeste.descricao3 = "descricao3-1";
        modeloTeste.valor = 10.6f;
        modeloTeste.existe = true;
        mapaEsperados.put(LINHA_3_DESCRICOES_1, modeloTeste);

        modeloTeste = new ModeloTeste();
        modeloTeste.codigo = 2;
        modeloTeste.descricao1 = "descricao1-2";
        modeloTeste.descricao2 = "descricao2-2";
        modeloTeste.descricao3 = "descricao3-2";
        modeloTeste.valor = 10.6f;
        modeloTeste.existe = true;
        mapaEsperados.put(LINHA_3_DESCRICOES_2, modeloTeste);


        modeloTeste = new ModeloTeste();
        modeloTeste.codigo = 3;
        modeloTeste.descricao1 = "descricao1-3, QQQQ";
        modeloTeste.descricao2 = "descricao2-3";
        modeloTeste.descricao3 = "descricao3-3";
        modeloTeste.valor = 10.6f;
        modeloTeste.existe = true;
        mapaEsperados.put(LINHA_3_DESCRICOES_3, modeloTeste);

        modeloTeste = new ModeloTeste();
        modeloTeste.codigo = 4;
        modeloTeste.descricao1 = ",,,,descricao1-4";
        modeloTeste.descricao2 = "descricao2-4,,,,";
        modeloTeste.descricao3 = "descricao3-4";
        modeloTeste.valor = 10.6f;
        modeloTeste.existe = true;
        mapaEsperados.put(LINHA_3_DESCRICOES_4, modeloTeste);

        modeloTeste = new ModeloTeste();
        modeloTeste.codigo = 5;
        modeloTeste.descricao1 = "descricao1-5";
        modeloTeste.descricao2 = "descricao2-5, QQQQ";
        modeloTeste.descricao3 = "descricao3-5";
        modeloTeste.valor = 10.6f;
        modeloTeste.existe = true;
        mapaEsperados.put(LINHA_3_DESCRICOES_5, modeloTeste);

        modeloTeste = new ModeloTeste();
        modeloTeste.codigo = 6;
        modeloTeste.descricao1 = "descricao1-6";
        modeloTeste.descricao2 = "descricao2-6";
        modeloTeste.descricao3 = "descricao3-6, QQQQ";
        modeloTeste.valor = 10.6f;
        modeloTeste.existe = true;
        mapaEsperados.put(LINHA_3_DESCRICOES_6, modeloTeste);


        modeloTeste = new ModeloTeste();
        modeloTeste.codigo = 7;
        modeloTeste.descricao1 = "descricao1-7,QQQQ";
        modeloTeste.descricao2 = "descricao2-7, ";
        modeloTeste.descricao3 = ",descricao3-7, QQQQ";
        modeloTeste.valor = 10.6f;
        modeloTeste.existe = true;
        mapaEsperados.put(LINHA_3_DESCRICOES_7, modeloTeste);
    }

    @Test
    void testLeDados() throws CSVMapperException, IOException {
        String builder = CABECALHO_CORRETO + '\n' +
                LINHA_SIMPLES1 + '\n';
        ByteArrayInputStream bis = new ByteArrayInputStream(builder.getBytes());
        try (CSVReader<ModeloTeste> instance = new CSVReader<>(new InputStreamReader(bis), new TestMapper())) {
            ModeloTeste esperado = mapaEsperados.get(LINHA_SIMPLES1);
            ModeloTeste resultado = instance.leDados().findFirst().get();
            assertEquals(esperado, resultado);
        }
    }

    @Test
    void testLeDadosCabecalhoIncorreto1() throws CSVMapperException, IOException {
        String builder = CABECALHO_INCORRETO1 + '\n' +
                LINHA_SIMPLES1 + '\n';
        ByteArrayInputStream bis = new ByteArrayInputStream(builder.getBytes());
        try (CSVReader<ModeloTeste> instance = new CSVReader<>(new InputStreamReader(bis), new TestMapper())) {
            Stream<ModeloTeste> stream = instance.leDados();
            Optional<ModeloTeste> resultado = stream.findFirst();
            fail("Excecao esperada");
        } catch (CSVMapperException ex) {
            assertEquals("codigo1 não existe.", ex.getMessage());
        }
    }

    @Test
    void testLeDadosApenasCabecalho() throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream((CABECALHO_CORRETO + '\n').getBytes());
        try (CSVReader<ModeloTeste> instance = new CSVReader<>(new InputStreamReader(bis), new TestMapper())) {
            Optional<ModeloTeste> resultado = instance.leDados().findFirst();
            assertTrue(resultado.isEmpty(), "Stream deve estar vazia.");
        }
    }

    @Test
    void testLeDadosVazio() throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream("\n".getBytes());
        try (CSVReader<ModeloTeste> instance = new CSVReader<>(new InputStreamReader(bis), new TestMapper())) {
            Optional<ModeloTeste> resultado = instance.leDados().findFirst();
            assertTrue(resultado.isEmpty(), "Stream deve estar vazia.");
        }
    }

    @Test
    void testLeDados3Linhas() throws CSVMapperException, IOException {
        String builder = CABECALHO_CORRETO + '\n' +
                LINHA_SIMPLES1 + '\n' +
                LINHA_SIMPLES2 + '\n' +
                LINHA_SIMPLES3 + '\n';
        ByteArrayInputStream bis = new ByteArrayInputStream(builder.getBytes());
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
    void testLeDados3LinhasComLinhasEmBranco() throws CSVMapperException, IOException {
        String builder = CABECALHO_CORRETO + '\n' +
                LINHA_SIMPLES1 + '\n' + '\n' +
                LINHA_SIMPLES2 + '\n' +
                "       " + '\n' +
                LINHA_SIMPLES3 + '\n';
        ByteArrayInputStream bis = new ByteArrayInputStream(builder.getBytes());
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
    void testLeDadosDescricaoComVirgula() throws CSVMapperException, IOException {
        String builder = CABECALHO_CORRETO + '\n' +
                LINHA_DESCRICAO_COM_VIRGULA_FIM + '\n' +
                LINHA_SIMPLES2 + '\n' +
                LINHA_SIMPLES3 + '\n';
        ByteArrayInputStream bis = new ByteArrayInputStream(builder.getBytes());
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

    @Test
    void testLeDados3Descricoes() throws CSVMapperException, IOException {
        String builder = CABECALHO_CORRETO_3_DESCRICOES + '\n' +
                LINHA_3_DESCRICOES_1 + '\n' +
                LINHA_3_DESCRICOES_2 + '\n' +
                LINHA_3_DESCRICOES_3 + '\n' +
                LINHA_3_DESCRICOES_4 + '\n' +
                LINHA_3_DESCRICOES_5 + '\n' +
                LINHA_3_DESCRICOES_6 + '\n' +
                LINHA_3_DESCRICOES_7 + '\n';
        ByteArrayInputStream bis = new ByteArrayInputStream(builder.getBytes());
        try (CSVReader<ModeloTeste> instance = new CSVReader<>(new InputStreamReader(bis), new TestMapper())) {
            Object[] esperado = new Object[]{
                    mapaEsperados.get(LINHA_3_DESCRICOES_1),
                    mapaEsperados.get(LINHA_3_DESCRICOES_2),
                    mapaEsperados.get(LINHA_3_DESCRICOES_3),
                    mapaEsperados.get(LINHA_3_DESCRICOES_4),
                    mapaEsperados.get(LINHA_3_DESCRICOES_5),
                    mapaEsperados.get(LINHA_3_DESCRICOES_6),
                    mapaEsperados.get(LINHA_3_DESCRICOES_7)
            };

            Object[] resultado = instance.leDados().toArray();
            assertArrayEquals(esperado, resultado);
        }
    }


    @Test
    void testLeDadosValoresAMais() throws CSVMapperException, IOException {
        String builder = CABECALHO_CORRETO + '\n' +
                LINHA_VALORES_A_MAIS + '\n';
        ByteArrayInputStream bis = new ByteArrayInputStream(builder.getBytes());
        try (CSVReader<ModeloTeste> instance = new CSVReader<>(new InputStreamReader(bis), new TestMapper())) {
            Stream<ModeloTeste> stream = instance.leDados();
            Optional<ModeloTeste> resultado = stream.findFirst();
            fail("Excecao esperada");
        } catch (CSVMapperException ex) {
            assertEquals("Esperados 4 valores, foram encontrados 5.", ex.getMessage());
        }
    }

    @Test
    void testLeDadosValoresAMenos() throws CSVMapperException, IOException {
        String builder = CABECALHO_CORRETO + '\n' +
                LINHA_VALORES_A_MENOS + '\n';
        ByteArrayInputStream bis = new ByteArrayInputStream(builder.getBytes());
        try (CSVReader<ModeloTeste> instance = new CSVReader<>(new InputStreamReader(bis), new TestMapper())) {
            Stream<ModeloTeste> stream = instance.leDados();
            Optional<ModeloTeste> resultado = stream.findFirst();
            fail("Excecao esperada");
        } catch (CSVMapperException ex) {
            assertEquals("Esperados 4 valores, foram encontrados 3.", ex.getMessage());
        }
    }

    private static class ModeloTeste {

        private int codigo;
        private boolean existe;
        private float valor;
        private String descricao1;
        private String descricao2;
        private String descricao3;

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 73 * hash + this.codigo;
            hash = 73 * hash + (this.existe ? 1 : 0);
            hash = 73 * hash + Float.floatToIntBits(this.valor);
            hash = 73 * hash + Objects.hashCode(this.descricao1);
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
            return Objects.equals(this.descricao1, other.descricao1);
        }

        @Override
        public String toString() {
            return "ModeloTeste{" + "codigo=" + codigo + ", existe=" + existe + ", valor=" + valor + ", descricao1=" + descricao1 + ", descricao2=" + descricao2 + ", descricao3=" + descricao3 + '}';
        }


    }

    private static class TestMapper implements CSVMapper<ModeloTeste> {

        private ModeloTeste testModel;

        @Override
        public void setValor(String campo, String valorComoString) throws CSVMapperException {
            switch (campo) {
                case "codigo" -> testModel.codigo = converteInt(valorComoString);
                case "existe" -> testModel.existe = Boolean.parseBoolean(valorComoString);
                case "valor" -> testModel.valor = converteFloat(valorComoString);
                case "descricao1" -> testModel.descricao1 = valorComoString;
                case "descricao2" -> testModel.descricao2 = valorComoString;
                case "descricao3" -> testModel.descricao3 = valorComoString;
                default -> throw new CSVMapperException(campo + " não existe.");
            }
        }

        @Override
        public ModeloTeste build() throws CSVMapperException {
            return testModel;
        }

        @Override
        public void reset() {
            testModel = new ModeloTeste();
        }

    }
}
