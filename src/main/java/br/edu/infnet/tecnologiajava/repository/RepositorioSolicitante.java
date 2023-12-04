package br.edu.infnet.tecnologiajava.repository;

import br.edu.infnet.tecnologiajava.model.domain.Solicitante;
import br.edu.infnet.tecnologiajava.services.bancodados.TabelaDependente;

public class RepositorioSolicitante extends TabelaDependente<String, Solicitante> {
    public RepositorioSolicitante()  {
        super("solicitante");
    }

}
