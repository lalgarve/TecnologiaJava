
package br.edu.infnet.tecnologiajava.model.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.edu.infnet.tecnologiajava.services.bancodados.ValorBD;

public class Pedido implements ValorBD<Integer> {
  private String descricao;
  private LocalDateTime data;
  private boolean web;
  private List<Produto> produtos = new ArrayList<>();

  public String getDescricao() {
    return descricao;
  }

  protected void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public LocalDateTime getData() {
    return data;
  }

  public void setData(LocalDateTime data) {
    this.data = data;
  }

  public boolean isWeb() {
    return web;
  }

  public void setWeb(boolean web) {
    this.web = web;
  }

  @Override
  public Integer getChave() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getChave'");
  }

  @Override
  public ValorBD<Integer> getInstanciaCopiaSegura() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getInstanciaCopiaSegura'");
  } 
}
