package br.edu.infnet.tecnologiajava.services.bancodados;

import java.util.Objects;

/**
 *
 * @author leila
 */
public class ValorSemDependente implements ValorBD<Integer>, Cloneable{
  
  private final int id;
  private String descricao;
  private final boolean retornaChaveNula;

  public ValorSemDependente(int id) {
    this.id = id;
    retornaChaveNula = false;
  }
  
  public ValorSemDependente(){
    id = -1;
    retornaChaveNula = true;
  }

  @Override
  public Integer getChave() {
    if(retornaChaveNula){
      return null;
    }
    return id;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public int getId() {
    return id;
  }
  
  @Override
  public ValorSemDependente getDeepClone(boolean todosCampos){
    try {
      return (ValorSemDependente) super.clone();
    } catch (CloneNotSupportedException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public int hashCode() {
    int hash = 5;
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final ValorSemDependente other = (ValorSemDependente) obj;
    if (this.id != other.id) {
      return false;
    }
    if (this.retornaChaveNula != other.retornaChaveNula) {
      return false;
    }
    return Objects.equals(this.descricao, other.descricao);
  }


  
}
