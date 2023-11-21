package br.edu.infnet.tecnologiajava.services.bancodados;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @param <V> classe dos valores armazenados na tabela
 * @param <C> classe das chaves dos valores armazenados
 * @author leila
 */
public interface TabelaBD<C, V extends ValorBD<C, V>> {

    void adiciona(V valor) throws BancoDadosException;

    void removePorId(C chave) throws BancoDadosException;

    void altera(V valor) throws BancoDadosException;

    Optional<V> consultaPorId(C chave) throws BancoDadosException;

    List<V> getValores() throws BancoDadosException;

    List<V> getValores(Predicate<V> filtro) throws BancoDadosException;

    String getNome();

}
