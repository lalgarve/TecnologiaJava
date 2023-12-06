package br.edu.infnet.tecnologiajava.model.mapper;

import br.edu.infnet.tecnologiajava.model.domain.ValidadorException;
import br.edu.infnet.tecnologiajava.model.mapper.ProdutoDesconhecido;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class ProdutoDesconhecidoTest {

    @Test
    void podeSerGravadoNoBanco() throws ValidadorException {
        ProdutoDesconhecido produtoDesconhecido = new ProdutoDesconhecido(2);
        assertFalse(produtoDesconhecido.podeSerGravadoNoBanco());
    }

    @Test
    void getDetalhe() throws ValidadorException {
        ProdutoDesconhecido produtoDesconhecido = new ProdutoDesconhecido(2);
        assertEquals("Desconhecido", produtoDesconhecido.getDetalhe());
    }

    @Test
    void testHashCode()  throws ValidadorException {
        ProdutoDesconhecido produtoDesconhecido = new ProdutoDesconhecido(2);
        assertEquals(2, produtoDesconhecido.hashCode());
    }

    @TestFactory
    Collection<DynamicTest> testEquals() throws ValidadorException {
        ProdutoDesconhecido produtoDesconhecido = new ProdutoDesconhecido(2);
        ProdutoDesconhecido produtoIgual = new ProdutoDesconhecido(2);
        ProdutoDesconhecido produtoDiferente = new ProdutoDesconhecido(3);

        return Arrays.asList(
                dynamicTest("Código diferente", () -> assertNotEquals(produtoDesconhecido, produtoDiferente)),
                dynamicTest("Igual, mesma instancia", () -> assertTrue(produtoDesconhecido.equals(produtoDesconhecido))),
                dynamicTest("Igual, instancia diferente", () -> assertEquals(produtoDesconhecido, produtoIgual)),
                dynamicTest("Objeto Nulo", () -> assertFalse(produtoDesconhecido.equals(null))),
                dynamicTest("Classe diferente", () -> assertFalse(produtoDesconhecido.equals(Boolean.FALSE)))

        );
    }
}