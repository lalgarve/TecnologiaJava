package br.edu.infnet.tecnologiajava.model.domain;

import br.edu.infnet.tecnologiajava.Validador;
import br.edu.infnet.tecnologiajava.ValidadorException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Locale;

@Getter
@EqualsAndHashCode(callSuper = true)
@JsonDeserialize(builder = Sobremesa.Builder.class)
public final class Sobremesa extends Produto {

    private final boolean doce;
    private final String informacao;
    private final float quantidade;

    public Sobremesa(int codigo, String nome,
                     boolean doce, String informacao, float quantidade,
                     float valor) throws ValidadorException {
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

    @JsonPOJOBuilder
    static class Builder {
        private int codigo;
        private String nome;
        private boolean doce;
        private String informacao;
        private float quantidade;
        private float valor;

        Builder withCodigo(int codigo) {
            this.codigo = codigo;
            return this;
        }

        Builder withNome(String nome) {
            this.nome = nome;
            return this;
        }

        Builder withDoce(boolean doce) {
            this.doce = doce;
            return this;
        }

        Builder withInformacao(String informacao) {
            this.informacao = informacao;
            return this;
        }

        Builder withQuantidade(float quantidade) {
            this.quantidade = quantidade;
            return this;
        }

        Builder withValor(float valor) {
            this.valor = valor;
            return this;
        }

        public Sobremesa build() throws ValidadorException {
            return new Sobremesa(codigo, nome, doce, informacao, quantidade, valor);
        }
    }

}
