
package br.edu.infnet.tecnologiajava.model.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
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
}
