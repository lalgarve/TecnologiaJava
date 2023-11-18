package br.edu.infnet.tecnologiajava.model.mapper;

import br.edu.infnet.tecnologiajava.model.domain.Sobremesa;
import br.edu.infnet.tecnologiajava.model.domain.ValidadorException;
import br.edu.infnet.tecnologiajava.services.csv.CSVMapperAbstrato;
import br.edu.infnet.tecnologiajava.services.csv.CSVMapperException;

public class SobremesaMapper extends CSVMapperAbstrato<Sobremesa>{

    private String nome;
    private String informacao;
    private float valor;
    private float quantidade;
    private boolean doce;

    public SobremesaMapper(){
        super(new String[]{"nome", "informacao", "valor", "quantidade", "doce"});
    }


    @Override
    public void setValor(String campo, String valorComoString) throws CSVMapperException {
        super.adicionaCampoSetado(campo);
        switch (campo) {
            case "nome" -> nome=valorComoString;
            case "informacao" -> informacao=valorComoString;
            case "valor" -> valor = converteFloat(valorComoString);
            case "quantidade" -> quantidade = converteFloat(valorComoString);
            case "doce" -> doce = converteBoolean(valorComoString);
            default -> throw new CSVMapperException("O campo "+campo+" não existe.");
        }
    }

    @Override
    public Sobremesa build() throws CSVMapperException {
        super.verificaTodosCamposSetatos();
        try{
            super.finaliza();
            return new Sobremesa(nome, doce, informacao, quantidade, valor);
        }catch(ValidadorException ex){
            throw new CSVMapperException("Informação mapeada inválida.", ex); 
        }
    }
    
}
