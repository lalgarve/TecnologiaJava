package br.edu.infnet.tecnologiajava.services.csv;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * Interface para realizar o mapeamento dos campos de um arquivo CSV
 * para a criação de um objeto.
 * @author leila
 * @param <T> Classe do objeto sendo mapeado
 */
public interface CSVMapper<T> {
  void reset();
  void setValor(String campo, String valorComoString) throws CSVMapperException;
  T build() throws CSVMapperException;

  default float converteFloat(String valorComoString) throws CSVMapperException{
    try{
      return Float.parseFloat(valorComoString);
    }catch(NumberFormatException ex){
      throw new CSVMapperException(valorComoString + " não é um número ponto flutuante.", ex);
    }
  }

  
  default int converteInt(String valorComoString) throws CSVMapperException{
    try{
      return Integer.parseInt(valorComoString);
    }catch(NumberFormatException ex){
      throw new CSVMapperException(valorComoString + " não é um número inteiro.", ex);
    }
  }

  default boolean converteBoolean(String valorComoString) throws CSVMapperException {
    if("true".compareToIgnoreCase(valorComoString)==0){
      return true;
    }
    if("false".compareToIgnoreCase(valorComoString)==0){
      return false;
    }
    throw new CSVMapperException(valorComoString + " não é um valor booleano.");
  }

  default LocalDateTime converteData(String valorComoString) throws CSVMapperException {
    LocalDateTime localDateTime = null;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    try {
      localDateTime = LocalDateTime.parse(valorComoString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
    catch(DateTimeParseException e){
      throw new CSVMapperException(valorComoString + " não é uma data.");
    }

    return localDateTime;
  }
}
