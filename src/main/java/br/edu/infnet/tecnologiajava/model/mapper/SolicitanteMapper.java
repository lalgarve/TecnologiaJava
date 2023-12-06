package br.edu.infnet.tecnologiajava.model.mapper;

import br.edu.infnet.tecnologiajava.model.domain.Solicitante;
import br.edu.infnet.tecnologiajava.model.domain.ValidadorException;
import br.edu.infnet.tecnologiajava.services.mapper.MapperAbstrato;
import br.edu.infnet.tecnologiajava.services.mapper.MapperException;

public class SolicitanteMapper extends MapperAbstrato<Solicitante> {

    private String cpf;
    private String nome;
    private String email;

    public SolicitanteMapper() {
        super(new String[]{"cpf", "nome", "email"});
    }

    @Override
    public void setValor(String campo, String valorComoString) throws MapperException {
        adicionaCampoSetado(campo);
        switch (campo) {
            case "nome" -> nome = valorComoString;
            case "cpf" -> cpf = valorComoString;
            case "email" -> email = valorComoString;
            default -> throw new MapperException("O campo " + campo + " não existe.");
        }
    }

    @Override
    public Solicitante build() throws MapperException {
        verificaTodosCamposSetatos();
        try {
            finaliza();
            return new Solicitante(cpf, nome, email);
        } catch (ValidadorException ex) {
            throw new MapperException("Informação mapeada inválida.", ex);
        }
    }

}
