package br.edu.infnet.tecnologiajava.services.relatorios;

import br.edu.infnet.tecnologiajava.Validador;
import br.edu.infnet.tecnologiajava.ValidadorException;
import br.edu.infnet.tecnologiajava.model.domain.Pedido;
import br.edu.infnet.tecnologiajava.model.domain.Produto;
import br.edu.infnet.tecnologiajava.model.view.LinhaRelatorioProdutosVendidos;
import br.edu.infnet.tecnologiajava.model.view.RelatorioProdutosVendidos;
import br.edu.infnet.tecnologiajava.repository.RepositorioPedido;
import br.edu.infnet.tecnologiajava.services.bancodados.BancoDadosException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;

public class GeradorRelatorioProdutosVendidos {
    private final int mes;
    private final int ano;

    public GeradorRelatorioProdutosVendidos(int ano, int mes) throws ValidadorException {
        Validador validador = new Validador();
        validador.valida("O ano precisa ser maior ou igual a 2023", ano >= 2023);
        validador.valida("O mês precisa estar entre 1 e 12", mes >= 1 && mes <= 12);
        if (validador.temErro()) {
            throw new ValidadorException("Os parâmetros de entrada do relatório produtos vendidos não são válidos", validador);
        }
        this.mes = mes;
        this.ano = ano;
    }

    public RelatorioProdutosVendidos geraRelatorioProdutosVendidos(RepositorioPedido repositorioPedido) throws BancoDadosException {
        Predicate<Pedido> filtro = pedido -> {
            LocalDateTime data = pedido.getData();
            return (data.getMonthValue() == mes && data.getYear() == ano);
        };
        List<Pedido> pedidos = repositorioPedido.getValores(filtro);

        return pedidos.isEmpty()?getRelatorioVendasSemDados(pedidos):getRelatorioVendasComDados(pedidos);
    }

    private RelatorioProdutosVendidos getRelatorioVendasSemDados(List<Pedido> pedidos) {
        RelatorioProdutosVendidos produtosVendidos = new RelatorioProdutosVendidos();
        produtosVendidos.setAno(ano);
        produtosVendidos.setMes(mes);
        produtosVendidos.setDadosVenda(Collections.emptyList());
        produtosVendidos.setMensagem("Não há dados de venda para o período.");
        return produtosVendidos;
    }

    private RelatorioProdutosVendidos getRelatorioVendasComDados(List<Pedido> pedidos) {
        Map<Produto, Integer> quantidadeProdutos = new TreeMap<>(
                (produto1, produto2) -> produto1.getCodigo() - produto2.getCodigo()
        );
        pedidos.forEach(pedido ->
            pedido.getProdutos().forEach(produto -> {
                int quantidade = quantidadeProdutos.getOrDefault(produto, 0);
                quantidadeProdutos.put(produto, quantidade + 1);
            })
        );

        List<LinhaRelatorioProdutosVendidos> dadosVenda = quantidadeProdutos.entrySet().stream()
                .map(entry -> new LinhaRelatorioProdutosVendidos(entry.getKey(), entry.getValue()))
                .toList();

        RelatorioProdutosVendidos produtosVendidos = new RelatorioProdutosVendidos();
        produtosVendidos.setAno(ano);
        produtosVendidos.setMes(mes);
        produtosVendidos.setDadosVenda(dadosVenda);

        float valorVendas = dadosVenda.stream()
                .map(linha -> linha.getValorTotal())
                .reduce(0.0f, Float::sum);
        ;
        produtosVendidos.setMensagem(String.format(Locale.forLanguageTag("PT"), "O valor total vendido é de R$ %.2f reais.", valorVendas));
        return produtosVendidos;
    }

    public String geraRelatorioProdutosVendidosMd(RepositorioPedido repositorioPedido) throws BancoDadosException {
        RelatorioProdutosVendidos relatorioProdutosVendidos = geraRelatorioProdutosVendidos(repositorioPedido);
        StringBuilder builder = new StringBuilder();
        builder.append("# Relatório de vendas\n");
        builder.append("*Ano:* ").append(ano).append(' ');
        builder.append("*Mês:* ").append(mes).append('\n');
        builder.append('\n');

        if(!relatorioProdutosVendidos.getDadosVenda().isEmpty()) {
            adicionaTabelaDados(builder, relatorioProdutosVendidos.getDadosVenda());
        }

        builder.append('*').append(relatorioProdutosVendidos.getMensagem()).append("*\n");
        return builder.toString();
    }

    private void adicionaTabelaDados(StringBuilder builder, List<LinhaRelatorioProdutosVendidos> dadosVendas) {
        builder.append("|Código|Tipo|Nome|Valor|Quantidade|Total|\n");
        builder.append("|------|----|----|----:|---------:|----:|\n");
        dadosVendas.forEach(linha -> {
            builder.append('|');
            builder.append(linha.getCodigo()).append('|');
            builder.append(linha.getTipo()).append('|');
            builder.append(linha.getNome()).append('|');
            builder.append(getValorFloatAsString(linha.getValor())).append('|');
            builder.append(linha.getQuantidade()).append('|');
            builder.append(getValorFloatAsString(linha.getValorTotal())).append('|').append('\n');
        });
    }

    private String getValorFloatAsString(float valor) {
        return String.format(Locale.forLanguageTag("PT"), "%.2f", valor);
    }
}
