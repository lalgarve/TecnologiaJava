/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

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
    
    for(int i=0; i<7; i++){
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
    TabelaImpl<Integer, ValorSemDependente> instance = new TabelaImpl<>();
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
    TabelaImpl<Integer, ValorSemDependente> instance = new TabelaImpl<>();
    instance.adiciona(valor);
    BancoDadosException excecao = assertThrows(BancoDadosException.class, ()->instance.adiciona(valorMesmaChave));
    assertEquals("A chave 0 já existe na tabela.", excecao.getMessage());
  }
  
  @Test 
  public void testAdicionaChaveNula() throws Exception {
    ValorSemDependente valor = new ValorSemDependente();
    TabelaImpl<Integer, ValorSemDependente> instance = new TabelaImpl<>();
    NullPointerException excecao = assertThrows(NullPointerException.class, () -> instance.adiciona(valor));
    assertEquals("A chave não pode ser nula.", excecao.getMessage());
  }
  
  @Test 
  public void testAdicionaValorNulo() throws Exception {
    ValorSemDependente valor = null;
    TabelaImpl<Integer, ValorSemDependente> instance = new TabelaImpl<>();
    NullPointerException excecao = assertThrows(NullPointerException.class, () -> instance.adiciona(valor));
    assertEquals("O valor não pode ser nulo.", excecao.getMessage());
  }
  
  @Test
  public void testRemove() throws Exception {
    ValorSemDependente valor = valoresTeste.get(0);
    TabelaImpl<Integer, ValorSemDependente> instance = new TabelaImpl<>();
    instance.adiciona(valor);    
    instance.removePorId(valor.getChave());
    Optional<ValorSemDependente> resultado = instance.consultaPorId(valor.getChave());
    assertTrue(resultado.isEmpty());
  }  

  @Test
  public void testRemoveValorNaoExiste() throws Exception {
    TabelaImpl<Integer, ValorSemDependente> instance = new TabelaImpl<>();
    BancoDadosException excecao = assertThrows(BancoDadosException.class, () -> instance.removePorId(0));
    assertEquals("A chave 0 não existe na tabela.", excecao.getMessage());
  }
  
  @Test
  public void testRemoveChaveNula() throws Exception {
    TabelaImpl<Integer, ValorSemDependente> instance = new TabelaImpl<>();
    NullPointerException excecao = assertThrows(NullPointerException.class, () -> instance.removePorId(null));
    assertEquals("A chave não pode ser nula.", excecao.getMessage());
  }  

}
