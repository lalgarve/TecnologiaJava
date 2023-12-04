package br.edu.infnet.tecnologiajava.repository;

import br.edu.infnet.tecnologiajava.TecnologiajavaApplication;
import br.edu.infnet.tecnologiajava.model.domain.Pedido;
import br.edu.infnet.tecnologiajava.model.domain.Solicitante;
import br.edu.infnet.tecnologiajava.services.bancodados.BancoDadosException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfiguracaoRepositorios {
    @Bean
    public RepositorioSolicitante repositorioSolicitante() throws BancoDadosException {
        RepositorioSolicitante repositorioSolicitante = new RepositorioSolicitante();
        String arquivo = TecnologiajavaApplication.class.getResource("/solicitante.csv").getFile();
        InicializadorRepositorio.carregaSolicitante(repositorioSolicitante, arquivo);
        repositorioSolicitante.adiciona(Solicitante.getVazio());
        return repositorioSolicitante;
    }

    @Bean
    public RepositorioProduto repositorioProduto() throws BancoDadosException {
        RepositorioProduto repositorioProduto = new RepositorioProduto();
        String arquivo = TecnologiajavaApplication.class.getResource("/sobremesa.csv").getFile();
        InicializadorRepositorio.carregaSobremesa(repositorioProduto, arquivo);
        arquivo = TecnologiajavaApplication.class.getResource("/bebida.csv").getFile();
        InicializadorRepositorio.carregaBebida(repositorioProduto, arquivo);
        arquivo = TecnologiajavaApplication.class.getResource("/comida.csv").getFile();
        InicializadorRepositorio.carregaComida(repositorioProduto, arquivo);
        return repositorioProduto;
    }

    @Bean
    public RepositorioPedido repositorioPedido() throws BancoDadosException {
        Pedido.inicializaContadorCodigo();
        RepositorioPedido repositorioPedido = new RepositorioPedido(repositorioProduto(), repositorioSolicitante());
        String arquivo = TecnologiajavaApplication.class.getResource("/pedido.csv").getFile();
        InicializadorRepositorio.carregaPedido(repositorioPedido, arquivo);
        return repositorioPedido;
    }

}
