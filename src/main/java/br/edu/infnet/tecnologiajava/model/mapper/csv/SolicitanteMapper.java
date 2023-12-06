package br.edu.infnet.tecnologiajava.model.mapper.csv;

import br.edu.infnet.tecnologiajava.model.domain.Solicitante;
import br.edu.infnet.tecnologiajava.model.domain.ValidadorException;
import br.edu.infnet.tecnologiajava.services.mapper.csv.CSVMapperAbstrato;
import br.edu.infnet.tecnologiajava.services.mapper.csv.CSVMapperException;

public class SolicitanteMapper extends CSVMapperAbstrato<Solicitante> {

    private String cpf;
    private String nome;
    private String email;

    public SolicitanteMapper() {
        super(new String[]{"cpf", "nome", "email"});
    }

    @Override
    public void setValor(String campo, String valorComoString) throws CSVMapperException {
        adicionaCampoSetado(campo);
        switch (campo) {
            case "nome" -> nome = valorComoString;
            case "cpf" -> cpf = valorComoString;
            case "email" -> email = valorComoString;
            default -> throw new CSVMapperException("O campo " + campo + " não existe.");
        }
    }

    @Override
    public Solicitante build() throws CSVMapperException {
        verificaTodosCamposSetatos();
        try {
            finaliza();
            return new Solicitante(cpf, nome, email);
        } catch (ValidadorException ex) {
            throw new CSVMapperException("Informação mapeada inválida.", ex);
        }
    }

}
