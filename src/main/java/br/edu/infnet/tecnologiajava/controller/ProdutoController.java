package br.edu.infnet.tecnologiajava.controller;

import br.edu.infnet.tecnologiajava.model.domain.Bebida;
import br.edu.infnet.tecnologiajava.model.domain.Comida;
import br.edu.infnet.tecnologiajava.model.domain.Produto;
import br.edu.infnet.tecnologiajava.model.domain.Sobremesa;
import br.edu.infnet.tecnologiajava.model.mapper.BebidaMapper;
import br.edu.infnet.tecnologiajava.model.mapper.ComidaMapper;
import br.edu.infnet.tecnologiajava.model.mapper.SobremesaMapper;
import br.edu.infnet.tecnologiajava.model.view.RespostaInclusao;
import br.edu.infnet.tecnologiajava.model.view.RespostaSucesso;
import br.edu.infnet.tecnologiajava.repository.RepositorioProduto;
import br.edu.infnet.tecnologiajava.services.bancodados.BancoDadosException;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

    private Logger logger = LoggerFactory.getLogger(ProdutoController.class);

    private final RepositorioProduto repositorioProduto;

    public ProdutoController(RepositorioProduto repositorioProduto) {
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
        logger.info("Apagado produto {} com sucesso.", chave);
        return new RespostaSucesso("O produto com chave " + chave + " foi apagado com sucesso.");
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
    public RespostaInclusao<Integer> addSobremesa(@RequestBody JsonNode requestBody) throws BancoDadosException {
        SobremesaMapper sobremesaMapper = new SobremesaMapper();
        sobremesaMapper.reset();
        sobremesaMapper.setValores(requestBody);
        Produto sobremesa = sobremesaMapper.build();
        repositorioProduto.adiciona(sobremesa);
        logger.info("Incluído produto - {}.", sobremesa);
        return new RespostaInclusao<>(sobremesa.getChave());
    }

    @PutMapping(path = "/sobremesa",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RespostaSucesso updateSobremesa(@RequestBody Sobremesa sobremesa) throws BancoDadosException {
        repositorioProduto.altera(sobremesa);
        logger.info("Alterado produto - {}.", sobremesa);
        return new RespostaSucesso(getMensagemSucessoAlteracao(sobremesa));
    }

    @PostMapping(path = "/bebida",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RespostaInclusao<Integer> addBebida(@RequestBody JsonNode requestBody) throws BancoDadosException {
        BebidaMapper bebidaMapper = new BebidaMapper();
        bebidaMapper.reset();
        bebidaMapper.setValores(requestBody);
        Produto bebida = bebidaMapper.build();
        repositorioProduto.adiciona(bebida);
        logger.info("Incluído produto - {}.", bebida);
        return new RespostaInclusao<>(bebida.getChave());
    }

    @PutMapping(path = "/bebida",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RespostaSucesso updateBebida(@RequestBody Bebida bebida) throws BancoDadosException {
        repositorioProduto.altera(bebida);
        logger.info("Alterado produto - {}.", bebida);
        return new RespostaSucesso(getMensagemSucessoAlteracao(bebida));
    }


    @PostMapping(path = "/comida",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RespostaInclusao<Integer> addComida(@RequestBody JsonNode requestBody) throws BancoDadosException {
        ComidaMapper comidaMapper = new ComidaMapper();
        comidaMapper.reset();
        comidaMapper.setValores(requestBody);
        Produto comida = comidaMapper.build();
        repositorioProduto.adiciona(comida);
        logger.info("Incluído produto - {}.", comida);
        return new RespostaInclusao<>(comida.getChave());
    }

    @PutMapping(path = "/comida",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public RespostaSucesso updateComida(@RequestBody Comida comida) throws BancoDadosException {
        repositorioProduto.altera(comida);
        logger.info("Alterado produto - {}.", comida);
        return new RespostaSucesso(getMensagemSucessoAlteracao(comida));
    }

    private static String getMensagemSucessoAlteracao(Produto produto) {
        return "O produto com chave " + produto.getChave() + " foi alterado com sucesso.";
    }

}
