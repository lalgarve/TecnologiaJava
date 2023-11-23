package br.edu.infnet.tecnologiajava.services.bancodados;

/**
 * Interface implementada por classes que serão salvas no banco de dados.
 *
 * @author leila
 */
public interface ValorBD<C, V> {
    C getChave();

    /**
     * Retorna uma instância do objeto que não permita
     * que os campos da instância original sejam alterados.
     *
     * @return a cópia segura
     */
    V criaInstanciaCopiaSegura();

    boolean podeSerGravadoNoBanco();
}
