
package br.edu.infnet.tecnologiajava.services.bancodados;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes da tabela dependente que não testam a interface
 * {@link br.edu.infnet.tecnologiajava.services.bancodados.TabelaBD tabelaBD}
 * sem usar a {@link br.edu.infnet.tecnologiajava.services.bancodados.TabelaPrincipal tabelaPrincipal}
 * @author leila
 */
public class TabelaDependenteTest {
  
  private TabelaDependente<Integer, ValorSemDependente> tabelaDependente;
  private TabelaImpl<Integer, ValorComDependente> tabelaRelacao;
  
  public TabelaDependenteTest() {
  }
  
  @BeforeEach
  public void criaTabelaERelacoes() throws BancoDadosException{
    tabelaDependente = new TabelaDependente<>("tabeladependente");
    tabelaRelacao = new TabelaImpl<>("tabelarelacao");
    for(int i=0; i<5; i++){
      ValorSemDependente valor;
      valor = new ValorSemDependente(i);
      valor.setDescricao("valor"+i);
      tabelaDependente.adiciona(valor);
    }
  }

  @ParameterizedTest
  @ValueSource(ints = {0,1,4} )
  public void testAdicionaUso(int chave) throws Exception {
    tabelaDependente.adicionaUso(chave, tabelaRelacao);
    BancoDadosException excecao = assertThrows(BancoDadosException.class, 
            () -> tabelaDependente.removePorId(chave));
    assertEquals("O valor está sendo usado com. Chave: "+chave, excecao.getMessage());
  }

  @Test
  public void testAdicionaUsoChaveNula() throws Exception {
    NullPointerException excecao = assertThrows(NullPointerException.class, 
            () -> tabelaDependente.adicionaUso(null, tabelaRelacao));
    assertEquals("A chave não pode ser nula.", excecao.getMessage());
  }
  
  
  @ParameterizedTest
  @ValueSource(ints = {20,10,14} )
  public void testAdicionaUsoChaveNaoExiste(int chave) throws Exception {
    BancoDadosException excecao = assertThrows(BancoDadosException.class, 
            () -> tabelaDependente.adicionaUso(10, tabelaRelacao));
    assertEquals("A chave não existe: "+chave, excecao.getMessage());
  }


}
