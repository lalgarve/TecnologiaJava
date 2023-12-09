package br.edu.infnet.tecnologiajava.services.mapper.json;

import br.edu.infnet.tecnologiajava.services.mapper.Mapper;
import br.edu.infnet.tecnologiajava.services.mapper.MapperException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public interface JsonMapper<T> extends Mapper<T> {
    Set<JsonNodeType> TIPOS_CAMPO_VALIDOS = Set.of(JsonNodeType.BOOLEAN, JsonNodeType.NUMBER,
            JsonNodeType.STRING, JsonNodeType.ARRAY);

    default void setValores(JsonNode jsonNode) throws MapperException {
        Iterator<Map.Entry<String, JsonNode>> campos = jsonNode.fields();
        while (campos.hasNext()) {
            Map.Entry<String, JsonNode> campo = campos.next();
            if (!TIPOS_CAMPO_VALIDOS.contains(campo.getValue().getNodeType())) {
                throw new MapperException("O campo " + campo.getKey() + " com o tipo "
                        + campo.getValue().getNodeType() + " não é suportado.");
            }
            if (!campo.getValue().isArray()) {
                setValor(campo.getKey(), campo.getValue().asText());
            } else
            {
                String codigos = codigosSeparadosVirgula(campo);
                setValor(campo.getKey(), codigos);
            }
        }

    }

    private static String codigosSeparadosVirgula(Map.Entry<String, JsonNode> campo) {
        Iterator<JsonNode> jsonNodeIterator = campo.getValue().iterator();
        Spliterator<JsonNode> jsonNodeSpliterator = Spliterators.spliteratorUnknownSize(jsonNodeIterator, Spliterator.ORDERED);
        return StreamSupport.stream(jsonNodeSpliterator, false)
                .map(node -> node.asInt() + "")
                .collect(Collectors.joining(", "));
    }
}
