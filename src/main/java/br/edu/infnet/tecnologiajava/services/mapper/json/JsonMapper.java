package br.edu.infnet.tecnologiajava.services.mapper.json;

import br.edu.infnet.tecnologiajava.services.mapper.Mapper;
import br.edu.infnet.tecnologiajava.services.mapper.MapperException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.StreamSupport;

public interface JsonMapper <T> extends Mapper<T> {
    static Set<JsonNodeType> TIPOS_CAMPO_VALIDOS = Set.of(JsonNodeType.BOOLEAN, JsonNodeType.NUMBER, JsonNodeType.STRING);

    default void setValores(JsonNode jsonNode) throws MapperException{
        Iterator<Map.Entry<String, JsonNode>> campos = jsonNode.fields();
        while(campos.hasNext()){
            Map.Entry<String, JsonNode> campo = campos.next();
            if(!TIPOS_CAMPO_VALIDOS.contains(campo.getValue().getNodeType())){
                throw new MapperException("O campo "+campo.getKey()+" com o tipo "
                        + campo.getValue().getNodeType()+" não é suportado.");
            }
            setValor(campo.getKey(), campo.getValue().asText());
        }
        
    }
}
