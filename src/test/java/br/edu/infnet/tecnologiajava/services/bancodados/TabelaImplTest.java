
package br.edu.infnet.tecnologiajava.services.bancodados;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author leila
 */
public class TabelaImplTest {
  
  private static List<ValorSemDependente> valoresTeste;
  private static List<ValorSemDependente> valoresTesteMesmaChave;
  
  public TabelaImplTest() {
  }
  
  @BeforeAll
  public static void inicializaValores(){
    
    valoresTeste = new ArrayList<>();
    valoresTesteMesmaChave = new ArrayList<>();
    
    for(int i=0; i<5; i++){
      ValorSemDependente valor;
      valor = new ValorSemDependente(i);
      valor.setDescricao("valor"+i);
      valoresTeste.add(valor);
      
      valor = new ValorSemDependente(i);
      valor.setDescricao("valor"+i);
      valoresTesteMesmaChave.add(valor);

    }
    
  }

  @Test
  public void testAdiciona() throws Exception {
    ValorSemDependente valor = valoresTeste.get(0);
    TabelaImpl<Integer, ValorSemDependente> instance = new TabelaImpl<>("minhatabela");
    instance.adiciona(valor);
    Optional<ValorSemDependente> resultado = instance.consultaPorId(valor.getChave());
    assertTrue(resultado.isPresent());
    assertEquals(valor, resultado.get());
    assertFalse(valor==resultado.get(),"Um clone deveria ter sido retornado/");
  }
  
  @Test
  public void testAdicionaMesmaChave() throws Exception {
    ValorSemDependente valor = valoresTeste.get(0);
    ValorSemDependente valorMesmaChave = valoresTesteMesmaChave.get(0);
    TabelaImpl<Integer, ValorSemDependente> instance = new TabelaImpl<>("minhatabela");
    instance.adiciona(valor);
    BancoDadosException excecao = assertThrows(BancoDadosException.class, ()->instance.adiciona(valorMesmaChave));
    assertEquals("A chave 0 já existe na tabela.", excecao.getMessage());
  }
  
  @Test 
  public void testAdicionaChaveNula() throws Exception {
    ValorSemDependente valor = new ValorSemDependente();
    TabelaImpl<Integer, ValorSemDependente> instance = new TabelaImpl<>("minhatabela");
    NullPointerException excecao = assertThrows(NullPointerException.class, () -> instance.adiciona(valor));
    assertEquals("A chave não pode ser nula.", excecao.getMessage());
  }
  
  @Test 
  public void testAdicionaValorNulo() throws Exception {
    ValorSemDependente valor = null;
    TabelaImpl<Integer, ValorSemDependente> instance = new TabelaImpl<>("minhatabela");
    NullPointerException excecao = assertThrows(NullPointerException.class, () -> instance.adiciona(valor));
    assertEquals("O valor não pode ser nulo.", excecao.getMessage());
  }
  
  @Test
  public void testRemove() throws Exception {
    ValorSemDependente valor = valoresTeste.get(0);
    TabelaImpl<Integer, ValorSemDependente> instance = new TabelaImpl<>("minhatabela");
    instance.adiciona(valor);    
    instance.removePorId(valor.getChave());
    Optional<ValorSemDependente> resultado = instance.consultaPorId(valor.getChave());
    assertTrue(resultado.isEmpty());
  }  

  @Test
  public void testRemoveValorNaoExiste() throws Exception {
    TabelaImpl<Integer, ValorSemDependente> instance = new TabelaImpl<>("minhatabela");
    BancoDadosException excecao = assertThrows(BancoDadosException.class, () -> instance.removePorId(0));
    assertEquals("A chave 0 não existe na tabela.", excecao.getMessage());
  }
  
  @Test
  public void testRemoveChaveNula() throws Exception {
    TabelaImpl<Integer, ValorSemDependente> instance = new TabelaImpl<>("minhatabela");
    NullPointerException excecao = assertThrows(NullPointerException.class, () -> instance.removePorId(null));
    assertEquals("A chave não pode ser nula.", excecao.getMessage());
  }  
  
