package br.edu.infnet.tecnologiajava.services.mapper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class MapperAbstrato<T> implements Mapper<T> {

    private Set<String> camposSetados;
    private final String[] camposObrigatorios;

    protected MapperAbstrato(String[] camposObrigatorios) {
        Objects.requireNonNull(camposObrigatorios, "Os campos não podem ser nulo.");
        this.camposObrigatorios = camposObrigatorios;
    }

    @Override
    public void reset() {
        camposSetados = new HashSet<>();
    }

    protected void adicionaCampoSetado(String campo) throws MapperException {
        if (camposSetados == null) {
            throw new MapperException("O método reset não foi chamado.");
        }
        if (camposSetados.contains(campo)) {
            throw new MapperException("O campo " + campo + " já foi setado.");
        }
        camposSetados.add(campo);
    }

    protected void verificaTodosCamposSetatos() throws MapperException {
        if (!camposSetados.containsAll(Arrays.asList(camposObrigatorios))) {
            String camposFaltantes = Arrays.stream(camposObrigatorios)
                    .filter(campo -> !camposSetados.contains(campo))
                    .collect(Collectors.joining(", "));
            if (camposFaltantes.contains(",")) {
                throw new MapperException("Os seguintes campos não foram setados: " + camposFaltantes + ".");
            } else {
                throw new MapperException("O campo " + camposFaltantes + " não foi setado.");
            }
        }
    }

    protected void finaliza() {
        camposSetados = null;
    }

}
