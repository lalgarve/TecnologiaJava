package br.edu.infnet.tecnologiajava;

import br.edu.infnet.tecnologiajava.model.domain.Solicitante;
import br.edu.infnet.tecnologiajava.repository.InicializadorRepositorio;
import br.edu.infnet.tecnologiajava.repository.RepositorioPedido;
import br.edu.infnet.tecnologiajava.repository.RepositorioSolicitante;
import br.edu.infnet.tecnologiajava.services.bancodados.BancoDadosException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@SpringBootApplication
public class TecnologiajavaApplication {
    public static void main(String[] args) throws BancoDadosException, IOException {
        SpringApplication.run(TecnologiajavaApplication.class, args);
    }

}
