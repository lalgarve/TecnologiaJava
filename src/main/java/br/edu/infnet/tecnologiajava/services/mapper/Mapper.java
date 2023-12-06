package br.edu.infnet.tecnologiajava.services.mapper;

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
public interface Mapper<T> {
    void reset();

    void setValor(String campo, String valorComoString) throws MapperException;

    T build() throws MapperException;

    default float converteFloat(String valorComoString) throws MapperException {
        try {
            return Float.parseFloat(valorComoString);
        } catch (NumberFormatException ex) {
            throw new MapperException(valorComoString + " não é um número ponto flutuante.", ex);
        }
    }


    default int converteInt(String valorComoString) throws MapperException {
        try {
            return Integer.parseInt(valorComoString);
        } catch (NumberFormatException ex) {
            throw new MapperException(valorComoString + " não é um número inteiro.", ex);
        }
    }

    default boolean converteBoolean(String valorComoString) throws MapperException {
        if ("true".compareToIgnoreCase(valorComoString) == 0) {
            return true;
        }
        if ("false".compareToIgnoreCase(valorComoString) == 0) {
            return false;
        }
        throw new MapperException(valorComoString + " não é um valor booleano.");
    }

    default LocalDateTime converteData(String valorComoString) throws MapperException {
        LocalDateTime localDateTime;
        try {
            localDateTime = LocalDateTime.parse(valorComoString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (DateTimeParseException e) {
            throw new MapperException(valorComoString + " não está no formato ano-mês-diaThora:minuto, por exemplo: 2023-01-10T13:30.");
        }

        return localDateTime;
    }
}
