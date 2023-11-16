package br.edu.infnet.tecnologiajava.model.mapper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import br.edu.infnet.tecnologiajava.model.domain.Sobremesa;
import br.edu.infnet.tecnologiajava.model.domain.ValidadorException;
import br.edu.infnet.tecnologiajava.services.csv.CSVMapper;
import br.edu.infnet.tecnologiajava.services.csv.CSVMapperException;

public class SobremesaMapper implements CSVMapper<Sobremesa>{

    private String nome;
    private String informacao;
    private float valor;
    private float quantidade;
    private boolean doce;

    private Set<String> camposSetados;
    private static final String[] camposSobremesa = {"nome", "informacao", "valor", "quantidade", "doce"};

    @Override
    public void reset() {
        camposSetados = new HashSet<>();
    }

    @Override
    public void setValor(String campo, String valorComoString) throws CSVMapperException {
        if(camposSetados.contains(campo)){
            throw new CSVMapperException("O campo "+campo+" já foi setado.");
        }
        
        switch (campo) {
            case "nome" -> nome=valorComoString;
            case "informacao" -> informacao=valorComoString;
            case "valor" -> valor = converteFloat(valorComoString);
            case "quantidade" -> quantidade = converteFloat(valorComoString);
            case "doce" -> doce = converteBoolean(valorComoString);
            default -> throw new CSVMapperException("Campo "+campo+" não é válido");
        }
        camposSetados.add(campo);
    }

    @Override
    public Sobremesa build() throws CSVMapperException {
        if(!camposSetados.containsAll(Arrays.asList(camposSobremesa))){
            String camposFaltantes = Arrays.stream(camposSobremesa)
                .filter((campo) -> !camposSetados.contains(campo))
                .collect(Collectors.joining(", "));
            if(camposFaltantes.indexOf(",")>0){    
                throw new CSVMapperException("Os seguintes campos não foram setados: "+camposFaltantes+".");  
            } else {
                throw new CSVMapperException("O campo "+camposFaltantes+" não foi setado.");
            } 
        }
        try{
            return new Sobremesa(nome, doce, informacao, quantidade, valor);
        }catch(ValidadorException ex){
            throw new CSVMapperException("Informação mapeada inválida", ex); 
        }
    }
    
}
