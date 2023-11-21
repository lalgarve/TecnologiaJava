package br.edu.infnet.tecnologiajava.model.domain;

import br.edu.infnet.tecnologiajava.services.bancodados.Imutavel;
import br.edu.infnet.tecnologiajava.services.bancodados.ValorBD;

public abstract sealed class Produto implements ValorBD<Integer, Produto>, Imutavel
        permits Sobremesa, Comida, Bebida {

    private static int proximoCodigo = 1;

    private final String nome;
    private final float valor;
    private final int codigo;
    private final boolean completo;

    protected Produto(final String nome, final float valor, final int codigo) {
        this.nome = nome;
        this.valor = valor;
        this.codigo = codigo;
        completo = true;
    }

    protected Produto(final String nome, final float valor) {
        this.nome = nome;
        this.valor = valor;
        this.codigo = proximoCodigo++;
        completo = true;
    }

    protected Produto(int codigo) {
        this.completo = false;
        this.codigo = codigo;
        this.valor = 0.0f;
        this.nome = "";
    }

    public static void inicializaContadorCodigo() {
        proximoCodigo = 1;
    }

    public String getNome() {
        return nome;
    }

    public float getValor() {
        return valor;
    }

    public int getCodigo() {
        return codigo;
    }

    @Override
    public Integer getChave() {
        return codigo;
    }

    @Override
    public Produto getInstanciaCopiaSegura() {
        return this;
    }

    public abstract String getDetalhe();

    protected boolean comparaCamposProduto(Produto other) {
        if (!nome.equals(other.nome))
            return false;
        if (Float.floatToIntBits(valor) != Float.floatToIntBits(other.valor))
            return false;
        return codigo == other.codigo;
    }

    protected void validaCamposProduto(Validador validador) {
        validador.valida("O nome não pode ser nulo", nome != null);
        validador.valida("O nome não pode estar em branco", nome == null || !nome.isBlank());
        validador.valida("O valor precisa ser maior que zero", valor > 0);
        validador.valida("O código precisa ser maior que zero", codigo > 0);
    }

    @Override
    public boolean podeSerGravadoNoBanco() {
        return completo;
    }
}
