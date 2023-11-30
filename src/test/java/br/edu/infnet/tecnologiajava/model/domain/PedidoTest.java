package br.edu.infnet.tecnologiajava.model.domain;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class PedidoTest {
    @TestFactory
    Collection<DynamicTest> testEqualsHashCode() throws ValidadorException {
        List<DynamicTest> testes = new ArrayList<>();
        List<Produto> sobremesas = new ArrayList<>();
        adicionaSobremesa(sobremesas, 5, "sobremesa");
        List<Produto> bebidas = new ArrayList<>();
        adicionaBebida(bebidas, 5, "bebida");
        List<Produto> comidas = new ArrayList<>();
        adicionaSobremesa(comidas, 3, "comida");

        LocalDateTime data = LocalDateTime.of(2023, 2, 13, 12, 30);
        Solicitante solicitante = new Solicitante("062.427.708-90", "João", "joao@yahoo.com.br");
        Pedido pedido = new Pedido(10, "Pedido 1", data, false, solicitante);
        pedido.setProdutos(bebidas);
        int hashCode = pedido.hashCode();

        Pedido codigoDiferente = new Pedido(20, "Pedido 1", data, false, solicitante);
        codigoDiferente.setProdutos(bebidas);
        testes.add(dynamicTest("Equals Codigo diferente", () -> assertNotEquals(pedido, codigoDiferente)));
        testes.add(dynamicTest("HashCode Codigo diferente", () -> assertNotEquals(hashCode, codigoDiferente.hashCode())));

        Pedido descricaoDiferente = new Pedido(20, "Pedido 2", data, false, solicitante);
        descricaoDiferente.setProdutos(bebidas);
        testes.add(dynamicTest("Equals Descricao diferente", () -> assertNotEquals(pedido, descricaoDiferente)));
        testes.add(dynamicTest("HahsCode Descricao diferente", () -> assertNotEquals(hashCode, descricaoDiferente.hashCode())));

        Pedido dataDiferente = new Pedido(20, "Pedido 1", LocalDateTime.now(), false, solicitante);
        dataDiferente.setProdutos(bebidas);
        testes.add(dynamicTest("Equals Data diferente", () -> assertNotEquals(pedido, dataDiferente)));
        testes.add(dynamicTest("HashCode Data diferente", () -> assertNotEquals(hashCode, dataDiferente.hashCode())));

        Pedido webDiferente = new Pedido(10, "Pedido 1", data, true, solicitante);
        webDiferente.setProdutos(bebidas);
        testes.add(dynamicTest("Equals Web diferente", () -> assertNotEquals(pedido, webDiferente)));
        testes.add(dynamicTest("HashsCode Web diferente", () -> assertNotEquals(hashCode, webDiferente.hashCode())));

        Pedido produtosDiferentesMesmaQuantidade = new Pedido(10, "Pedido 1", data, false, solicitante);
        produtosDiferentesMesmaQuantidade.setProdutos(sobremesas);
        testes.add(dynamicTest("Equals Produtos diferentes, mesma quantidade",
                () -> assertNotEquals(pedido, produtosDiferentesMesmaQuantidade)));
        testes.add(dynamicTest("HashCode Produtos diferentes, mesma quantidade",
                () -> assertNotEquals(hashCode, produtosDiferentesMesmaQuantidade.hashCode())));
        Pedido produtosDiferentesQuantidadeDiferente = new Pedido(10, "Pedido 1", data, false, solicitante);
        produtosDiferentesQuantidadeDiferente.setProdutos(comidas);
        testes.add(dynamicTest("Equals Produtos diferentes, quantidade diferente",
                () -> assertNotEquals(pedido, produtosDiferentesQuantidadeDiferente)));
        testes.add(dynamicTest("HashCode Produtos diferentes, quantidade diferente",
                () -> assertNotEquals(hashCode, produtosDiferentesQuantidadeDiferente.hashCode())));

        Pedido solicitanteDiferente = new Pedido(10, "Pedido 1", data, false, Solicitante.getVazio());
        solicitanteDiferente.setProdutos(bebidas);
        testes.add(dynamicTest("Equals Solicitante diferente", () -> assertNotEquals(pedido, solicitanteDiferente)));
        testes.add(dynamicTest("Solicitante Solicitante diferente", () -> assertNotEquals(hashCode, solicitanteDiferente.hashCode())));

        Pedido igual = new Pedido(pedido);
        testes.add(dynamicTest("Equals Mesma Instancia", () -> assertEquals(pedido, pedido)));
        testes.add(dynamicTest("Equals Instancia diferente, objeto igual", () -> assertEquals(pedido, igual)));
        testes.add(dynamicTest("HashCode Instancia diferente, objeto igual", () -> assertEquals(hashCode, igual.hashCode())));
        testes.add(dynamicTest("Equals Classe diferente", () -> assertFalse(pedido.equals(Solicitante.getVazio()))));
        testes.add(dynamicTest("Equals Null", () -> assertFalse(pedido.equals(null))));

        return testes;

    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 10})
    void testSetProdutos(int quantidadeProdutos) throws ValidadorException {
        Pedido pedido = new Pedido("Pedido 1", false, Solicitante.getVazio());
        List<Produto> produtos = new ArrayList<>();
        adicionaSobremesa(produtos, quantidadeProdutos, "sobremesa");
        Object[] produtosIncluidos = produtos.toArray();
        pedido.setProdutos(produtos);
        assertEquals(quantidadeProdutos, pedido.getNumeroProdutos());
        Object[] produtosPedido = pedido.getProdutos().toArray();
        assertArrayEquals(produtosIncluidos, produtosPedido);
    }

    @Test
    void testSetProdutosNull() throws ValidadorException {
        Pedido pedido = new Pedido("Pedido 1", false, Solicitante.getVazio());
        NullPointerException excecao = assertThrows(NullPointerException.class, () -> pedido.setProdutos(null));
        assertEquals("A lista com produtos não pode ser nula.", excecao.getMessage());
    }

    @Test
    void testToStringSolicitanteVazio1() throws ValidadorException {
        LocalDateTime data = LocalDateTime.of(2023, 2, 13, 12, 30);
        Pedido pedido = new Pedido(10, "Pedido 1", data, false, Solicitante.getVazio());
        List<Produto> produtos = new ArrayList<>();
        adicionaSobremesa(produtos, 1, "sobremesa");
        pedido.setProdutos(produtos);
        assertEquals(
                "Pedido: codigo=10, data=13 fev. 2023 12:30, descricao=Pedido 1, web=false, sem solicitante, número produtos=1, valor total=10,00",
                pedido.toString());
    }

    @Test
    void testToStringSolicitanteVazio2() throws ValidadorException {
        LocalDateTime data = LocalDateTime.of(2023, 2, 13, 12, 30);
        Pedido pedido = new Pedido(10, "Pedido 1", data, false, Solicitante.getVazio());
        List<Produto> produtos = new ArrayList<>();
        adicionaBebida(produtos, 3, "bebida");
        pedido.setProdutos(produtos);
        assertEquals(
                "Pedido: codigo=10, data=13 fev. 2023 12:30, descricao=Pedido 1, web=false, sem solicitante, número produtos=3, valor total=30,00",
                pedido.toString());
    }

    @Test
    void testToStringSolicitantePreenchido() throws ValidadorException {
        LocalDateTime data = LocalDateTime.of(2023, 2, 13, 12, 30);
        Solicitante solicitante = new Solicitante("062.427.708-90", "João", "joao@yahoo.com.br");
        Pedido pedido = new Pedido(10, "Pedido 1", data, false, solicitante);
        List<Produto> produtos = new ArrayList<>();
        adicionaSobremesa(produtos, 3, "sobremesa");
        adicionaComida(produtos, 3, "comida");
        pedido.setProdutos(produtos);
        assertEquals(
                "Pedido: codigo=10, data=13 fev. 2023 12:30, descricao=Pedido 1, web=false, solicitante=062.427.708-90, número produtos=6, valor total=60,00",
                pedido.toString());
    }

    @TestFactory
    Collection<DynamicTest> testValidacaoConstrutor() throws ValidadorException {
        List<DynamicTest> testes = new ArrayList<>();
        LocalDateTime data = LocalDateTime.of(2023, 2, 13, 12, 30);
        Solicitante solicitante = new Solicitante("062.427.708-90", "João", "joao@yahoo.com.br");
        testes.add(testValidacao("Codigo negativo", "O código precisa ser maior que zero",
                () -> new Pedido(-20, "Pedido 1", data, false, solicitante)));
        testes.add(testValidacao("Descrição Nula", "A descrição não pode ser nula",
                () -> new Pedido(20, null, data, false, Solicitante.getVazio())));
        testes.add(testValidacao("Descrição em Branco", "A descrição não pode estar em branco",
                () -> new Pedido(20, " ", data, false, solicitante)));
        testes.add(testValidacao("Data nula", "A data não pode ser nula",
                () -> new Pedido(20, "Pedido 1", null, false, solicitante)));
        testes.add(testValidacao("Solicitante nulo", "O solicitante não pode ser nulo",
                () -> new Pedido(20, "Pedido 1", data, false, null)));
        return testes;
    }

    @TestFactory
    Collection<DynamicTest> testGetters() throws ValidadorException {
        List<DynamicTest> testes = new ArrayList<>();
        LocalDateTime data = LocalDateTime.of(2023, 2, 13, 12, 30);
        Solicitante solicitante = new Solicitante("062.427.708-90", "João", "joao@yahoo.com.br");
        Pedido pedido = new Pedido(10, "Pedido 1", data, true, solicitante);
        List<Produto> produtos = new ArrayList<>();
        adicionaSobremesa(produtos, 3, "sobremesa");

        testes.add(dynamicTest("Codigo", () -> assertEquals(10, pedido.getCodigo())));
        testes.add(dynamicTest("Solicitante", () -> assertEquals(solicitante, pedido.getSolicitante())));
        testes.add(dynamicTest("Descricao", () -> assertEquals("Pedido 1", pedido.getDescricao())));
        testes.add(dynamicTest("Data", () -> assertEquals(data, pedido.getData())));
        testes.add(dynamicTest("Web", () -> assertTrue(pedido.isWeb())));

        return testes;
    }

    private DynamicTest testValidacao(String nomeTeste, String mensagemEsperada, Executable criacaoErrada) {
        Executable teste = () -> {
            ValidadorException excecao = assertThrows(ValidadorException.class, criacaoErrada);
            assertEquals(1, excecao.getValidador().getMensagens().size());
            assertEquals(mensagemEsperada, excecao.getValidador().getMensagens().get(0));
        };
        return dynamicTest(nomeTeste, teste);
    }

    private void adicionaSobremesa(List<Produto> produtos, int quantidade, String nome) throws ValidadorException {
        for (int i = 0; i < quantidade; i++) {
            produtos.add(new Sobremesa(nome + " " + i, (i % 2) == 0, "informação " + i, i + 10, 10.f));
        }
    }

    private void adicionaBebida(List<Produto> produtos, int quantidade, String nome) throws ValidadorException {
        for (int i = 0; i < quantidade; i++) {
            produtos.add(new Bebida(nome + " " + i, "marca " + i, 3.0f, (i % 2) == 0, 10.0f));
        }
    }

    private void adicionaComida(List<Produto> produtos, int quantidade, String nome) throws ValidadorException {
        for (int i = 0; i < quantidade; i++) {
            produtos.add(new Comida(nome + " " + i, "marca " + i, i + 10, (i % 2) == 0, 10.0f));
        }
    }
}
