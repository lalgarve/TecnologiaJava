package br.edu.infnet.tecnologiajava.model.domain;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class PedidoTest {
    @TestFactory
    public Collection<DynamicTest> testEquals() throws ValidadorException {
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

        Pedido codigoDiferente = new Pedido(20, "Pedido 1", data, false, solicitante);
        codigoDiferente.setProdutos(bebidas);
        testes.add(dynamicTest("Codigo diferente", () -> assertFalse(pedido.equals(codigoDiferente))));

        Pedido descricaoDiferente = new Pedido(20, "Pedido 2", data, false, solicitante);
        descricaoDiferente.setProdutos(bebidas);
        testes.add(dynamicTest("Descricao diferente", () -> assertFalse(pedido.equals(descricaoDiferente))));

        Pedido dataDiferente = new Pedido(20, "Pedido 1", LocalDateTime.now(), false, solicitante);
        dataDiferente.setProdutos(bebidas);
        testes.add(dynamicTest("Data diferente", () -> assertFalse(pedido.equals(dataDiferente))));

        Pedido webDiferente = new Pedido(10, "Pedido 1", data, true, solicitante);
        webDiferente.setProdutos(bebidas);
        testes.add(dynamicTest("Web diferente", () -> assertFalse(pedido.equals(webDiferente))));

        Pedido produtosDiferentesMesmaQuantidade = new Pedido(10, "Pedido 1", data, false, solicitante);
        produtosDiferentesMesmaQuantidade.setProdutos(sobremesas);
        testes.add(dynamicTest("Produtos diferentes, mesma quantidade",
                () -> assertFalse(pedido.equals(produtosDiferentesMesmaQuantidade))));

        Pedido produtosDiferentesQuantidadeDiferente = new Pedido(10, "Pedido 1", data, false, solicitante);
        produtosDiferentesQuantidadeDiferente.setProdutos(comidas);
        testes.add(dynamicTest("Produtos diferentes, quantidade diferente",
                () -> assertFalse(pedido.equals(produtosDiferentesQuantidadeDiferente))));

        Pedido solicitanteDiferente = new Pedido(10, "Pedido 1", data, false, Solicitante.getVazio());
        solicitanteDiferente.setProdutos(bebidas);
        testes.add(dynamicTest("Solicitante diferente", () -> assertFalse(pedido.equals(solicitanteDiferente))));

        Pedido igual = new Pedido(pedido);
        testes.add(dynamicTest("Mesma Instancia", () -> assertTrue(pedido.equals(pedido))));
        testes.add(dynamicTest("Instancia diferente, objeto igual", () -> assertTrue(pedido.equals(igual))));
        testes.add(dynamicTest("Classe diferente", () -> assertFalse(pedido.equals(Solicitante.getVazio()))));
        testes.add(dynamicTest("Null", () -> assertFalse(pedido.equals(null))));

        return testes;

    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 3, 10 })
    public void testSetProdutos(int quantidadeProdutos) throws ValidadorException {
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
    public void testSetProdutosNull() throws ValidadorException{
        Pedido pedido = new Pedido("Pedido 1", false, Solicitante.getVazio());
        NullPointerException excecao = assertThrows(NullPointerException.class, () -> pedido.setProdutos(null));
        assertEquals("A lista com produtos não pode ser nula.", excecao.getMessage());
    }

    @Test
    public void testToStringSolicitanteVazio1() throws ValidadorException {
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
    public void testToStringSolicitanteVazio2() throws ValidadorException {
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
    public void testToStringSolicitantePreenchido() throws ValidadorException {
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
    public Collection<DynamicTest> testValidacaoConstrutor() throws ValidadorException {
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
    public Collection<DynamicTest> testGetters() throws ValidadorException{       
        List<DynamicTest> testes = new ArrayList<>();
        LocalDateTime data = LocalDateTime.of(2023, 2, 13, 12, 30);
        Solicitante solicitante = new Solicitante("062.427.708-90", "João", "joao@yahoo.com.br");        
        Pedido pedido = new Pedido(10, "Pedido 1", data, true, solicitante);
        List<Produto> produtos = new ArrayList<>();
        adicionaSobremesa(produtos, 3, "sobremesa");
        
        testes.add(dynamicTest("Codigo", ()->assertEquals(10, pedido.getCodigo())));
        testes.add(dynamicTest("Solicitante", ()->assertEquals(solicitante, pedido.getSolicitante())));
        testes.add(dynamicTest("Descricao", ()->assertEquals("Pedido 1", pedido.getDescricao())));
        testes.add(dynamicTest("Data", ()->assertEquals(data, pedido.getData())));
        testes.add(dynamicTest("Web", ()->assertEquals(true, pedido.isWeb())));

        return testes;
    }

    private DynamicTest testValidacao(String nomeTeste, String mensagemEsperada, Executable criacaoErrada){
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