  @Test
  public void testAltera() throws Exception{
    ValorSemDependente valor = valoresTeste.get(0);
    TabelaImpl<Integer, ValorSemDependente> instance = new TabelaImpl<>("minhatabela");
    instance.adiciona(valor);
    ValorSemDependente valorAlterado = valoresTesteMesmaChave.get(0);
    instance.altera(valorAlterado);
    Optional<ValorSemDependente> resultado = instance.consultaPorId(valor.getChave());
    assertTrue(resultado.isPresent());
    assertEquals(valorAlterado, resultado.get());
    assertFalse(valor==resultado.get(),"Um clone deveria ter sido retornado/");
  }
  
  @Test
  public void testAlteraChaveInexistente() throws Exception {
    ValorSemDependente valor = valoresTeste.get(0);
    TabelaImpl<Integer, ValorSemDependente> instance = new TabelaImpl<>("minhatabela"); 
    BancoDadosException excecao = assertThrows(BancoDadosException.class, () -> instance.altera(valor));
    assertEquals("Não exite chave 0 para ser alterada.", excecao.getMessage());
  }
  
  @Test
  public void testAlteraValorNulo() throws Exception {
    ValorSemDependente valor = null;
    TabelaImpl<Integer, ValorSemDependente> instance = new TabelaImpl<>("minhatabela"); 
    NullPointerException excecao = assertThrows(NullPointerException.class, () -> instance.altera(valor));
    assertEquals("O valor não pode ser nulo.", excecao.getMessage());
  }  

    
  @Test
  public void testAlteraChaveNula() throws Exception {
    ValorSemDependente valor = new ValorSemDependente();
    TabelaImpl<Integer, ValorSemDependente> instance = new TabelaImpl<>("minhatabela"); 
    NullPointerException excecao = assertThrows(NullPointerException.class, () -> instance.altera(valor));
    assertEquals("A chave não pode ser nula.", excecao.getMessage());
  } 
  
  @Test
  public void testConsultaPorIdChaveNula() throws Exception {
    ValorSemDependente valor = valoresTeste.get(0);
    TabelaImpl<Integer, ValorSemDependente> instance = new TabelaImpl<>("minhatabela");
    instance.adiciona(valor);
    Optional<ValorSemDependente> resultado = instance.consultaPorId(valor.getChave());
    NullPointerException excecao = assertThrows(NullPointerException.class, () ->  instance.consultaPorId(null));
    assertEquals("A chave não pode ser nula.", excecao.getMessage());
  }
  
 
  @Test
  public void testConsultaPorIdChaveNaoExiste() throws Exception {
    ValorSemDependente valor = valoresTeste.get(0);
    TabelaImpl<Integer, ValorSemDependente> instance = new TabelaImpl<>("minhatabela");
    instance.adiciona(valor);
    Optional<ValorSemDependente> resultado = instance.consultaPorId(valor.getChave()+1);
    assertTrue(resultado.isEmpty());
  } 
  
  @Test
  public void testGetValores() throws Exception {
    TabelaImpl<Integer, ValorSemDependente> instance = new TabelaImpl<>("minhatabela");
    valoresTeste.forEach((valor) -> {
      try {
        instance.adiciona(valor);
      } catch (BancoDadosException ex) {
        fail(ex);
      }
    });
    List<ValorSemDependente> valores = instance.getValores();
    assertEquals(valoresTeste.size(), valores.size());
    Optional<ValorSemDependente> naoExiste = valoresTeste.stream().filter((valorTeste) -> !valores.contains(valorTeste)).findFirst();
    assertTrue(naoExiste.isEmpty(), "Um valor não foi encontrado: "+naoExiste.orElse(null));
    Optional<ValorSemDependente> naoEClone = valoresTeste.stream().filter((valorTeste) -> {
      int index = valores.indexOf(valorTeste);
      return valores.get(index) != valorTeste;
    }).findFirst();
    assertTrue(naoExiste.isEmpty(), "Um valor não é clone encontrado: "+naoExiste.orElse(null));
  }
  
  @Test
  public void testGetValoresTabelaVazia() throws Exception {
    TabelaImpl<Integer, ValorSemDependente> instance = new TabelaImpl<>("minhatabela");
    List<ValorSemDependente> valores = instance.getValores();
    assertEquals(0, valores.size());
  }
}
