package br.edu.infnet.tecnologiajava.services.csv;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Interface para realizar o mapeamento dos campos de um arquivo CSV
 * para a criação de um objeto.
 *
 * @param <T> Classe do objeto sendo mapeado
 * @author leila
 */
public interface CSVMapper<T> {
    void reset();

    void setValor(String campo, String valorComoString) throws CSVMapperException;

    T build() throws CSVMapperException;

    default float converteFloat(String valorComoString) throws CSVMapperException {
        try {
            return Float.parseFloat(valorComoString);
        } catch (NumberFormatException ex) {
            throw new CSVMapperException(valorComoString + " não é um número ponto flutuante.", ex);
        }
    }


    default int converteInt(String valorComoString) throws CSVMapperException {
        try {
            return Integer.parseInt(valorComoString);
        } catch (NumberFormatException ex) {
            throw new CSVMapperException(valorComoString + " não é um número inteiro.", ex);
        }
    }

    default boolean converteBoolean(String valorComoString) throws CSVMapperException {
        if ("true".compareToIgnoreCase(valorComoString) == 0) {
            return true;
        }
        if ("false".compareToIgnoreCase(valorComoString) == 0) {
            return false;
        }
        throw new CSVMapperException(valorComoString + " não é um valor booleano.");
    }

    default LocalDateTime converteData(String valorComoString) throws CSVMapperException {
        LocalDateTime localDateTime;
        try {
            localDateTime = LocalDateTime.parse(valorComoString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (DateTimeParseException e) {
            throw new CSVMapperException(valorComoString + " não está no formato ano-mês-diaThora:minuto, por exemplo: 2023-01-10T13:30.");
        }

        return localDateTime;
    }
}
