package br.edu.infnet.tecnologiajava.repository;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("repositorios")
@Data
public class ConfiguracaoRepositorios {
    private String arquivoSolicitante;
    private String arquivoSobremesa;
    private String arquivoComida;
    private String arquivoBebida;
    private String arquivoPedidos;
}
