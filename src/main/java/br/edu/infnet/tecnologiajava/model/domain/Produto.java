package br.edu.infnet.tecnologiajava.model.domain;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import br.edu.infnet.tecnologiajava.services.bancodados.ValorBD;
import br.edu.infnet.tecnologiajava.services.bancodados.Imutavel;

public abstract sealed class Produto implements ValorBD<Integer>, Imutavel permits Bebida, Sobremesa, Comida{
  
  private static int proximoCodigo = 1;

  private final String nome;
  private final float valor;
  private final int codigo;
  private static final Set<TipoProduto> tiposProdutos; 
  
  static{
    Set<TipoProduto> modificavel = new TreeSet<>();
    modificavel.add(new TipoProduto("Sobremesa", Sobremesa.class));
    modificavel.add(new TipoProduto("Bebida", Bebida.class));
    modificavel.add(new TipoProduto("Comida", Comida.class));
    tiposProdutos=Collections.unmodifiableSet(modificavel);
  }
  
  public Produto(final String nome, final float valor, final int codigo) {
    this.nome = nome;
    this.valor = valor;
    this.codigo = codigo;
  }

  public Produto(final String nome, final float valor) {
    this.nome = nome;
    this.valor = valor;
    this.codigo = proximoCodigo++;
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
  public Integer getChave(){
     return codigo;
  }

  @Override
  public Produto getInstanciaCopiaSegura(){
    return this;
  }
   
  public static Set<TipoProduto> getTiposprodutos() {
    return tiposProdutos;
  }

  public abstract String getDetalhe();

  protected boolean comparaCamposProduto(Produto other){        
    if (!nome.equals(other.nome))
      return false;
    if (Float.floatToIntBits(valor) != Float.floatToIntBits(other.valor))
      return false;
    if (codigo != other.codigo)
      return false;
    return true;
  }

  protected void validaCamposProduto(Validador validador){
    validador.valida("O nome não pode ser nulo", nome!=null);
    validador.valida("O nome não pode estar em branco", nome==null || !nome.isBlank());
    validador.valida("O valor precisa ser maior que zero", valor > 0);
    validador.valida("O código precisa ser maior que zero", codigo>0);
  }
  
}
