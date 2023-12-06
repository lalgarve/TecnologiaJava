package br.edu.infnet.tecnologiajava.model.mapper;

import br.edu.infnet.tecnologiajava.model.domain.Solicitante;
import br.edu.infnet.tecnologiajava.model.domain.ValidadorException;
import br.edu.infnet.tecnologiajava.services.mapper.MapperException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class SolicitanteMapperTest {

    @Test
    void setValorInvalido() {
        SolicitanteMapper mapper = new SolicitanteMapper();
        mapper.reset();
        MapperException excecao = assertThrows(MapperException.class, () -> mapper.setValor("Nome", "nome"));
        assertEquals("O campo Nome não existe.", excecao.getMessage());
    }

    @Test
    void build() throws ValidadorException {
        SolicitanteMapper mapper = new SolicitanteMapper();
        mapper.reset();
        mapper.setValor("cpf", "775.007.216-09");
        mapper.setValor("nome", "João");
        mapper.setValor("email", "joao@gmail.com");
        Solicitante solicitante = mapper.build();
        Solicitante esperado = new Solicitante("775.007.216-09", "João", "joao@gmail.com");
        assertEquals(esperado, solicitante);
    }
    @ParameterizedTest
    @CsvSource (value={"775.007.216-08;joão;joao@gmail.com",
            "775.007.216-09; ;joao@gmail.com",
            "775.007.216-09;joão;joao@gmail"}, delimiter = ';')
    void buildInvalido(String cpf, String nome, String email) {
        SolicitanteMapper mapper = new SolicitanteMapper();
        mapper.reset();
        mapper.setValor("cpf", cpf);
        mapper.setValor("nome", nome);
        mapper.setValor("email", email);
        MapperException excecao = assertThrows(MapperException.class, () -> mapper.build());
        assertEquals("Informação mapeada inválida.", excecao.getMessage());
        assertEquals(ValidadorException.class, excecao.getCause().getClass());
    }

    @Test
    void buildIncompleto() throws ValidadorException {
        SolicitanteMapper mapper = new SolicitanteMapper();
        mapper.reset();
        mapper.setValor("cpf", "775.007.216-09");
        mapper.setValor("email", "joao@gmail.com");
        MapperException excecao = assertThrows(MapperException.class, () -> mapper.build());
        assertEquals("O campo nome não foi setado.", excecao.getMessage());
    }

    /**
     * Se o código não chamar o método finaliza da classe mãe, a mensagem da exceção
     * fica errada. Teste identificado pela mutação que retira a chamada do método.
     */
    @Test
    void testNecessidadeFinaliza(){
        SolicitanteMapper mapper = new SolicitanteMapper();
        mapper.reset();
        mapper.setValor("cpf", "775.007.216-09");
        mapper.setValor("nome", "João");
        mapper.setValor("email", "joao@gmail.com");
        Solicitante solicitante = mapper.build();
        MapperException excecao = assertThrows(MapperException.class, () -> mapper.setValor("cpf", "929.204.815-50"));
        assertEquals("O método reset não foi chamado.", excecao.getMessage());
    }
}