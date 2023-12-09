package br.edu.infnet.tecnologiajava.controller;

import br.edu.infnet.tecnologiajava.model.domain.Pedido;
import br.edu.infnet.tecnologiajava.model.mapper.PedidoMapper;
import br.edu.infnet.tecnologiajava.model.view.RespostaInclusao;
import br.edu.infnet.tecnologiajava.model.view.RespostaSucesso;
import br.edu.infnet.tecnologiajava.repository.RepositorioPedido;
import br.edu.infnet.tecnologiajava.services.bancodados.BancoDadosException;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pedido")
public class PedidoController {

    private final RepositorioPedido repositorioPedido;

    private Logger logger = LoggerFactory.getLogger(RepositorioPedido.class);

    public PedidoController(RepositorioPedido repositorioPedido) {
        this.repositorioPedido = repositorioPedido;
    }

    @GetMapping(value = "/{chave}", produces = "application/json")
    public ResponseEntity<Pedido> getPedidoPorChave(@PathVariable int chave) throws BancoDadosException {
        Optional<Pedido> optionalPedido = repositorioPedido.consultaPorId(chave);
        return ResponseEntity.of(optionalPedido);
    }

    @GetMapping(produces = "application/json")
    public List<Pedido> getAllPedido() throws BancoDadosException {
        return repositorioPedido.getValores();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RespostaInclusao<Integer> addPedido(@RequestBody JsonNode requestBody) throws BancoDadosException {
        PedidoMapper pedidoMapper = new PedidoMapper();
        pedidoMapper.reset();
        pedidoMapper.setValores(requestBody);
        Pedido pedido = pedidoMapper.build();
        repositorioPedido.adiciona(pedido);
        Pedido pedidoBanco = repositorioPedido.consultaPorId(pedido.getChave()).orElseThrow();
        logger.info("Pedido adicionado: {}.", pedidoBanco);
        return new RespostaInclusao<>(pedido.getChave());
    }

    @DeleteMapping(value = "/{chave}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RespostaSucesso deleteProdutoPorChave(@PathVariable int chave) throws BancoDadosException {
        repositorioPedido.removePorId(chave);
        logger.info("Apagado pedido {} com sucesso.", chave);
        return new RespostaSucesso("O pedido com chave " + chave + " foi apagado com sucesso.");
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RespostaSucesso updatePedido(@RequestBody Pedido pedido) throws BancoDadosException {
        repositorioPedido.altera(pedido);
        Pedido pedidoBanco = repositorioPedido.consultaPorId(pedido.getChave()).orElseThrow();
        logger.info("Pedido alterado: {}.", pedidoBanco);
        return new RespostaSucesso("O pedido com chave " + pedido.getChave() + " foi alterado com sucesso.");
    }
}
