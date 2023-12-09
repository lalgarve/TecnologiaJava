package br.edu.infnet.tecnologiajava.repository;

import br.edu.infnet.tecnologiajava.services.bancodados.BancoDadosException;

import java.text.Normalizer;
import java.util.List;
import java.util.Objects;

public interface BuscaTexto<T> {
    /**
     * O texto é normalizado antes da busca ser realizada.
     */
    List<T> buscaPorTexto(String[] palavras) throws BancoDadosException;

    /**
     * @see <a href="https://https://www.baeldung.com/java-remove-accents-from-text/">Remove Accents and Diacritics From a String in Java</a>
     */
    default String normalizaTexto(String texto) {
        Objects.requireNonNull(texto, "Texto não pode ser null");
        String normalized = Normalizer.normalize(texto.toLowerCase(), Normalizer.Form.NFKD);
        return normalized.replaceAll("\\p{M}", "");
    }

}
