package br.edu.infnet.tecnologiajava.model.mapper;

import br.edu.infnet.tecnologiajava.ValidadorException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
    void testHashCode() throws ValidadorException {
        ProdutoDesconhecido produtoDesconhecido = new ProdutoDesconhecido(2);
        assertEquals(2, produtoDesconhecido.hashCode());
    }

}