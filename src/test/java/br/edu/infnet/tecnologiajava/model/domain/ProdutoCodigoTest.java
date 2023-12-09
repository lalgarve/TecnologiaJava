package br.edu.infnet.tecnologiajava.model.domain;

import br.edu.infnet.tecnologiajava.ValidadorException;
import br.edu.infnet.tecnologiajava.model.domain.ProdutoCodigo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ProdutoCodigoTest {

    @Test
    void podeSerGravadoNoBanco() throws ValidadorException {
        ProdutoCodigo produtoDesconhecido = new ProdutoCodigo(2);
        assertFalse(produtoDesconhecido.podeSerGravadoNoBanco());
    }

    @Test
    void getDetalhe() throws ValidadorException {
        ProdutoCodigo produtoCodigo = new ProdutoCodigo(2);
        assertEquals("Desconhecido", produtoCodigo.getDetalhe());
    }

    @Test
    void testHashCode() throws ValidadorException {
        ProdutoCodigo produtoCodigo = new ProdutoCodigo(2);
        assertEquals(2, produtoCodigo.hashCode());
    }

}