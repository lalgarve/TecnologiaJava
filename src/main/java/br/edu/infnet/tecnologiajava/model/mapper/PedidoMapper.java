package br.edu.infnet.tecnologiajava.model.mapper;

import br.edu.infnet.tecnologiajava.model.domain.*;
import br.edu.infnet.tecnologiajava.services.csv.CSVMapperAbstrato;
import br.edu.infnet.tecnologiajava.services.csv.CSVMapperException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class PedidoMapper extends CSVMapperAbstrato<Pedido> {

    private String descricao;
    private LocalDateTime data;
    private boolean web;
    private List<Produto> produtos;
    private String cpfSolicitante;

    public PedidoMapper() {
        super(new String[]{"descricao", "data", "web", "produtos", "cpfSolicitante"});
    }

    @Override
    public void setValor(String campo, String valorComoString) throws CSVMapperException {
        super.adicionaCampoSetado(campo);
        switch (campo) {
            case "descricao" -> descricao = valorComoString;
            case "data" -> data = converteData(valorComoString);
            case "produtos" -> produtos = constroiListaProdutos(valorComoString);
            case "cpfSolicitante" -> cpfSolicitante = valorComoString;
            default -> throw new CSVMapperException("O campo " + campo + " não existe.");
        }
    }

    @Override
    public Pedido build() throws CSVMapperException {
        verificaTodosCamposSetatos();
        try {
            finaliza();
            Solicitante solicitante = cpfSolicitante.isBlank()
                    ? Solicitante.getVazio()
                    : new Solicitante(cpfSolicitante, "gerado", "gerado@email.com");
            Pedido pedido = new Pedido(descricao, data, web, solicitante);
            pedido.setProdutos(produtos);
            return pedido;
        } catch (ValidadorException ex) {
            throw new CSVMapperException("Informação mapeada inválida.", ex);
        }
    }

    private List<Produto> constroiListaProdutos(String valorComoString) {
        String[] codigosComoString = valorComoString.split(", ");
        Iterator<Integer> iterator = Arrays.stream(codigosComoString)
                .map(this::converteInt).iterator();
        List<Produto> produtosSoComCodigo = new ArrayList<>();
        while (iterator.hasNext()) {
            Sobremesa sobremesa = new Sobremesa(iterator.next());
            produtosSoComCodigo.add(sobremesa);
        }
        return produtosSoComCodigo;
    }


}
