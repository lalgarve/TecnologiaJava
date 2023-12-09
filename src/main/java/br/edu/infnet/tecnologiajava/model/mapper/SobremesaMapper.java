package br.edu.infnet.tecnologiajava.model.mapper;

import br.edu.infnet.tecnologiajava.model.domain.Produto;
import br.edu.infnet.tecnologiajava.model.domain.Sobremesa;
import br.edu.infnet.tecnologiajava.ValidadorException;
import br.edu.infnet.tecnologiajava.services.mapper.MapperAbstrato;
import br.edu.infnet.tecnologiajava.services.mapper.MapperException;
import br.edu.infnet.tecnologiajava.services.mapper.json.JsonMapper;

public class SobremesaMapper extends MapperAbstrato<Produto> implements JsonMapper<Produto> {

    private String nome;
    private String informacao;
    private float valor;
    private float quantidade;
    private boolean doce;

    public SobremesaMapper() {
        super(new String[]{"nome", "informacao", "valor", "quantidade", "doce"});
    }


    @Override
    public void setValor(String campo, String valorComoString) throws MapperException {
        super.adicionaCampoSetado(campo);
        switch (campo) {
            case "nome" -> nome = valorComoString;
            case "informacao" -> informacao = valorComoString;
            case "valor" -> valor = converteFloat(valorComoString);
            case "quantidade" -> quantidade = converteFloat(valorComoString);
            case "doce" -> doce = converteBoolean(valorComoString);
            default -> throw new MapperException("O campo " + campo + " não existe.");
        }
    }

    @Override
    public Produto build() throws MapperException {
        super.verificaTodosCamposSetatos();
        try {
            super.finaliza();
            return new Sobremesa(nome, doce, informacao, quantidade, valor);
        } catch (ValidadorException ex) {
            throw new MapperException("Informação mapeada inválida.", ex);
        }
    }


}
