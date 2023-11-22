package br.edu.infnet.tecnologiajava.repository;

import br.edu.infnet.tecnologiajava.ExcecaoInesperada;
import br.edu.infnet.tecnologiajava.model.domain.Solicitante;
import br.edu.infnet.tecnologiajava.services.bancodados.BancoDadosException;
import br.edu.infnet.tecnologiajava.services.bancodados.TabelaDependente;

public class RepositorioSolicitante extends TabelaDependente<String, Solicitante> {
    private static RepositorioSolicitante instance;

    private RepositorioSolicitante() throws ExcecaoInesperada {
        super("solicitante");
        try {
            this.adiciona(Solicitante.getVazio());
        } catch (BancoDadosException e) {
            throw new ExcecaoInesperada("Esta exceção não deveria nunca ocorrer.", e);
        }
    }

    public static RepositorioSolicitante getInstance() throws BancoDadosException {
        if (instance == null) {
            throw new BancoDadosException("Repositório solicitante não foi inicializado.");
        }
        return instance;
    }

    static void criaRepositorio() {
        instance = new RepositorioSolicitante();
    }
}
