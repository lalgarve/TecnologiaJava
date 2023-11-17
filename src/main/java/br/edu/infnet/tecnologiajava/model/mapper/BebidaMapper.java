package br.edu.infnet.tecnologiajava.model.mapper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import br.edu.infnet.tecnologiajava.model.domain.Bebida;
import br.edu.infnet.tecnologiajava.model.domain.ValidadorException;
import br.edu.infnet.tecnologiajava.services.csv.CSVMapper;
import br.edu.infnet.tecnologiajava.services.csv.CSVMapperException;

public class BebidaMapper implements CSVMapper<Bebida> {

    private String nome;
    private String marca;
    private float tamanho;
    private float valor;
    private boolean gelada;

    private Set<String> camposSetados;
    private static final String[] camposBebida = {"nome", "marca", "valor", "tamanho", "gelada"};

    @Override
    public void reset() {
        camposSetados = new HashSet<>();
    }

    @Override
    public void setValor(String campo, String valorComoString) throws CSVMapperException {
        if(camposSetados==null){
            throw new CSVMapperException("O método reset não foi chamado.");
        }
        if(camposSetados.contains(campo)){
            throw new CSVMapperException("O campo "+campo+" já foi setado.");
        }
        
        switch (campo) {
            case "nome" -> nome=valorComoString;
            case "marca" -> marca=valorComoString;
            case "valor" -> valor = converteFloat(valorComoString);
            case "tamanho" -> tamanho = converteFloat(valorComoString);
            case "gelada" -> gelada = converteBoolean(valorComoString);
            default -> throw new CSVMapperException("O campo "+campo+" não existe.");
        }
        camposSetados.add(campo);
    }

    @Override
    public Bebida build() throws CSVMapperException {
        if(!camposSetados.containsAll(Arrays.asList(camposBebida))){
            String camposFaltantes = Arrays.stream(camposBebida)
                .filter((campo) -> !camposSetados.contains(campo))
                .collect(Collectors.joining(", "));
            if(camposFaltantes.indexOf(",")>0){    
                throw new CSVMapperException("Os seguintes campos não foram setados: "+camposFaltantes+".");  
            } else {
                throw new CSVMapperException("O campo "+camposFaltantes+" não foi setado.");
            } 
        }
        try{
            camposSetados=null;
            return new Bebida(nome, marca, tamanho, gelada, valor);
        }catch(ValidadorException ex){
            throw new CSVMapperException("Informação mapeada inválida.", ex); 
        }
    }
    
}
