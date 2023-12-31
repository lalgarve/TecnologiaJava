package br.edu.infnet.tecnologiajava.repository;

import br.edu.infnet.tecnologiajava.ExcecaoInesperada;
import br.edu.infnet.tecnologiajava.TecnologiajavaApplication;
import br.edu.infnet.tecnologiajava.model.domain.Pedido;
import br.edu.infnet.tecnologiajava.model.domain.Solicitante;
import br.edu.infnet.tecnologiajava.model.mapper.*;
import br.edu.infnet.tecnologiajava.services.bancodados.BancoDadosException;
import br.edu.infnet.tecnologiajava.services.bancodados.TabelaBD;
import br.edu.infnet.tecnologiajava.services.bancodados.ValorBD;
import br.edu.infnet.tecnologiajava.services.mapper.Mapper;
import br.edu.infnet.tecnologiajava.services.mapper.MapperException;
import br.edu.infnet.tecnologiajava.services.mapper.csv.CSVReader;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Iterator;

@Configuration
@AllArgsConstructor
public class CriacaoRepositorios {

    private final Logger logger = LoggerFactory.getLogger(CriacaoRepositorios.class);

    private final ConfiguracaoRepositorios configuracaoRepositorios;

    @Bean
    public RepositorioSolicitante repositorioSolicitante() throws BancoDadosException {
        RepositorioSolicitante repositorioSolicitante = new RepositorioSolicitante();
        logger.info("Carregando solicitantes.");
        carrega(configuracaoRepositorios.getArquivoSolicitante(), new SolicitanteMapper(), repositorioSolicitante);
        repositorioSolicitante.adiciona(Solicitante.getVazio());
        return repositorioSolicitante;
    }

    @Bean
    public RepositorioProduto repositorioProduto() throws BancoDadosException {
        RepositorioProduto repositorioProduto = new RepositorioProduto();
        logger.info("Carregando sobremesas.");
        carrega(configuracaoRepositorios.getArquivoSobremesa(), new SobremesaMapper(), repositorioProduto);
        logger.info("Carregando bebidas.");
        carrega(configuracaoRepositorios.getArquivoBebida(), new BebidaMapper(), repositorioProduto);
        logger.info("Carregando comidas.");
        carrega(configuracaoRepositorios.getArquivoComida(), new ComidaMapper(), repositorioProduto);
        return repositorioProduto;
    }

    @Bean
    public RepositorioPedido repositorioPedido() throws BancoDadosException {
        Pedido.inicializaContadorCodigo();
        RepositorioPedido repositorioPedido = new RepositorioPedido(repositorioProduto(), repositorioSolicitante());
        logger.info("Carregando pedidos.");
        carrega(configuracaoRepositorios.getArquivoPedidos(), new PedidoMapper(), repositorioPedido);
        return repositorioPedido;
    }

    public static <T extends ValorBD<?, T>> void carrega(String recurso, Mapper<T> mapper, TabelaBD<?, T> repositorio) throws BancoDadosException {
        URL recursoUrl = TecnologiajavaApplication.class.getResource(recurso);
        if (recursoUrl == null) {
            throw new ExcecaoInesperada("Recurso " + recurso + " não foi encontrado.");
        }
        try (FileReader fr = new FileReader(recursoUrl.getFile())) {
            carrega(fr, mapper, repositorio);
        } catch (IOException ex) {
            throw new BancoDadosException("Recurso " + recurso + " não pode ser lido.");
        }
    }

    private static <T extends ValorBD<?, T>> void carrega(Reader reader, Mapper<T> mapper, TabelaBD<?, T> repositorio)
            throws BancoDadosException {
        try (CSVReader<T> csvReader = new CSVReader<>(reader, mapper)) {
            Iterator<T> iterator = csvReader.leDados().iterator();
            while (iterator.hasNext()) {
                repositorio.adiciona(iterator.next());
            }
        } catch (IOException | UncheckedIOException ex) {
            throw new BancoDadosException("Erro lendo dados da " + repositorio.getNome() + ".", ex);
        } catch (MapperException ex) {
            throw new BancoDadosException("Erro nos campos da " + repositorio.getNome() + ".", ex);
        }
    }

}
