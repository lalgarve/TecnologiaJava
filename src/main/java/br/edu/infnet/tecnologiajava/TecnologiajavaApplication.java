package br.edu.infnet.tecnologiajava;

import br.edu.infnet.tecnologiajava.repository.ControladorRepositorio;
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
        ControladorRepositorio.inicializa();


        try (InputStream is = TecnologiajavaApplication.class.getResourceAsStream("/sobremesa.csv")) {
            //noinspection ConstantConditions
            logger.info("Carregando sobremesas.");
            ControladorRepositorio.carregaSobremesa(new InputStreamReader(is));
        }
        try (InputStream is = TecnologiajavaApplication.class.getResourceAsStream("/comida.csv")) {
            //noinspection ConstantConditions
            logger.info("Carregando comidas.");
            ControladorRepositorio.carregaComida(new InputStreamReader(is));
        }
        try (InputStream is = TecnologiajavaApplication.class.getResourceAsStream("/bebida.csv")) {
            //noinspection ConstantConditions
            logger.info("Carregando bebidas.");
            ControladorRepositorio.carregaBebida(new InputStreamReader(is));
        }
        try (InputStream is = TecnologiajavaApplication.class.getResourceAsStream("/solicitante.csv")) {
            //noinspection ConstantConditions
            logger.info("Carregando solicitantes.");
            ControladorRepositorio.carregaSolicitante(new InputStreamReader(is));
        }
        SpringApplication.run(TecnologiajavaApplication.class, args);
    }

}
