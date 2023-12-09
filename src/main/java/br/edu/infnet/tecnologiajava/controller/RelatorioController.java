package br.edu.infnet.tecnologiajava.controller;

import br.edu.infnet.tecnologiajava.ValidadorException;
import br.edu.infnet.tecnologiajava.model.view.RelatorioProdutosVendidos;
import br.edu.infnet.tecnologiajava.model.view.RespostaSucesso;
import br.edu.infnet.tecnologiajava.repository.RepositorioPedido;
import br.edu.infnet.tecnologiajava.services.bancodados.BancoDadosException;
import br.edu.infnet.tecnologiajava.services.relatorios.GeradorRelatorioProdutosVendidos;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileWriter;
import java.io.IOException;

@RestController
@RequestMapping("/relatorio")
public class RelatorioController {

    private final RepositorioPedido repositorioPedido;

    public RelatorioController(RepositorioPedido repositorioPedido){
        this.repositorioPedido = repositorioPedido;
    }

    @GetMapping(value = "/vendas/{ano}/{mes}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RelatorioProdutosVendidos relatorioProdutosVendidos(@PathVariable int ano, @PathVariable int mes) throws BancoDadosException, ValidadorException {
        GeradorRelatorioProdutosVendidos geradorRelatorioProdutosVendidos = new GeradorRelatorioProdutosVendidos(ano, mes);
        return geradorRelatorioProdutosVendidos.geraRelatorioProdutosVendidos(repositorioPedido);
    }

    @GetMapping(value = "/vendas/{ano}/{mes}/salva", produces = MediaType.APPLICATION_JSON_VALUE)
    public RespostaSucesso salvaRelatorioProdutosVendidos(@PathVariable int ano, @PathVariable int mes) throws BancoDadosException, ValidadorException, IOException {
        String arquivo = "RelatorioVendas-"+ano+"-"+mes+".md";
        GeradorRelatorioProdutosVendidos geradorRelatorioProdutosVendidos = new GeradorRelatorioProdutosVendidos(ano, mes);
        String relatorio = geradorRelatorioProdutosVendidos.geraRelatorioProdutosVendidosMd(repositorioPedido);
        try (FileWriter fw = new FileWriter(arquivo)){
            fw.write(relatorio);
        }
        return new RespostaSucesso("Arquivo "+ arquivo +"gerado com sucesso.");
    }


}
