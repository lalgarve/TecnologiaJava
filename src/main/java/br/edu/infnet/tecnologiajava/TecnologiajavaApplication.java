package br.edu.infnet.tecnologiajava;

import br.edu.infnet.tecnologiajava.repository.InicializadorRepositorio;
import br.edu.infnet.tecnologiajava.services.bancodados.BancoDadosException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@SpringBootApplication
public class TecnologiajavaApplication {

    private static Logger logger = LogManager.getLogger(TecnologiajavaApplication.class);

    public static void main(String[] args) throws BancoDadosException, IOException {
        InicializadorRepositorio.inicializa();


        try (InputStream is = TecnologiajavaApplication.class.getResourceAsStream("/sobremesa.csv")) {
            //noinspection ConstantConditions
            logger.info("Carregando sobremesas.");
            InicializadorRepositorio.carregaSobremesa(new InputStreamReader(is));
        }
        try (InputStream is = TecnologiajavaApplication.class.getResourceAsStream("/comida.csv")) {
            //noinspection ConstantConditions
            logger.info("Carregando comidas.");
            InicializadorRepositorio.carregaComida(new InputStreamReader(is));
        }
        try (InputStream is = TecnologiajavaApplication.class.getResourceAsStream("/bebida.csv")) {
            //noinspection ConstantConditions
            logger.info("Carregando bebidas.");
            InicializadorRepositorio.carregaBebida(new InputStreamReader(is));
        }
        try (InputStream is = TecnologiajavaApplication.class.getResourceAsStream("/solicitante.csv")) {
            //noinspection ConstantConditions
            logger.info("Carregando solicitantes.");
            InicializadorRepositorio.carregaSolicitante(new InputStreamReader(is));
        }
        try (InputStream is = TecnologiajavaApplication.class.getResourceAsStream("/pedido.csv")) {
            //noinspection ConstantConditions
            logger.info("Carregando pedidos.");
            InicializadorRepositorio.carregaPedido(new InputStreamReader(is));
        }
        SpringApplication.run(TecnologiajavaApplication.class, args);
    }

}
