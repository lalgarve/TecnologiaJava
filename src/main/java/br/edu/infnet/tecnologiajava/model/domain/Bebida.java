package br.edu.infnet.tecnologiajava.model.domain;

import java.util.Locale;

public final class Bebida extends Produto {
  
  private final boolean gelada;
  private final float tamanho;
  private final String marca;

  public Bebida(int codigo, String nome, String marca, float tamanho, boolean gelada, float valor) {
    super(nome, valor, codigo);
    this.gelada = gelada;
    this.tamanho = tamanho;
    this.marca = marca;
    valida();
  }

  public Bebida(String nome, String marca, float tamanho, boolean gelada, float valor) {
    super(nome, valor);
    this.gelada = gelada;
    this.tamanho = tamanho;
    this.marca = marca;
    valida();
  }

  private void valida(){
    Validador validador = new Validador();
    super.validaCamposProduto(validador);
    validador.valida("A marca não pode ser nula", marca!=null);
    validador.valida("A marca não pode estar em branco", marca==null || !marca.isBlank());
    validador.valida("O tamanho precisa estar entre 0,1 L e 10 L", tamanho >= 0.1 && tamanho <= 10.0f);
    if(validador.temErro()){
      throw  new ValidadorException("Há campos da bebida inválidos", validador);
    }
  }

  public boolean isGelada() {
    return gelada;
  }

  public float getTamanho() {
    return tamanho;
  }

  public String getMarca() {
    return marca;
  }

  @Override
  public String getDetalhe() {
    return String.format(Locale.forLanguageTag("PT"), "Bebida %s %s - %.1f L - %s", 
      getNome(), marca, tamanho, gelada?"gelada":"quente");
  }

  @Override
  public String toString() { 
    return String.format(Locale.US, 
      "Bebida: codigo=%d, nome=%s, marca=%s, tamanho=%.1f, gelada=%s, valor=%.2f",
      getCodigo(), getNome(), getMarca(), getTamanho(), isGelada(), getValor());
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (gelada ? 1231 : 1237);
    result = prime * result + Float.floatToIntBits(tamanho);
    result = prime * result + ((marca == null) ? 0 : marca.hashCode());
    return result + super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Bebida other = (Bebida) obj;
    if (gelada != other.gelada)
      return false;
    if (Float.floatToIntBits(tamanho) != Float.floatToIntBits(other.tamanho))
      return false;
    if (!marca.equals(other.marca))
      return false;
    return super.comparaCamposProduto(other);
  }

  
  
}
