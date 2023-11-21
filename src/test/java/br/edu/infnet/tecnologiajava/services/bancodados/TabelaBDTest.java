package br.edu.infnet.tecnologiajava.services.bancodados;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testa se as classe implementaram a interface
 * {@link br.edu.infnet.tecnologiajava.services.bancodados.TabelaBD TabelaBD}
 * corretamente. Os testes são parametrizados para incluir todas as classes.
 *
 * @author leila
 */
public class TabelaBDTest {

    private static List<ValorSemDependente> valoresTeste;
    private static List<ValorSemDependente> valoresTesteMesmaChave;

    public TabelaBDTest() {
    }

    @BeforeAll
    public static void inicializaValores() {

        valoresTeste = new ArrayList<>();
        valoresTesteMesmaChave = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            ValorSemDependente valor;
            valor = new ValorSemDependente(i);
            valor.setDescricao("valor" + i);
            valoresTeste.add(valor);

            valor = new ValorSemDependente(i);
            valor.setDescricao("valorMesmaChave" + i);
            valoresTesteMesmaChave.add(valor);

        }

    }

    @ParameterizedTest
    @EnumSource(FabricaTabelaBD.class)
    void testAdicionaMesmaChave(FabricaTabelaBD fabrica) throws Exception {
        ValorSemDependente valor = valoresTeste.get(0);
        ValorSemDependente valorMesmaChave = valoresTesteMesmaChave.get(0);
        TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
        instance.adiciona(valor);
        BancoDadosException excecao = assertThrows(BancoDadosException.class, () -> instance.adiciona(valorMesmaChave));
        assertEquals("A chave 0 já existe na tabela minhatabela.", excecao.getMessage());
    }

    @ParameterizedTest
    @EnumSource(FabricaTabelaBD.class)
    void testAdiciona(FabricaTabelaBD fabrica) throws Exception {
        ValorSemDependente valor = valoresTeste.get(0);
        TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
        instance.adiciona(valor);
        Optional<ValorSemDependente> resultado = instance.consultaPorId(valor.getChave());
        assertTrue(resultado.isPresent());
        assertEquals(valor, resultado.get());
        assertNotSame(valor, resultado.get(), "Um clone deveria ter sido retornado.");
    }

    @ParameterizedTest
    @EnumSource(FabricaTabelaBD.class)
    void testAdicionaChaveNula(FabricaTabelaBD fabrica) throws Exception {
        ValorSemDependente valor = new ValorSemDependente();
        TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
        NullPointerException excecao = assertThrows(NullPointerException.class, () -> instance.adiciona(valor));
        assertEquals("A chave não pode ser nula.", excecao.getMessage());
    }

    @ParameterizedTest
    @EnumSource(FabricaTabelaBD.class)
    void testAdicionaValorNulo(FabricaTabelaBD fabrica) throws Exception {
        ValorSemDependente valor = null;
        TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
        NullPointerException excecao = assertThrows(NullPointerException.class, () -> instance.adiciona(valor));
        assertEquals("O valor não pode ser nulo.", excecao.getMessage());
    }

    @ParameterizedTest
    @EnumSource(FabricaTabelaBD.class)
    void testAdicionaEEditaValorAdicionado(FabricaTabelaBD fabrica) throws Exception {
        ValorSemDependente valor = new ValorSemDependente(10);
        valor.setDescricao("valor 1");
        TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
        instance.adiciona(valor);
        valor.setDescricao("Nova descricao");
        ValorSemDependente valorTabela = instance.consultaPorId(10).get();
        assertNotEquals(valorTabela, valor);
    }

    @ParameterizedTest
    @EnumSource(FabricaTabelaBD.class)
    void testAdicionaValorNaoPodeSerAdcionado(FabricaTabelaBD fabrica) throws Exception {
        ValorSemDependente valor = new ValorSemDependente(12);
        valor.setPodeSerGravadoNoBanco(false);
        TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
        BancoDadosException excecao = assertThrows(BancoDadosException.class, () -> instance.adiciona(valor));
        assertEquals("O valor não pode ser adicionado.", excecao.getMessage());
    }

    @ParameterizedTest
    @EnumSource(FabricaTabelaBD.class)
    void testRemove(FabricaTabelaBD fabrica) throws Exception {
        ValorSemDependente valor = valoresTeste.get(0);
        TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
        instance.adiciona(valor);
        instance.removePorId(valor.getChave());
        Optional<ValorSemDependente> resultado = instance.consultaPorId(valor.getChave());
        assertTrue(resultado.isEmpty());
    }

    @ParameterizedTest
    @EnumSource(FabricaTabelaBD.class)
    void testRemoveValorNaoExiste(FabricaTabelaBD fabrica) throws Exception {
        TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
        BancoDadosException excecao = assertThrows(BancoDadosException.class, () -> instance.removePorId(0));
        assertEquals("A chave 0 não existe na tabela minhatabela.", excecao.getMessage());
    }

    @ParameterizedTest
    @EnumSource(FabricaTabelaBD.class)
    void testRemoveChaveNula(FabricaTabelaBD fabrica) throws Exception {
        TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
        NullPointerException excecao = assertThrows(NullPointerException.class, () -> instance.removePorId(null));
        assertEquals("A chave não pode ser nula.", excecao.getMessage());
    }

    @ParameterizedTest
    @EnumSource(FabricaTabelaBD.class)
    void testAlteraEEditaValorAdicionado(FabricaTabelaBD fabrica) throws Exception {
        ValorSemDependente valor = valoresTeste.get(0);
        TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
        instance.adiciona(valor);
        ValorSemDependente valorAlterado = valoresTesteMesmaChave.get(0);
        instance.altera(valorAlterado);

        Optional<ValorSemDependente> resultado = instance.consultaPorId(valor.getChave());
        assertTrue(resultado.isPresent());
        assertEquals(valorAlterado, resultado.get());
        assertNotSame(valor, resultado.get(), "Um clone deveria ter sido retornado/");
    }

    @ParameterizedTest
    @EnumSource(FabricaTabelaBD.class)
    void testAltera(FabricaTabelaBD fabrica) throws Exception {
        ValorSemDependente valor = valoresTeste.get(0);
        TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
        instance.adiciona(valor);
        ValorSemDependente valorAlterado = valoresTesteMesmaChave.get(0).getInstanciaCopiaSegura();
        instance.altera(valorAlterado);
        valorAlterado.setDescricao("Outra descricao");
        Optional<ValorSemDependente> resultado = instance.consultaPorId(valor.getChave());
        assertTrue(resultado.isPresent());
        assertNotEquals(valorAlterado, resultado.get());
    }

    @ParameterizedTest
    @EnumSource(FabricaTabelaBD.class)
    void testAlteraChaveInexistente(FabricaTabelaBD fabrica) throws Exception {
        ValorSemDependente valor = valoresTeste.get(0);
        TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
        BancoDadosException excecao = assertThrows(BancoDadosException.class, () -> instance.altera(valor));
        assertEquals("Não exite chave 0 para ser alterada.", excecao.getMessage());
    }

    @ParameterizedTest
    @EnumSource(FabricaTabelaBD.class)
    void testAlteraValorNulo(FabricaTabelaBD fabrica) throws Exception {
        ValorSemDependente valor = null;
        TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
        NullPointerException excecao = assertThrows(NullPointerException.class, () -> instance.altera(valor));
        assertEquals("O valor não pode ser nulo.", excecao.getMessage());
    }


    @ParameterizedTest
    @EnumSource(FabricaTabelaBD.class)
    void testAlteraChaveNula(FabricaTabelaBD fabrica) throws Exception {
        ValorSemDependente valor = new ValorSemDependente();
        TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
        NullPointerException excecao = assertThrows(NullPointerException.class, () -> instance.altera(valor));
        assertEquals("A chave não pode ser nula.", excecao.getMessage());
    }

    @ParameterizedTest
    @EnumSource(FabricaTabelaBD.class)
    void testAlteraComValorNaoPodeSerAdicionado(FabricaTabelaBD fabrica) throws Exception {
        ValorSemDependente valor = valoresTeste.get(0);
        TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
        instance.adiciona(valor);
        ValorSemDependente valorAlterado = valoresTesteMesmaChave.get(0).getInstanciaCopiaSegura();
        valorAlterado.setDescricao("Outra descricao");
        valorAlterado.setPodeSerGravadoNoBanco(false);
        BancoDadosException excecao = assertThrows(BancoDadosException.class, () -> instance.altera(valorAlterado));
        assertEquals("Novo valor não pode ser posto no banco de dados.", excecao.getMessage());
    }

    @ParameterizedTest
    @EnumSource(FabricaTabelaBD.class)
    void testConsultaPorIdChaveNula(FabricaTabelaBD fabrica) throws Exception {
        ValorSemDependente valor = valoresTeste.get(0);
        TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
        instance.adiciona(valor);
        NullPointerException excecao = assertThrows(NullPointerException.class, () -> instance.consultaPorId(null));
        assertEquals("A chave não pode ser nula.", excecao.getMessage());
    }


    @ParameterizedTest
    @EnumSource(FabricaTabelaBD.class)
    void testConsultaPorIdChaveNaoExiste(FabricaTabelaBD fabrica) throws Exception {
        ValorSemDependente valor = valoresTeste.get(0);
        TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
        instance.adiciona(valor);
        Optional<ValorSemDependente> resultado = instance.consultaPorId(valor.getChave() + 1);
        assertTrue(resultado.isEmpty());
    }

    @ParameterizedTest
    @EnumSource(FabricaTabelaBD.class)
    void testGetValores(FabricaTabelaBD fabrica) throws Exception {
        TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");

        valoresTeste.forEach((valor) -> {
            try {
                instance.adiciona(valor);
            } catch (BancoDadosException ex) {
                fail(ex);
            }
        });

        List<ValorSemDependente> valores = instance.getValores();
        assertEquals(valoresTeste.size(), valores.size());

        Optional<ValorSemDependente> naoExiste = valoresTeste.stream().filter((valorTeste) -> !valores.contains(valorTeste)).findFirst();
        assertTrue(naoExiste.isEmpty(), "Um valor não foi encontrado: " + naoExiste.orElse(null));

        Optional<ValorSemDependente> naoEClone = valoresTeste.stream().filter((valorTeste) -> {
            int index = valores.indexOf(valorTeste);
            return valores.get(index) == valorTeste;
        }).findFirst();

        assertTrue(naoEClone.isEmpty(), "Um valor não é clone encontrado: " + naoExiste.orElse(null));
    }

    @ParameterizedTest
    @EnumSource(FabricaTabelaBD.class)
    void testGetValoresTabelaVazia(FabricaTabelaBD fabrica) throws Exception {
        TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
        List<ValorSemDependente> valores = instance.getValores();
        assertEquals(0, valores.size());
    }

    @ParameterizedTest
    @EnumSource(FabricaTabelaBD.class)
    void testGetValoresComFiltro(FabricaTabelaBD fabrica) throws Exception {
        TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
        valoresTeste.forEach((valor) -> {
            try {
                instance.adiciona(valor);
            } catch (BancoDadosException ex) {
                fail(ex);
            }
        });
        Predicate<ValorSemDependente> filtraPares = (valor) -> valor.getId() % 2 == 0;

        List<ValorSemDependente> valores = instance.getValores(filtraPares);
        assertEquals(3, valores.size());
        long count = valores.stream().filter(filtraPares.negate()).count();
        assertEquals(0, count);

    }

    @ParameterizedTest
    @EnumSource(FabricaTabelaBD.class)
    void testGetValoresComFiltroNulo(FabricaTabelaBD fabrica) throws Exception {
        TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
        valoresTeste.forEach((valor) -> {
            try {
                instance.adiciona(valor);
            } catch (BancoDadosException ex) {
                fail(ex);
            }
        });

        NullPointerException excecao = assertThrows(NullPointerException.class,
                () -> instance.getValores(null));
        assertEquals("O filtro não pode ser nulo.", excecao.getMessage());
    }

    @ParameterizedTest
    @EnumSource(FabricaTabelaBD.class)
    void testGetNome(FabricaTabelaBD fabrica) throws Exception {
        TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
        assertEquals("minhatabela", instance.getNome());
    }


    @ParameterizedTest
    @EnumSource(FabricaTabelaBD.class)
    void testNomeNull(FabricaTabelaBD fabrica) throws Exception {
        NullPointerException excecao = assertThrows(NullPointerException.class,
                () -> fabrica.constroiTabela(null));
        assertEquals("O nome não pode ser nulo.", excecao.getMessage());
    }


    @ParameterizedTest
    @EnumSource(FabricaTabelaBD.class)
    void testNomeBlack(FabricaTabelaBD fabrica) throws Exception {
        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class,
                () -> fabrica.constroiTabela("  "));
        assertEquals("O nome não pode estar vazio ou em branco.", excecao.getMessage());
    }
}
