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
@RequestMapping("/pedidos")
public class PedidoController {

    @GetMapping(value = "/{chave}", produces = "application/json")
    public ResponseEntity<Pedido> getPedido(@PathVariable int chave) throws BancoDadosException {
        RepositorioPedido repositorioPedido = RepositorioPedido.getInstance();
        Optional<Pedido> optionalPedido = repositorioPedido.consultaPorId(chave);
        return ResponseEntity.of(optionalPedido);
    }

    @GetMapping(produces = "application/json")
    public List<Pedido> getPedidos() throws BancoDadosException {
        RepositorioPedido repositorioPedido = RepositorioPedido.getInstance();
        return repositorioPedido.getValores();
    }
}
