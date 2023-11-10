package br.edu.infnet.tecnologiajava.services.bancodados;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ValorComDependente implements ValorBD<Integer>{

  private final Integer chave;
  private final List<ValorSemDependente> valoresDependentes;

  public ValorComDependente(Integer chave) {
    this.chave = chave;
    valoresDependentes = new ArrayList<>();
  } 
  
  public ValorComDependente(ValorComDependente aSerCopiado, boolean todosCampos){
    this.chave = aSerCopiado.chave;
    if(!todosCampos){
      valoresDependentes = null;
    } else {
      valoresDependentes = new ArrayList<>();
      aSerCopiado.valoresDependentes.forEach(
              (original) -> valoresDependentes.add(original.getDeepClone(true))
      );
    }
  }
  
  @Override
  public Integer getChave() {
    return chave;
  }
  
  public void adcionaValorDependente(ValorSemDependente valor){
    valoresDependentes.add(valor);
  }
  
  public void removeValorDependente(ValorSemDependente valor){
    valoresDependentes.remove(valor);
  }

  @Override
  public ValorComDependente getDeepClone(boolean todosCampos) {
    return new ValorComDependente(this, todosCampos);
  }
  
}
