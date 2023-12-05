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
@RequestMapping("/produto")
public class ProdutoController {

    private RepositorioProduto repositorioProduto;

    public ProdutoController(RepositorioProduto repositorioProduto){
        this.repositorioProduto = repositorioProduto;
    }

    @GetMapping(value = "/{chave}", produces = "application/json")
    public ResponseEntity<Produto> getProdutoPorChave(@PathVariable int chave) throws BancoDadosException {
        Optional<Produto> optionalProduto = repositorioProduto.consultaPorId(chave);
        return ResponseEntity.of(optionalProduto);
    }

    @GetMapping(produces = "application/json")
    public List<Produto> getAllProduto() throws BancoDadosException {
        return repositorioProduto.getValores();
    }

    @GetMapping(value = "/sobremesa", produces = "application/json")
    public List<Produto> getAllSobremesa() throws BancoDadosException {
        return getProdutoPorClasse(Sobremesa.class);
    }

    @GetMapping(value = "/bebida", produces = "application/json")
    public List<Produto> getAllBebida() throws BancoDadosException {
        return getProdutoPorClasse(Bebida.class);
    }

    @GetMapping(value = "/comida", produces = "application/json")
    public List<Produto> getAllComida() throws BancoDadosException {
        return getProdutoPorClasse(Comida.class);
    }

    private List<Produto> getProdutoPorClasse(Class<? extends Produto> classe) throws BancoDadosException {
        Predicate<Produto> condicao = produto -> produto.getClass().equals(classe);
        return repositorioProduto.getValores(condicao);
    }

    @GetMapping(value = "/busca/{palavrasJuntas}", produces = "application/json")
    public List<Produto> buscaProduto(@PathVariable String palavrasJuntas) throws BancoDadosException {
        String[] palavras = palavrasJuntas.split("[ _]");
        return repositorioProduto.buscaPorTexto(palavras);
    }
}
