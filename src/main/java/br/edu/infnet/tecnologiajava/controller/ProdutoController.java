package br.edu.infnet.tecnologiajava.controller;

import br.edu.infnet.tecnologiajava.model.domain.*;
import br.edu.infnet.tecnologiajava.model.mapper.BebidaMapper;
import br.edu.infnet.tecnologiajava.model.mapper.SobremesaMapper;
import br.edu.infnet.tecnologiajava.model.view.RespostaInclusao;
import br.edu.infnet.tecnologiajava.model.view.RespostaSucesso;
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

    private final RepositorioProduto repositorioProduto;

    public ProdutoController(RepositorioProduto repositorioProduto){
        this.repositorioProduto = repositorioProduto;
    }

    @GetMapping(value = "/{chave}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Produto> getProdutoPorChave(@PathVariable int chave) throws BancoDadosException {
        Optional<Produto> optionalProduto = repositorioProduto.consultaPorId(chave);
        return ResponseEntity.of(optionalProduto);
    }

    @DeleteMapping(value = "/{chave}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RespostaSucesso deleteProdutoPorChave(@PathVariable int chave) throws BancoDadosException {
        repositorioProduto.removePorId(chave);
        return new RespostaSucesso("O produto com chave "+chave+" foi apagado com sucesso.");
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

    @PostMapping(path = "/sobremesa",
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

    @PutMapping(path = "/sobremesa",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RespostaSucesso updateSobremesa(@RequestBody Sobremesa sobremesa) throws BancoDadosException{
        repositorioProduto.altera(sobremesa);
        return new RespostaSucesso("O produto com chave " + sobremesa.getChave()+" foi alterado para sobremesa com sucesso.");
    }

    @PostMapping(path = "/bebida",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RespostaInclusao<Integer> addBebida(@RequestBody JsonNode requestBody) throws BancoDadosException{
        BebidaMapper bebidaMapper = new BebidaMapper();
        bebidaMapper.reset();
        bebidaMapper.setValores(requestBody);
        Produto bebida = bebidaMapper.build();
        repositorioProduto.adiciona(bebida);
        return new RespostaInclusao<>(bebida.getChave());
    }

    @PutMapping(path = "/bebida",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RespostaSucesso updateBebida(@RequestBody Bebida bebida) throws BancoDadosException{
        repositorioProduto.altera(bebida);
        return new RespostaSucesso("O produto com chave " + bebida.getChave()+" foi alterado para bebida com sucesso.");
    }

}
