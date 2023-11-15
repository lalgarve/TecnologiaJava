
package br.edu.infnet.tecnologiajava.model.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import br.edu.infnet.tecnologiajava.services.bancodados.ListaComCopiaSegura;
import br.edu.infnet.tecnologiajava.services.bancodados.ValorBD;

public class Pedido implements ValorBD<Integer> {
  private final String descricao;
  private final LocalDateTime data;
  private final boolean web;
  private float valorTotal = -1.0f;
  private final ListaComCopiaSegura<Produto> produtos;
  private static int proximoCodigo = 1;
  private final int codigo;
  private final Solicitante solicitante;


  public Pedido(String descricao, boolean web, Solicitante solicitante) throws ValidadorException {
    this(proximoCodigo++, descricao, LocalDateTime.now(), web, solicitante);
  }

  public Pedido(String descricao, LocalDateTime data, boolean web, Solicitante solicitante) throws ValidadorException{
    this(proximoCodigo++, descricao, data, web, solicitante);
  }

  public Pedido(int codigo, String descricao, LocalDateTime data, boolean web, Solicitante solicitante) throws ValidadorException{
    this.codigo = codigo;
    produtos = new ListaComCopiaSegura<>();
    this.descricao = descricao;
    this.data = data;
    this.web = web;
    this.solicitante = solicitante;
  }

  public Pedido(Pedido pedido) {
    data = pedido.data;
    descricao = pedido.descricao;
    web = pedido.web;
    codigo = pedido.codigo;
    produtos = new ListaComCopiaSegura<>(pedido.produtos);
    solicitante = pedido.solicitante;
  }

  public String getDescricao() {
    return descricao;
  }

  public LocalDateTime getData() {
    return data;
  }

  public boolean isWeb() {
    return web;
  }

  public Stream<Produto> getProdutos(){
     return produtos.stream();
  }

  public Solicitante getSolicitante() {
    return solicitante;
  }

  public void setProdutos(List<Produto> produtos) throws ValidadorException{
    this.produtos.clear();
    this.produtos.addAll(produtos);
    valorTotal=-1;
  }

  public float getValorTotal(){
    if(valorTotal<0){
      valorTotal = produtos.stream().map((produto)->produto.getValor()).reduce(0.0f, Float::sum);
    }
    return valorTotal;
  }

  public int getNumeroProdutos(){
    return produtos.size();
  }

  @Override
  public Integer getChave() {
    return codigo;
  }

  @Override
  public Pedido getInstanciaCopiaSegura() {
    return new Pedido(this);
  }

  @Override
  public String toString(){
    if(solicitante==Solicitante.getVazio()){
      return String.format(Locale.forLanguageTag("PT"), "Pedido: codigo=%1$d, data=%2$td %2$tb %2$tY %2$tH:%2$tM, descricao=%3$s, web=%4$b, sem solicitante, número produtos=%5$d, valor total=%6$.2f",
        codigo, data, descricao, web, produtos.size(), getValorTotal());
    } else {
      return String.format("Pedido: codigo=%1$d, data=%2$td %2$tb %2$tY %2$tH:%2$tM, descricao=%3$s, web=%4$b, solicitante=%5$s, número produtos=%6$d, valor total=%7$.2f",
        codigo, data, descricao, web, solicitante.getCPF(), produtos.size(), getValorTotal());
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + descricao.hashCode();
    result = prime * result + data.hashCode();
    result = prime * result + (web ? 1231 : 1237);
    result = prime * result + produtos.hashCode();
    result = prime * result + codigo;
    result = prime * result + solicitante.hashCode();
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
    Pedido other = (Pedido) obj;
    if (!descricao.equals(other.descricao))
      return false;
    if (!data.equals(other.data))
      return false;
    if (web != other.web)
      return false;
    if (!produtos.equals(other.produtos))
      return false;
    if (codigo != other.codigo)
      return false;
    if (!solicitante.equals(other.solicitante))
      return false;
    return true;
  }


}
