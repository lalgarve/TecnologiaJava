package br.edu.infnet.tecnologiajava.services.bancodados;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes da tabela dependente que não testam a interface
 * {@link br.edu.infnet.tecnologiajava.services.bancodados.TabelaBD tabelaBD}
 * sem usar a {@link br.edu.infnet.tecnologiajava.services.bancodados.TabelaPrincipal tabelaPrincipal}
 *
 * @author leila
 */
class TabelaDependenteTest {

    private TabelaDependente<Integer, ValorSemDependente> tabelaDependente;
    private TabelaImpl<Integer, ValorComDependente> tabelaRelacao1;
    private TabelaImpl<Integer, ValorComDependente> tabelaRelacao2;

    TabelaDependenteTest() {
    }

    @BeforeEach
    void criaTabelaERelacoes() throws BancoDadosException {
        tabelaDependente = new TabelaDependente<>("tabeladependente");
        tabelaRelacao1 = new TabelaImpl<>("tabelarelacao1");
        tabelaRelacao2 = new TabelaImpl<>("tabelarelacao2");
        for (int i = 0; i < 5; i++) {
            ValorSemDependente valor;
            valor = new ValorSemDependente(i);
            valor.setDescricao("valor" + i);
            tabelaDependente.adiciona(valor);
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 4})
    void testAdicionaUso(int chave) throws Exception {
        tabelaDependente.adicionaUso(chave, tabelaRelacao1);
        excecaoRemovePorId(chave);
        Optional<ValorSemDependente> valor = tabelaDependente.consultaPorId(chave);
        assertTrue(valor.isPresent(), "Valor não deveria ter sido removido");
    }

    @Test
    void testAdicionaUsoChaveNula() throws Exception {
        NullPointerException excecao = assertThrows(NullPointerException.class,
                () -> tabelaDependente.adicionaUso(null, tabelaRelacao1));
        assertEquals("A chave não pode ser nula.", excecao.getMessage());
    }


    @ParameterizedTest
    @ValueSource(ints = {20, 10, 14})
    void testAdicionaUsoChaveNaoExiste(int chave) throws Exception {
        BancoDadosException excecao = assertThrows(BancoDadosException.class,
                () -> tabelaDependente.adicionaUso(chave, tabelaRelacao1));
        assertEquals("A chave não existe: " + chave, excecao.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 4})
    void testRemoveUso(int chave) throws Exception {
        tabelaDependente.adicionaUso(chave, tabelaRelacao1);
        tabelaDependente.removeUso(chave, tabelaRelacao1);
        tabelaDependente.removePorId(chave);
        Optional<ValorSemDependente> valor = tabelaDependente.consultaPorId(chave);
        assertTrue(valor.isEmpty(), "Valor deveria ter sido removido");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 4})
    void testRemoveUsoDuasTabelas(int chave) throws Exception {
        tabelaDependente.adicionaUso(chave, tabelaRelacao1);
        tabelaDependente.adicionaUso(chave, tabelaRelacao2);
        tabelaDependente.removeUso(chave, tabelaRelacao1);
        excecaoRemovePorId(chave);
        Optional<ValorSemDependente> valor = tabelaDependente.consultaPorId(chave);
        assertTrue(valor.isPresent(), "Valor não deveria ter sido removido");
    }


    @ParameterizedTest
    @ValueSource(ints = {0, 1, 4})
    void testRemoveUsoSemAdicionar(int chave) throws Exception {
        BancoDadosException excecao = assertThrows(BancoDadosException.class,
                () -> tabelaDependente.removeUso(chave, tabelaRelacao1));
        assertEquals(String.format(
                "Não há uso para a chave %d na tabela %s.", chave, tabelaRelacao1.getNome()
        ), excecao.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 4})
    void testRemoveUsoVariasChamadas(int chave) throws Exception {
        tabelaDependente.adicionaUso(chave, tabelaRelacao1);
        tabelaDependente.adicionaUso(chave, tabelaRelacao1);
        tabelaDependente.adicionaUso(chave, tabelaRelacao1);
        tabelaDependente.removeUso(chave, tabelaRelacao1);
        tabelaDependente.removeUso(chave, tabelaRelacao1);
        tabelaDependente.removeUso(chave, tabelaRelacao1);
        tabelaDependente.removePorId(chave);
        Optional<ValorSemDependente> valor = tabelaDependente.consultaPorId(chave);
        assertTrue(valor.isEmpty(), "Valor deveria ter sido removido");
    }

    @Test
    void testRemoveUsoChaveNula() throws Exception {
        NullPointerException excecao = assertThrows(NullPointerException.class,
                () -> tabelaDependente.removeUso(null, tabelaRelacao1));
        assertEquals("A chave não pode ser nula.", excecao.getMessage());
    }

    private void excecaoRemovePorId(int chave) {
        BancoDadosException excecao = assertThrows(BancoDadosException.class,
                () -> tabelaDependente.removePorId(chave));
        assertEquals("O valor está sendo usado. Chave: " + chave, excecao.getMessage());
    }
}
