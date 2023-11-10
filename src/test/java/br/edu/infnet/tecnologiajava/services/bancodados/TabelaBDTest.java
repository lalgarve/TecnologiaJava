
package br.edu.infnet.tecnologiajava.services.bancodados;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testa se as classe implementaram a interface 
 * {@link br.edu.infnet.tecnologiajava.services.bancodados.TabelaBD TabelaBD}
 * corretamente. Os testes são parametrizados para incluir todas as classes.
 * 
 * @author leila
 */
public class TabelaBDTest {
  
  private static List<ValorSemDependente> valoresTeste;
  private static List<ValorSemDependente> valoresTesteMesmaChave;
  
  public TabelaBDTest() {
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

  @ParameterizedTest
  @EnumSource(FabricaTabelaBD.class)
  public void testAdiciona(FabricaTabelaBD fabrica) throws Exception {
    ValorSemDependente valor = valoresTeste.get(0);
    TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
    instance.adiciona(valor);
    Optional<ValorSemDependente> resultado = instance.consultaPorId(valor.getChave());
    assertTrue(resultado.isPresent());
    assertEquals(valor, resultado.get());
    assertFalse(valor==resultado.get(),"Um clone deveria ter sido retornado/");
  }
  
  @ParameterizedTest
  @EnumSource(FabricaTabelaBD.class)
  public void testAdicionaMesmaChave(FabricaTabelaBD fabrica) throws Exception {
    ValorSemDependente valor = valoresTeste.get(0);
    ValorSemDependente valorMesmaChave = valoresTesteMesmaChave.get(0);
    TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
    instance.adiciona(valor);
    BancoDadosException excecao = assertThrows(BancoDadosException.class, ()->instance.adiciona(valorMesmaChave));
    assertEquals("A chave 0 já existe na tabela.", excecao.getMessage());
  }
  
  @ParameterizedTest
  @EnumSource(FabricaTabelaBD.class) 
  public void testAdicionaChaveNula(FabricaTabelaBD fabrica) throws Exception {
    ValorSemDependente valor = new ValorSemDependente();
    TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
    NullPointerException excecao = assertThrows(NullPointerException.class, () -> instance.adiciona(valor));
    assertEquals("A chave não pode ser nula.", excecao.getMessage());
  }
  
  @ParameterizedTest
  @EnumSource(FabricaTabelaBD.class) 
  public void testAdicionaValorNulo(FabricaTabelaBD fabrica) throws Exception {
    ValorSemDependente valor = null;
    TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
    NullPointerException excecao = assertThrows(NullPointerException.class, () -> instance.adiciona(valor));
    assertEquals("O valor não pode ser nulo.", excecao.getMessage());
  }
  
  @ParameterizedTest
  @EnumSource(FabricaTabelaBD.class) 
  public void testRemove(FabricaTabelaBD fabrica) throws Exception {
    ValorSemDependente valor = valoresTeste.get(0);
    TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
    instance.adiciona(valor);    
    instance.removePorId(valor.getChave());
    Optional<ValorSemDependente> resultado = instance.consultaPorId(valor.getChave());
    assertTrue(resultado.isEmpty());
  }  

  @ParameterizedTest
  @EnumSource(FabricaTabelaBD.class) 
  public void testRemoveValorNaoExiste(FabricaTabelaBD fabrica) throws Exception {
    TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
    BancoDadosException excecao = assertThrows(BancoDadosException.class, () -> instance.removePorId(0));
    assertEquals("A chave 0 não existe na tabela.", excecao.getMessage());
  }
  
  @ParameterizedTest
  @EnumSource(FabricaTabelaBD.class) 
  public void testRemoveChaveNula(FabricaTabelaBD fabrica) throws Exception {
    TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
    NullPointerException excecao = assertThrows(NullPointerException.class, () -> instance.removePorId(null));
    assertEquals("A chave não pode ser nula.", excecao.getMessage());
  }  
  
  @ParameterizedTest
  @EnumSource(FabricaTabelaBD.class) 
  public void testAltera(FabricaTabelaBD fabrica) throws Exception{
    ValorSemDependente valor = valoresTeste.get(0);
    TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
    instance.adiciona(valor);
    ValorSemDependente valorAlterado = valoresTesteMesmaChave.get(0);
    instance.altera(valorAlterado);
    Optional<ValorSemDependente> resultado = instance.consultaPorId(valor.getChave());
    assertTrue(resultado.isPresent());
    assertEquals(valorAlterado, resultado.get());
    assertFalse(valor==resultado.get(),"Um clone deveria ter sido retornado/");
  }
  
  @ParameterizedTest
  @EnumSource(FabricaTabelaBD.class) 
  public void testAlteraChaveInexistente(FabricaTabelaBD fabrica) throws Exception {
    ValorSemDependente valor = valoresTeste.get(0);
    TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela"); 
    BancoDadosException excecao = assertThrows(BancoDadosException.class, () -> instance.altera(valor));
    assertEquals("Não exite chave 0 para ser alterada.", excecao.getMessage());
  }
  
  @ParameterizedTest
  @EnumSource(FabricaTabelaBD.class) 
  public void testAlteraValorNulo(FabricaTabelaBD fabrica) throws Exception {
    ValorSemDependente valor = null;
    TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela"); 
    NullPointerException excecao = assertThrows(NullPointerException.class, () -> instance.altera(valor));
    assertEquals("O valor não pode ser nulo.", excecao.getMessage());
  }  

    
  @ParameterizedTest
  @EnumSource(FabricaTabelaBD.class)
  public void testAlteraChaveNula(FabricaTabelaBD fabrica) throws Exception {
    ValorSemDependente valor = new ValorSemDependente();
    TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela"); 
    NullPointerException excecao = assertThrows(NullPointerException.class, () -> instance.altera(valor));
    assertEquals("A chave não pode ser nula.", excecao.getMessage());
  } 
  
  @ParameterizedTest
  @EnumSource(FabricaTabelaBD.class)
  public void testConsultaPorIdChaveNula(FabricaTabelaBD fabrica) throws Exception {
    ValorSemDependente valor = valoresTeste.get(0);
    TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
    instance.adiciona(valor);
    NullPointerException excecao = assertThrows(NullPointerException.class, () ->  instance.consultaPorId(null));
    assertEquals("A chave não pode ser nula.", excecao.getMessage());
  }
  
 
  @ParameterizedTest
  @EnumSource(FabricaTabelaBD.class)
  public void testConsultaPorIdChaveNaoExiste(FabricaTabelaBD fabrica) throws Exception {
    ValorSemDependente valor = valoresTeste.get(0);
    TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
    instance.adiciona(valor);
    Optional<ValorSemDependente> resultado = instance.consultaPorId(valor.getChave()+1);
    assertTrue(resultado.isEmpty());
  } 
  
  @ParameterizedTest
  @EnumSource(FabricaTabelaBD.class)
  public void testGetValores(FabricaTabelaBD fabrica) throws Exception {
    TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
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
  
  @ParameterizedTest
  @EnumSource(FabricaTabelaBD.class)
  public void testGetValoresTabelaVazia(FabricaTabelaBD fabrica) throws Exception {
    TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
    List<ValorSemDependente> valores = instance.getValores();
    assertEquals(0, valores.size());
  }
  
  @ParameterizedTest
  @EnumSource(FabricaTabelaBD.class)
  public void testGetValoresComFiltro(FabricaTabelaBD fabrica) throws Exception {
    TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
    valoresTeste.forEach((valor) -> {
      try {
        instance.adiciona(valor);
      } catch (BancoDadosException ex) {
        fail(ex);
      }
    });
    Predicate<ValorSemDependente> filtraPares = (valor) -> valor.getId()%2 == 0;
    
    List<ValorSemDependente> valores = instance.getValores(filtraPares);
    assertEquals(3, valores.size());
    long count = valores.stream().filter(filtraPares.negate()).count();
    assertEquals(count, 0);

  }
  
  @ParameterizedTest
  @EnumSource(FabricaTabelaBD.class)
  public void testGetValoresComFiltroNulo(FabricaTabelaBD fabrica) throws Exception {
    TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
    valoresTeste.forEach((valor) -> {
      try {
        instance.adiciona(valor);
      } catch (BancoDadosException ex) {
        fail(ex);
      }
    });
    
    NullPointerException excecao = assertThrows(NullPointerException.class, 
            () -> instance.getValores(null));
    assertEquals("O filtro não pode ser nulo.", excecao.getMessage());
  }  
  
  @ParameterizedTest
  @EnumSource(FabricaTabelaBD.class)
  public void testGetNome(FabricaTabelaBD fabrica) throws Exception {
    TabelaBD<Integer, ValorSemDependente> instance = fabrica.constroiTabela("minhatabela");
    assertEquals("minhatabela", instance.getNome());
  }
  
    
  @ParameterizedTest
  @EnumSource(FabricaTabelaBD.class)
  public void testNomeNull(FabricaTabelaBD fabrica) throws Exception {
    NullPointerException excecao = assertThrows(NullPointerException.class, 
            () -> fabrica.constroiTabela(null));
    assertEquals("O nome não pode ser nulo.", excecao.getMessage());
  }
  
      
  @ParameterizedTest
  @EnumSource(FabricaTabelaBD.class)
  public void testNomeBlack(FabricaTabelaBD fabrica) throws Exception {
    IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class, 
            () -> fabrica.constroiTabela("  "));
    assertEquals("O nome não pode estar vazio ou em branco.", excecao.getMessage());
  }
}
