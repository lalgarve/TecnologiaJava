package br.edu.infnet.tecnologiajava.controller;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("controller")
@Data
public class ConfiguracaoController {
    private String relatoriosPath;
}
