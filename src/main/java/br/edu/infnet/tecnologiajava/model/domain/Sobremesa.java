package br.edu.infnet.tecnologiajava.model.domain;

import java.util.Locale;

public final class Sobremesa extends Produto {

    
    private final boolean doce;
    private final String informacao;
    private final float quantidade;

    

    public Sobremesa(int codigo, String nome, boolean doce, String informacao, float quantidade, float valor) {
        super(nome, valor, codigo);
        this.doce = doce;
        this.informacao = informacao;
        this.quantidade = quantidade;
        valida();
    }

    public Sobremesa(String nome, boolean doce, String informacao, float quantidade, float valor) {
        super(nome, valor);
        this.doce = doce;
        this.informacao = informacao;
        this.quantidade = quantidade;
        valida();
    }

    private void valida(){
        Validador validador = new Validador();
        super.validaCamposProduto(validador);
        validador.valida("A informação não pode ser nula", informacao!=null);
        validador.valida("A informação não pode estar em branco", informacao==null || !informacao.isBlank());
        validador.valida("A quantidade precisa ser maior que zero", quantidade > 0.0);
        if(validador.temErro()){
          throw  new ValidadorException("Há campos da sobremesa inválidos", validador);
        }
    }

    public boolean isDoce() {
        return doce;
    }

   public String getInformacao() {
        return informacao;
    }


    public float getQuantidade() {
        return quantidade;
    }

    @Override
    public String getDetalhe() {
        return String.format(Locale.forLanguageTag("PT"), "Sobremesa %s %s - %.2f Kg", 
        doce?"doce":"salgada", informacao, quantidade);
    }

    @Override
    public String toString() { 
        return String.format(Locale.US, 
        "Sobremesa: codigo=%d, nome=%s, informacao=%s, doce=%b, quantidade=%.2f, valor=%.2f",
        getCodigo(), getNome(), informacao, doce, quantidade, getValor());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (doce ? 1231 : 1237);
        result = prime * result + ((informacao == null) ? 0 : informacao.hashCode());
        result = prime * result + Float.floatToIntBits(quantidade);
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
        Sobremesa other = (Sobremesa) obj;
        if (!super.comparaCamposProduto(other)){
            return false;
        }
        if (doce != other.doce)
            return false;
        if (!informacao.equals(other.informacao))
            return false;
        if (Float.floatToIntBits(quantidade) != Float.floatToIntBits(other.quantidade))
            return false;
        return true;
    }
    
}
