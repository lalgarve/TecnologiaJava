package br.edu.infnet.tecnologiajava.model.domain;

import java.util.Locale;

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

    public float getPeso() {
        return peso;
    }

    public boolean isVegano() {
        return vegano;
    }

    public String getIngredientes() {
        return ingredientes;
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
                vegano?"vegano, ":"",ingredientes, peso);
    }

    @Override
    public String toString() {
        return String.format(Locale.US,
                "Comida: codigo=%d, nome=%s, ingredientes=%s, vegano=%b, peso=%.2f, valor=%.2f",
                getCodigo(), getNome(), ingredientes, vegano, peso, getValor());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(peso);
        result = prime * result + (vegano ? 1231 : 1237);
        result = prime * result + ((ingredientes == null) ? 0 : ingredientes.hashCode());
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
        Comida other = (Comida) obj;
        if(!super.comparaCamposProduto(other)){
            return false;
        }
        if (Float.floatToIntBits(peso) != Float.floatToIntBits(other.peso))
            return false;
        if (vegano != other.vegano)
            return false;
        if (!ingredientes.equals(other.ingredientes))
            return false;
        return true;
    }
}
