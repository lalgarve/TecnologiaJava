package br.edu.infnet.tecnologiajava.model.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Locale;

@Getter @EqualsAndHashCode(callSuper = true)
public final class Sobremesa extends Produto {

    private final boolean doce;
    private final String informacao;
    private final float quantidade;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Sobremesa(@JsonProperty("codigo") int codigo, @JsonProperty("nome") String nome,
                     @JsonProperty("doce") boolean doce, @JsonProperty("informacao") String informacao, float quantidade,
                     @JsonProperty("valor") float valor) throws ValidadorException {
        super(nome, valor, codigo);
        this.doce = doce;
        this.informacao = informacao;
        this.quantidade = quantidade;
        valida();
    }

    public Sobremesa(String nome, boolean doce, String informacao, float quantidade,
                     float valor) throws ValidadorException {
        super(nome, valor);
        this.doce = doce;
        this.informacao = informacao;
        this.quantidade = quantidade;
        valida();
    }

    private void valida() throws ValidadorException {
        Validador validador = new Validador();
        super.validaCamposProduto(validador);
        validador.valida("A informação não pode ser nula", informacao != null);
        validador.valida("A informação não pode estar em branco", informacao == null || !informacao.isBlank());
        validador.valida("A quantidade precisa ser maior que zero", quantidade > 0.0);
        if (validador.temErro()) {
            throw new ValidadorException("Há campos da sobremesa inválidos", validador);
        }
    }

    @Override
    public String getDetalhe() {
        return String.format(Locale.forLanguageTag("PT"), "%s %s - %.2f Kg",
                doce ? "doce" : "salgada", informacao, quantidade);
    }

    @Override
    public String toString() {
        return String.format(Locale.US,
                "Sobremesa: codigo=%d, nome=%s, informacao=%s, doce=%b, quantidade=%.2f, valor=%.2f",
                getCodigo(), getNome(), informacao, doce, quantidade, getValor());
    }

}
