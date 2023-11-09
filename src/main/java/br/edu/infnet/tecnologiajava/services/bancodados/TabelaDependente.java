package br.edu.infnet.tecnologiajava.services.bancodados;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Usa o padrão decorator pois os valores são encapsulados para conseguir
 * saber se o valor está sendo referenciado por outra tabela ou não.
 * 
 * @author leila
 * @param <C> Classe da chave dos valores armazenados na tabela
 * @param <V> Classe dos valores armazenados na tabela
 */
public class TabelaDependente<C, V extends ValorBD<C>> implements TabelaBD<C,V> {
  private final String nome;
  private final TabelaImpl<C, DecoradorValor> tabelaImpl;
  
  public TabelaDependente(String nome){
    this.nome = nome;
    this.tabelaImpl = new TabelaImpl<>(nome);
  }

  @Override
  public void adiciona(V valor) throws BancoDadosException{
    tabelaImpl.adiciona(valor==null?null:new DecoradorValor(valor));
  }

  @Override
  public void removePorId(C chave) throws BancoDadosException{
    tabelaImpl.removePorId(chave);
  }

  @Override
  public void altera(V valor) throws BancoDadosException{
    tabelaImpl.altera(valor==null?null:new DecoradorValor(valor));
  }

  @Override
  public Optional<V> consultaPorId(C chave) throws BancoDadosException{
    return tabelaImpl.consultaPorId(chave).map((decorador) -> decorador.valor);
  }

  @Override
  public String getNome() {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }
  
  public void adicionaRelacao(TabelaBD tabela) throws BancoDadosException{
    
  }
  
  public void adicionaUso(C chave, TabelaBD tabela) throws BancoDadosException{
    
  }
  
  public void removeUso(C chave, TabelaBD tabela){
    
  }

  @Override
  public List<V> getValores() throws BancoDadosException {
    return tabelaImpl.getValores().stream()
                     .map((decorator) -> decorator.valor).toList();
  }

  @Override
  public List<V> getValores(Predicate<V> filtro) throws BancoDadosException {
     return tabelaImpl.getValores((decorador)->filtro.test(decorador.valor)).stream()
                     .map((decorator) -> decorator.valor).toList();   
  }
  
  private class DecoradorValor implements ValorBD<C>{
    private final V valor;
    
    DecoradorValor(V valor){
      this.valor = valor;
    }
    
    @Override
    public C getChave() {
      return valor.getChave();
    }

    @Override
    public ValorBD<C> getClone() {
      V clone = (V) valor.getClone();
      return new DecoradorValor(clone);
    }

    
  }
  
}
