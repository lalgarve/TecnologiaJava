package br.edu.infnet.tecnologiajava.model.mapper;

import br.edu.infnet.tecnologiajava.model.domain.Comida;
import br.edu.infnet.tecnologiajava.model.domain.Produto;
import br.edu.infnet.tecnologiajava.model.domain.ValidadorException;
import br.edu.infnet.tecnologiajava.services.csv.CSVMapper;
import br.edu.infnet.tecnologiajava.services.csv.CSVMapperAbstrato;
import br.edu.infnet.tecnologiajava.services.csv.CSVMapperException;

public class ComidaMapper extends CSVMapperAbstrato<Produto>{

    private String nome;
    private String ingredientes;
    private float peso;
    private float valor;
    private boolean vegano;

    public ComidaMapper(){
        super(new String[] {"nome", "ingredientes", "peso", "valor", "vegano"});
    }

    @Override
    public void setValor(String campo, String valorComoString) throws CSVMapperException {
        adicionaCampoSetado(campo);        
        switch (campo) {
            case "nome" -> nome=valorComoString;
            case "ingredientes" -> ingredientes=valorComoString;
            case "valor" -> valor = converteFloat(valorComoString);
            case "peso" -> peso = converteFloat(valorComoString);
            case "vegano" -> vegano = converteBoolean(valorComoString);
            default -> throw new CSVMapperException("O campo "+campo+" não existe.");
        }
    }

    @Override
    public Produto build() throws CSVMapperException {
        verificaTodosCamposSetatos();
        try{
            finaliza();
            return new Comida(nome, ingredientes, peso, vegano, valor);
        }catch(ValidadorException ex){
            throw new CSVMapperException("Informação mapeada inválida.", ex); 
        }
    }
    
}
