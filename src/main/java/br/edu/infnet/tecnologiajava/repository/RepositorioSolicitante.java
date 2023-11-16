package br.edu.infnet.tecnologiajava.repository;

import br.edu.infnet.tecnologiajava.model.domain.Solicitante;
import br.edu.infnet.tecnologiajava.services.bancodados.TabelaDependente;

public class RepositorioSolicitante extends TabelaDependente<String, Solicitante> {
    private static RepositorioSolicitante instance = new RepositorioSolicitante();
    
    private RepositorioSolicitante(){
        super("solicitante");
    }

    public static RepositorioSolicitante getInstance(){
        return instance;
    }
}
