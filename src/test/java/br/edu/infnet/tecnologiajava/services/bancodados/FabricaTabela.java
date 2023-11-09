package br.edu.infnet.tecnologiajava.services.bancodados;

public interface FabricaTabela<C, V extends ValorBD<C>> {
  TabelaBD<C,V> constroiTabela(String nome);
 
}
