package br.edu.infnet.tecnologiajava.services.bancodados;

public interface FabricaTabela<C, V extends ValorBD<C,V>> {
  TabelaBD<C,V> constroiTabela(String nome);
 
}
