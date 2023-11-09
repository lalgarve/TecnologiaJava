
package br.edu.infnet.tecnologiajava.services.bancodados;

/**
 * Java permite parametrizar os testes com enumeracao
 * @author leila
 */
public enum FabricaTabelaBD {
  TABELAIMPL ((nome) -> new TabelaImpl(nome)),
  TABELA_DEPENDENTE ((nome) -> new TabelaDependente(nome));
  
  private final FabricaTabela<Integer, ValorSemDependente> fabrica;
  private FabricaTabelaBD(FabricaTabela<Integer, ValorSemDependente> fabrica){
    this.fabrica = fabrica;
  }
  
  public TabelaBD<Integer, ValorSemDependente> constroiTabela(String nome){
    return fabrica.constroiTabela(nome);
  }
  
}
