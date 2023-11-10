package br.edu.infnet.tecnologiajava.services.bancodados;

/**
 * Interface implementada por classes que serão salvas no banco de dados.
 * @author leila
 */
public interface ValorBD<C>{
  C getChave();
  /**
   * Realiza um clone deep dos campos. Se a classe for imutável, pode retornar
   * o mesmo objeto.
   * 
   * @param todosCampos se estiver retornando uma lista com os objetos é falso,
   * se estiver retornando um único objeto é verdadeiro. Usado normalmente para 
   * evitar a cópia de coleções.
   * @return 
   */
  ValorBD<C> getDeepClone(boolean todosCampos);
}
