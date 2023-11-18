package br.edu.infnet.tecnologiajava.services.csv;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class CSVMapperAbstrato<T> implements CSVMapper<T>{

    private Set<String> camposSetados;
    private final String[] camposObrigatorios;

    protected CSVMapperAbstrato(String[] camposObrigatorios){
        Objects.requireNonNull(camposObrigatorios, "Os campos não podem ser nulo.");        
        this.camposObrigatorios = camposObrigatorios;
    }

    @Override
    public void reset(){
        camposSetados = new HashSet<>();
    }

    protected void adicionaCampoSetado(String campo) throws CSVMapperException{
        if(camposSetados==null){
            throw new CSVMapperException("O método reset não foi chamado.");
        }
        if(camposSetados.contains(campo)){
            throw new CSVMapperException("O campo "+campo+" já foi setado.");
        }
        camposSetados.add(campo);
    }

    protected void verificaTodosCamposSetatos() throws CSVMapperException{
        if(!camposSetados.containsAll(Arrays.asList(camposObrigatorios))){
            String camposFaltantes = Arrays.stream(camposObrigatorios)
                .filter((campo) -> !camposSetados.contains(campo))
                .collect(Collectors.joining(", "));
            if(camposFaltantes.indexOf(",")>0){    
                throw new CSVMapperException("Os seguintes campos não foram setados: "+camposFaltantes+".");  
            } else {
                throw new CSVMapperException("O campo "+camposFaltantes+" não foi setado.");
            } 
        }
    }

    protected void finaliza(){
        camposSetados = null;
    }
    
}
