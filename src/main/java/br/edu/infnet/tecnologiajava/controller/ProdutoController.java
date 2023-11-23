package br.edu.infnet.tecnologiajava.controller;

import br.edu.infnet.tecnologiajava.model.domain.Bebida;
import br.edu.infnet.tecnologiajava.model.domain.Comida;
import br.edu.infnet.tecnologiajava.model.domain.Produto;
import br.edu.infnet.tecnologiajava.model.domain.Sobremesa;
import br.edu.infnet.tecnologiajava.repository.RepositorioProduto;
import br.edu.infnet.tecnologiajava.services.bancodados.BancoDadosException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @GetMapping(value = "/{chave}", produces = "application/json")
    public ResponseEntity<Produto> getProduto(@PathVariable int chave) throws BancoDadosException {
        RepositorioProduto repositorioProduto = RepositorioProduto.getInstance();
        Optional<Produto> optionalProduto = repositorioProduto.consultaPorId(chave);
        return ResponseEntity.of(optionalProduto);
    }

    @GetMapping(produces = "application/json")
    public List<Produto> getProdutos() throws BancoDadosException {
        RepositorioProduto repositorioProduto = RepositorioProduto.getInstance();
        return repositorioProduto.getValores();
    }

    @GetMapping(value = "/sobremesas", produces = "application/json")
    public List<Produto> getSobremesas() throws BancoDadosException {
        return getProdutoPorClasse(Sobremesa.class);
    }

    @GetMapping(value = "/bebidas", produces = "application/json")
    public List<Produto> getBebidas() throws BancoDadosException {
        return getProdutoPorClasse(Bebida.class);
    }

    @GetMapping(value = "/comidas", produces = "application/json")
    public List<Produto> getComidas() throws BancoDadosException {
        return getProdutoPorClasse(Comida.class);
    }

    private List<Produto> getProdutoPorClasse(Class<? extends Produto> classe) throws BancoDadosException {
        Predicate<Produto> condicao = produto -> produto.getClass().equals(classe);
        RepositorioProduto repositorioProduto = RepositorioProduto.getInstance();
        return repositorioProduto.getValores(condicao);
    }
}
