package br.edu.infnet.tecnologiajava.controller;

import br.edu.infnet.tecnologiajava.ValidadorException;
import br.edu.infnet.tecnologiajava.model.view.RelatorioProdutosVendidos;
import br.edu.infnet.tecnologiajava.model.view.RespostaSucesso;
import br.edu.infnet.tecnologiajava.repository.RepositorioPedido;
import br.edu.infnet.tecnologiajava.services.bancodados.BancoDadosException;
import br.edu.infnet.tecnologiajava.services.relatorios.GeradorRelatorioProdutosVendidos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@RestController
@RequestMapping("/relatorio")
public class RelatorioController {

    private final RepositorioPedido repositorioPedido;

    private final ConfiguracaoController configuracaoController;

    private Logger logger = LoggerFactory.getLogger(RelatorioController.class);

    public RelatorioController(RepositorioPedido repositorioPedido, ConfiguracaoController configuracaoController) {
        this.repositorioPedido = repositorioPedido;
        this.configuracaoController = configuracaoController;
    }

    @GetMapping(value = "/vendas/{ano}/{mes}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RelatorioProdutosVendidos relatorioProdutosVendidos(@PathVariable int ano, @PathVariable int mes) throws BancoDadosException, ValidadorException {
        logger.info("Gerando relatório de vendas para o ano {ano} e mês {mes}");
        GeradorRelatorioProdutosVendidos geradorRelatorioProdutosVendidos = new GeradorRelatorioProdutosVendidos(ano, mes);
        return geradorRelatorioProdutosVendidos.geraRelatorioProdutosVendidos(repositorioPedido);
    }

    @GetMapping(value = "/vendas/{ano}/{mes}/salva", produces = MediaType.APPLICATION_JSON_VALUE)
    public RespostaSucesso salvaRelatorioProdutosVendidos(@PathVariable int ano, @PathVariable int mes) throws BancoDadosException, ValidadorException, IOException, RelatorioPathException {
        verificaPathRelatorio();
        String nomeArquivo = "RelatorioVendas-" + ano + "-" + mes + ".md";
        File arquivo = new File(configuracaoController.getRelatoriosPath(), nomeArquivo);
        try (FileWriter fw = new FileWriter(arquivo)) {
            logger.info("Gerando e gravando relatório de vendas para o ano {ano} e mês {mes}");
            GeradorRelatorioProdutosVendidos geradorRelatorioProdutosVendidos = new GeradorRelatorioProdutosVendidos(ano, mes);
            String relatorio = geradorRelatorioProdutosVendidos.geraRelatorioProdutosVendidosMd(repositorioPedido);
            fw.write(relatorio);
        }
        return new RespostaSucesso("Arquivo " + arquivo + "gerado com sucesso.");
    }

    private void verificaPathRelatorio() throws RelatorioPathException {
        String relatoriosPath = configuracaoController.getRelatoriosPath();
        if (relatoriosPath == null || relatoriosPath.isBlank()) {
            throw new RelatorioPathException("A pasta de relatórios não está configurada.");
        }
        File pastaRelatorios = new File(relatoriosPath);
        if (pastaRelatorios.isFile()) {
            throw new RelatorioPathException("A pasta de relatórios aponta para um arquivo.");
        }
        if (!pastaRelatorios.exists()) {
            throw new RelatorioPathException("A pasta de relatórios não existe.");
        }
        if (!pastaRelatorios.canWrite()) {
            throw new RelatorioPathException("A pasta de relatórios não pode ser escrita.");
        }
    }


}
