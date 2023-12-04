package br.edu.infnet.tecnologiajava.controller;

import br.edu.infnet.tecnologiajava.model.domain.Solicitante;
import br.edu.infnet.tecnologiajava.repository.RepositorioSolicitante;
import br.edu.infnet.tecnologiajava.services.bancodados.BancoDadosException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/solicitante")
public class SolicitanteController {
    private final RepositorioSolicitante repositorioSolicitante;

    public SolicitanteController(RepositorioSolicitante repositorioSolicitante){
        this.repositorioSolicitante = repositorioSolicitante;
    }

    @GetMapping(value = "/{chave}", produces = "application/json")
    public ResponseEntity<Solicitante> getSolicitantePorChave(@PathVariable String chave) throws BancoDadosException {
        Optional<Solicitante> optionalSolicitante = repositorioSolicitante.consultaPorId(chave);
        return ResponseEntity.of(optionalSolicitante);
    }

    @GetMapping(produces = "application/json")
    public List<Solicitante> getAllSolicitante() throws BancoDadosException {
        return repositorioSolicitante.getValores();
    }
}
