package br.edu.infnet.tecnologiajava.model.mapper;

import br.edu.infnet.tecnologiajava.model.domain.Pedido;
import br.edu.infnet.tecnologiajava.model.domain.Produto;
import br.edu.infnet.tecnologiajava.model.domain.Solicitante;
import br.edu.infnet.tecnologiajava.model.domain.ValidadorException;
import br.edu.infnet.tecnologiajava.services.mapper.MapperAbstrato;
import br.edu.infnet.tecnologiajava.services.mapper.MapperException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class PedidoMapper extends MapperAbstrato<Pedido> {

    private String descricao;
    private LocalDateTime data;
    private boolean web;
    private List<Produto> produtos;
    private String cpfSolicitante;

    public PedidoMapper() {
        super(new String[]{"descricao", "data", "web", "produtos", "cpfSolicitante"});
    }

    @Override
    public void setValor(String campo, String valorComoString) throws MapperException {
        super.adicionaCampoSetado(campo);
        switch (campo) {
            case "descricao" -> descricao = valorComoString;
            case "data" -> data = converteData(valorComoString);
            case "produtos" -> {
                try {
                    produtos = constroiListaProdutos(valorComoString);
                } catch (ValidadorException e) {
                    throw new MapperException("Código de produto inválido.", e);
                }
            }
            case "cpfSolicitante" -> cpfSolicitante = valorComoString;
            case "web" -> web = converteBoolean(valorComoString);
            default -> throw new MapperException("O campo " + campo + " não existe.");
        }
    }

    @Override
    public Pedido build() throws MapperException {
        verificaTodosCamposSetatos();
        try {
            finaliza();
            Solicitante solicitante = new Solicitante(cpfSolicitante);
            Pedido pedido = new Pedido(descricao, data, web, solicitante);
            pedido.setProdutos(produtos);
            return pedido;
        } catch (ValidadorException ex) {
            throw new MapperException("Informação mapeada inválida.", ex);
        }
    }

    private List<Produto> constroiListaProdutos(String valorComoString) throws ValidadorException {
        String[] codigosComoString = valorComoString.split("[, ]+");
        Iterator<Integer> iterator = Arrays.stream(codigosComoString)
                .map(this::converteInt).iterator();
        List<Produto> produtosSoComCodigo = new ArrayList<>();
        while (iterator.hasNext()) {
            Produto produto = new ProdutoDesconhecido(iterator.next());
            produtosSoComCodigo.add(produto);
        }
        return produtosSoComCodigo;
    }


}
