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
@JsonDeserialize(builder = Comida.Builder.class)
public final class Comida extends Produto {

    private final float peso;
    private final boolean vegano;
    private final String ingredientes;

    public Comida(int codigo, String nome, String ingredientes, float peso, boolean vegano,
                  float valor) throws ValidadorException {
        super(nome, valor, codigo);
        this.peso = peso;
        this.vegano = vegano;
        this.ingredientes = ingredientes;
        valida();
    }

    public Comida(String nome, String ingredientes, float peso, boolean vegano,
                  float valor) throws ValidadorException {
        super(nome, valor);
        this.peso = peso;
        this.vegano = vegano;
        this.ingredientes = ingredientes;
        valida();
    }

    private void valida() throws ValidadorException {
        Validador validador = new Validador();
        super.validaCamposProduto(validador);
        validador.valida("Os ingredientes não podem ser nulo", ingredientes != null);
        validador.valida("Os ingredientes não podem estar em branco", ingredientes == null || !ingredientes.isBlank());
        validador.valida("O peso precisa ser maior que zero", peso > 0.0f);
        if (validador.temErro()) {
            throw new ValidadorException("Há campos da comida inválidos", validador);
        }
    }

    @Override
    public String getDetalhe() {
        return String.format(Locale.forLanguageTag("PT"), "%singredientes: %s - %.2f kg",
                vegano ? "vegano, " : "", ingredientes, peso);
    }

    @Override
    public String toString() {
        return String.format(Locale.US,
                "Comida: codigo=%d, nome=%s, ingredientes=%s, vegano=%b, peso=%.2f, valor=%.2f",
                getCodigo(), getNome(), ingredientes, vegano, peso, getValor());
    }

    @JsonPOJOBuilder
    static class Builder {
        private int codigo;
        private String nome;
        private String ingredientes;
        private float peso;
        private boolean vegano;
        private float valor;

        Builder withCodigo(int codigo) {
            this.codigo = codigo;
            return this;
        }

        Builder withNome(String nome) {
            this.nome = nome;
            return this;
        }

        Builder withIngredientes(String ingredientes) {
            this.ingredientes = ingredientes;
            return this;
        }

        Builder withPeso(float peso) {
            this.peso = peso;
            return this;
        }

        Builder withVegano(boolean vegano) {
            this.vegano = vegano;
            return this;
        }

        Builder withValor(float valor) {
            this.valor = valor;
            return this;
        }

        public Comida build() throws ValidadorException {
            return new Comida(codigo, nome, ingredientes, peso, vegano, valor);
        }
    }
}
