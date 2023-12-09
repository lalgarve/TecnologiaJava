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
@JsonDeserialize(builder = Bebida.Builder.class)
public final class Bebida extends Produto {

    private final boolean gelada;
    private final float tamanho;
    private final String marca;

    public Bebida(int codigo, String nome, String marca, float tamanho,
                  boolean gelada, float valor) throws ValidadorException {
        super(nome, valor, codigo);
        this.gelada = gelada;
        this.tamanho = tamanho;
        this.marca = marca;
        valida();
    }

    public Bebida(String nome, String marca, float tamanho, boolean gelada,
                  float valor) throws ValidadorException {
        super(nome, valor);
        this.gelada = gelada;
        this.tamanho = tamanho;
        this.marca = marca;
        valida();
    }

    private void valida() throws ValidadorException {
        Validador validador = new Validador();
        super.validaCamposProduto(validador);
        validador.valida("A marca não pode ser nula", marca != null);
        validador.valida("A marca não pode estar em branco", marca == null || !marca.isBlank());
        validador.valida("O tamanho precisa estar entre 0,1 L e 10 L", tamanho >= 0.1f && tamanho <= 10.0f);
        if (validador.temErro()) {
            throw new ValidadorException("Há campos da bebida inválidos", validador);
        }
    }

    @Override
    public String getDetalhe() {
        return String.format(Locale.forLanguageTag("PT"), "Bebida %s - %.1f L - %s",
                marca, tamanho, gelada ? "gelada" : "quente");
    }

    @Override
    public String toString() {
        return String.format(Locale.US,
                "Bebida: codigo=%d, nome=%s, marca=%s, tamanho=%.1f, gelada=%s, valor=%.2f",
                getCodigo(), getNome(), getMarca(), getTamanho(), isGelada(), getValor());
    }

    @JsonPOJOBuilder
    static class Builder {
        private int codigo;
        private String nome;
        private String marca;
        private float tamanho;
        private boolean gelada;
        private float valor;

        Builder withCodigo(int codigo) {
            this.codigo = codigo;
            return this;
        }

        Builder withNome(String nome) {
            this.nome = nome;
            return this;
        }

        Builder withMarca(String marca) {
            this.marca = marca;
            return this;
        }

        Builder withTamanho(float tamanho) {
            this.tamanho = tamanho;
            return this;
        }

        Builder withGelada(boolean gelada) {
            this.gelada = gelada;
            return this;
        }

        Builder withValor(float valor) {
            this.valor = valor;
            return this;
        }

        public Bebida build() throws ValidadorException {
            return new Bebida(codigo, nome, marca, tamanho, gelada, valor);
        }
    }

}
