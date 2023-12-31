package br.edu.infnet.tecnologiajava.model.domain;

import br.edu.infnet.tecnologiajava.Validador;
import br.edu.infnet.tecnologiajava.ValidadorException;
import br.edu.infnet.tecnologiajava.services.bancodados.ListaComCopiaSegura;
import br.edu.infnet.tecnologiajava.services.bancodados.ValorBD;
import br.edu.infnet.tecnologiajava.services.mapper.MapperException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

@Getter
@JsonDeserialize(builder = Pedido.Builder.class)
public class Pedido implements ValorBD<Integer, Pedido> {
    private final String descricao;
    private final LocalDateTime data;
    private final boolean web;
    private float valorTotal = -1.0f;
    private final ListaComCopiaSegura<Produto> produtos;
    private static int proximoCodigo = 1;
    private final int codigo;
    private Solicitante solicitante;

    public Pedido(String descricao, boolean web, Solicitante solicitante) throws ValidadorException {
        this(proximoCodigo++, descricao, LocalDateTime.now(), web, solicitante);
    }

    public Pedido(String descricao, LocalDateTime data, boolean web, Solicitante solicitante) throws ValidadorException {
        this(proximoCodigo++, descricao, data, web, solicitante);
    }

    public Pedido(int codigo, String descricao, LocalDateTime data, boolean web, Solicitante solicitante) throws ValidadorException {
        this.codigo = codigo;
        produtos = new ListaComCopiaSegura<>();
        this.descricao = descricao;
        this.data = data;
        this.web = web;
        this.solicitante = solicitante;
        Validador validador = valida();
        if (validador.temErro()) {
            throw new ValidadorException("Há erros nos campos do pedido: ", validador);
        }
    }

    public Pedido(Pedido pedido) {
        data = pedido.data;
        descricao = pedido.descricao;
        web = pedido.web;
        codigo = pedido.codigo;
        produtos = new ListaComCopiaSegura<>(pedido.produtos);
        solicitante = pedido.solicitante;
    }


    public static void inicializaContadorCodigo() {
        proximoCodigo = 1;
    }

    private Validador valida() {
        Validador validador = new Validador();
        validador.valida("O código precisa ser maior que zero", codigo > 0);
        validador.valida("A descrição não pode ser nula", descricao != null);
        validador.valida("A descrição não pode estar em branco", descricao == null || !descricao.isBlank());
        validador.valida("A data não pode ser nula", data != null);
        validador.valida("O solicitante não pode ser nulo", solicitante != null);
        return validador;
    }

    public Stream<Produto> getProdutos() {
        return produtos.stream();
    }

    public void setSolicitante(Solicitante solicitante) {
        this.solicitante = solicitante;
    }

    public void setProdutos(List<Produto> produtos) {
        Objects.requireNonNull(produtos, "A lista com produtos não pode ser nula.");
        this.produtos.clear();
        this.produtos.addAll(produtos);
        valorTotal = -1;
    }
    @JsonIgnore
    public float getValorTotal() {
        if (valorTotal < 0) {
            valorTotal = produtos.stream().map(Produto::getValor).reduce(0.0f, Float::sum);
        }
        return valorTotal;
    }

    @JsonIgnore
    public int getNumeroProdutos() {
        return produtos.size();
    }

    @Override
    @JsonIgnore
    public Integer getChave() {
        return codigo;
    }

    @Override
    public Pedido criaInstanciaCopiaSegura() {
        return new Pedido(this);
    }

    @Override
    public boolean podeSerGravadoNoBanco() {
        return !produtos.isEmpty();
    }

    @Override
    public String toString() {
        if (solicitante == Solicitante.getVazio()) {
            return String.format(Locale.forLanguageTag("PT"), "Pedido: codigo=%1$d, data=%2$td %2$tb %2$tY %2$tH:%2$tM, descricao=%3$s, web=%4$b, sem solicitante, número produtos=%5$d, valor total=%6$.2f",
                    codigo, data, descricao, web, produtos.size(), getValorTotal());
        } else {
            return String.format(Locale.forLanguageTag("PT"), "Pedido: codigo=%1$d, data=%2$td %2$tb %2$tY %2$tH:%2$tM, descricao=%3$s, web=%4$b, solicitante=%5$s%6$s, número produtos=%7$d, valor total=%8$.2f",
                    codigo, data, descricao, web, solicitante.getCpf(), solicitante.podeSerGravadoNoBanco() ? "" : " (incompleto)", produtos.size(), getValorTotal());
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + descricao.hashCode();
        result = prime * result + data.hashCode();
        result = prime * result + (web ? 1231 : 1237);
        result = prime * result + produtos.hashCode();
        result = prime * result + codigo;
        result = prime * result + solicitante.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pedido other = (Pedido) obj;
        if (!descricao.equals(other.descricao))
            return false;
        if (!data.equals(other.data))
            return false;
        if (web != other.web)
            return false;
        if (!produtos.equals(other.produtos))
            return false;
        if (codigo != other.codigo)
            return false;
        return solicitante.equals(other.solicitante);
    }

    @JsonPOJOBuilder
    static class Builder {
        private int codigo;
        private String descricao;
        private LocalDateTime data;
        private List<Produto> produtos;
        private Solicitante solicitante;
        private boolean web;

        Builder withCodigo(int codigo){
            this.codigo = codigo;
            return this;
        }

        Builder withDescricao(String descricao){
            this.descricao = descricao;
            return this;
        }

        Builder withData(String data) {
            try {
                this.data = LocalDateTime.parse(data, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } catch (DateTimeParseException e) {
                throw new MapperException(data + " não está no formato ano-mês-diaThora:minuto, por exemplo: 2023-01-10T13:30.");
            }
            return this;
        }
        Builder withProdutos(List<Integer> produtos) throws ValidadorException {
           this.produtos = new ArrayList<>(produtos.size());
            for(Integer codigoProduto:produtos){
                this.produtos.add(new ProdutoCodigo(codigoProduto));
            }
            return this;
        }

        Builder withCpfSolicitante(String cpfSolicitante) throws ValidadorException {
            this.solicitante = new Solicitante(cpfSolicitante);
            return this;
        }

        Builder withWeb(boolean web){
            this.web = web;
            return this;
        }

        public Pedido build() throws ValidadorException {
            Pedido pedido = new Pedido(codigo, descricao, data, web, solicitante);
            pedido.setProdutos(produtos);
            return pedido;
        }

    }
}
