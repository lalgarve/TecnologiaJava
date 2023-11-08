package br.edu.infnet.tecnologiajava.model.domain;


public abstract class Produto {
  
  private String nome;
  private float valor;
  private int codigo;
  
  public abstract String getDetalhes();

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public float getValor() {
    return valor;
  }

  public void setValor(float valor) {
    this.valor = valor;
  }

  public int getCodigo() {
    return codigo;
  }

  public void setCodigo(int codigo) {
    this.codigo = codigo;
  }

}
