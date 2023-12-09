package br.edu.infnet.tecnologiajava.model.mapper;

import br.edu.infnet.tecnologiajava.model.domain.Bebida;
import br.edu.infnet.tecnologiajava.model.domain.Produto;
import br.edu.infnet.tecnologiajava.ValidadorException;
import br.edu.infnet.tecnologiajava.services.mapper.MapperAbstrato;
import br.edu.infnet.tecnologiajava.services.mapper.MapperException;
import br.edu.infnet.tecnologiajava.services.mapper.json.JsonMapper;

public class BebidaMapper extends MapperAbstrato<Produto> implements JsonMapper<Produto> {

    private String nome;
    private String marca;
    private float tamanho;
    private float valor;
    private boolean gelada;

    public BebidaMapper() {
        super(new String[]{"nome", "marca", "valor", "tamanho", "gelada"});
    }


    @Override
    public void setValor(String campo, String valorComoString) throws MapperException {
        adicionaCampoSetado(campo);
        switch (campo) {
            case "nome" -> nome = valorComoString;
            case "marca" -> marca = valorComoString;
            case "valor" -> valor = converteFloat(valorComoString);
            case "tamanho" -> tamanho = converteFloat(valorComoString);
            case "gelada" -> gelada = converteBoolean(valorComoString);
            default -> throw new MapperException("O campo " + campo + " não existe.");
        }
    }

    @Override
    public Produto build() throws MapperException {
        verificaTodosCamposSetatos();
        try {
            finaliza();
            return new Bebida(nome, marca, tamanho, gelada, valor);
        } catch (ValidadorException ex) {
            throw new MapperException("Informação mapeada inválida.", ex);
        }
    }

}
