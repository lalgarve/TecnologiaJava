package br.edu.infnet.tecnologiajava.controller;

import br.edu.infnet.tecnologiajava.model.domain.Solicitante;
import br.edu.infnet.tecnologiajava.model.view.RespostaInclusao;
import br.edu.infnet.tecnologiajava.model.view.RespostaSucesso;
import br.edu.infnet.tecnologiajava.repository.RepositorioSolicitante;
import br.edu.infnet.tecnologiajava.services.bancodados.BancoDadosException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping(value = "/{chave}", produces = "application/json")
    public RespostaSucesso deleteSolicitantePorChave(@PathVariable String chave) throws BancoDadosException {
        this.repositorioSolicitante.removePorId(chave);
        return new RespostaSucesso("O solicitante com o cpf "+chave+" foi apagado com sucesso.");
    }

    @GetMapping(produces = "application/json")
    public List<Solicitante> getAllSolicitante() throws BancoDadosException {
        return repositorioSolicitante.getValores();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RespostaInclusao<String> addSolicitante(@RequestBody Solicitante solicitante) throws BancoDadosException {
        repositorioSolicitante.adiciona(solicitante);
        return new RespostaInclusao<>(solicitante.getChave());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RespostaSucesso updateSolicitante(@RequestBody Solicitante solicitante) throws BancoDadosException {
        repositorioSolicitante.altera( solicitante);
        return new RespostaSucesso("Solicitante com cpf "+solicitante.getChave() + " alterado com sucesso.");
    }
}
