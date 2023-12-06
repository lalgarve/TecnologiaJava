package br.edu.infnet.tecnologiajava.services.mapper.json;

import br.edu.infnet.tecnologiajava.model.domain.ValidadorException;
import com.fasterxml.jackson.databind.JsonNode;

public interface JsonMapper <T>{
    T build(JsonNode node) throws ValidadorException;
}
