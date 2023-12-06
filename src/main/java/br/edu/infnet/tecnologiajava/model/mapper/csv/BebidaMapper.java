package br.edu.infnet.tecnologiajava.model.mapper.csv;

import br.edu.infnet.tecnologiajava.model.domain.Bebida;
import br.edu.infnet.tecnologiajava.model.domain.Produto;
import br.edu.infnet.tecnologiajava.model.domain.ValidadorException;
import br.edu.infnet.tecnologiajava.services.mapper.csv.CSVMapperAbstrato;
import br.edu.infnet.tecnologiajava.services.mapper.csv.CSVMapperException;

public class BebidaMapper extends CSVMapperAbstrato<Produto> {

    private String nome;
    private String marca;
    private float tamanho;
    private float valor;
    private boolean gelada;

    public BebidaMapper() {
        super(new String[]{"nome", "marca", "valor", "tamanho", "gelada"});
    }


    @Override
    public void setValor(String campo, String valorComoString) throws CSVMapperException {
        adicionaCampoSetado(campo);
        switch (campo) {
            case "nome" -> nome = valorComoString;
            case "marca" -> marca = valorComoString;
            case "valor" -> valor = converteFloat(valorComoString);
            case "tamanho" -> tamanho = converteFloat(valorComoString);
            case "gelada" -> gelada = converteBoolean(valorComoString);
            default -> throw new CSVMapperException("O campo " + campo + " não existe.");
        }
    }

    @Override
    public Produto build() throws CSVMapperException {
        verificaTodosCamposSetatos();
        try {
            finaliza();
            return new Bebida(nome, marca, tamanho, gelada, valor);
        } catch (ValidadorException ex) {
            throw new CSVMapperException("Informação mapeada inválida.", ex);
        }
    }

}
