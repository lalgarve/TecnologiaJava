package br.edu.infnet.tecnologiajava.services.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Stream;

public class CSVReader<T> implements AutoCloseable{
  
  private final BufferedReader reader;
  private final CSVMapper<T> mapper;

  public CSVReader(Reader reader, CSVMapper<T> mapper) {
    this.reader = new BufferedReader(reader);
    this.mapper = mapper;
  }
  
  public Stream<T> leDados() throws IOException, CSVMapperException{
    try{
      Stream<String> lines = reader.lines();
      List<String> cabecalho = leCabecalho(lines.findFirst().orElse(null));
      lines = reader.lines();
      return lines.map((linha)->leCampos(cabecalho, linha));
    }
    catch(UncheckedIOException ex){
      throw new IOException(ex.getMessage(),ex);
    }
    catch(UncheckedCSVMapperException ex){
      throw new CSVMapperException(ex.getMessage(),ex);
    }
  }
  
  

  @Override
  public void close() throws IOException{
    reader.close();
  }

  private List<String> leCabecalho(String linha) {
    if(linha==null){
      return Collections.EMPTY_LIST;
    } else {
      return Arrays.asList(linha.split(","));
    }
  }

  private T leCampos(List<String> cabecalho, String linha) {
    ListIterator<String> iteratorCabecalho = cabecalho.listIterator();
    List<String> valores = Arrays.asList(linha.split(","));
    mapper.reset();
    try {
      while (iteratorCabecalho.hasNext()) {
        String valor = valores.get(iteratorCabecalho.nextIndex());
        String campo = iteratorCabecalho.next();
        mapper.setValor(campo, valor);        
      }
      return mapper.build();
    } catch (CSVMapperException ex) {
      throw new UncheckedCSVMapperException(ex);
    }
  }

  
}
