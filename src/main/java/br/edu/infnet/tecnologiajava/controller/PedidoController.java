package br.edu.infnet.tecnologiajava.controller;

import br.edu.infnet.tecnologiajava.model.domain.Pedido;
import br.edu.infnet.tecnologiajava.repository.RepositorioPedido;
import br.edu.infnet.tecnologiajava.services.bancodados.BancoDadosException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pedido")
public class PedidoController {

    private final RepositorioPedido repositorioPedido;

    public PedidoController(RepositorioPedido repositorioPedido){
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
}
