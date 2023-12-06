package br.edu.infnet.tecnologiajava.controller;

import br.edu.infnet.tecnologiajava.model.domain.*;
import br.edu.infnet.tecnologiajava.model.mapper.SobremesaMapper;
import br.edu.infnet.tecnologiajava.model.view.RespostaInclusao;
import br.edu.infnet.tecnologiajava.repository.RepositorioProduto;
import br.edu.infnet.tecnologiajava.services.bancodados.BancoDadosException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/{chave}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Produto> getProdutoPorChave(@PathVariable int chave) throws BancoDadosException {
        Optional<Produto> optionalProduto = repositorioProduto.consultaPorId(chave);
        return ResponseEntity.of(optionalProduto);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Produto> getAllProduto() throws BancoDadosException {
        return repositorioProduto.getValores();
    }

    @GetMapping(value = "/sobremesa", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Produto> getAllSobremesa() throws BancoDadosException {
        return getProdutoPorClasse(Sobremesa.class);
    }

    @GetMapping(value = "/bebida", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Produto> getAllBebida() throws BancoDadosException {
        return getProdutoPorClasse(Bebida.class);
    }

    @GetMapping(value = "/comida", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Produto> getAllComida() throws BancoDadosException {
        return getProdutoPorClasse(Comida.class);
    }

    private List<Produto> getProdutoPorClasse(Class<? extends Produto> classe) throws BancoDadosException {
        Predicate<Produto> condicao = produto -> produto.getClass().equals(classe);
        return repositorioProduto.getValores(condicao);
    }

    @GetMapping(value = "/busca/{palavrasJuntas}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Produto> buscaProduto(@PathVariable String palavrasJuntas) throws BancoDadosException {
        String[] palavras = palavrasJuntas.split("[ _]");
        return repositorioProduto.buscaPorTexto(palavras);
    }

    @PostMapping(path = "sobremesa",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RespostaInclusao<Integer> addSobremesa(@RequestBody JsonNode requestBody) throws BancoDadosException{
        SobremesaMapper sobremesaMapper = new SobremesaMapper();
        sobremesaMapper.reset();
        sobremesaMapper.setValores(requestBody);
        Produto sobremesa = sobremesaMapper.build();
        repositorioProduto.adiciona(sobremesa);
        return new RespostaInclusao<>(sobremesa.getChave());
    }

}
