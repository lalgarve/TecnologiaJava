package br.edu.infnet.tecnologiajava.services.mapper.csv;

import br.edu.infnet.tecnologiajava.services.mapper.Mapper;
import br.edu.infnet.tecnologiajava.services.mapper.MapperException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Classe usada para ler arquivos do tipo CSV, ou seja,
 * arquivo possui campos separados por vírgula.
 * Formato:
 * <ul>
 * <li> Linhas vazias são ignoradas.</li>
 * <li> Campos do tipo String podem ter vírgula se o texto estiver entre aspas.</li>
 * <li> Campos sem conteúdo não são suportados.</li>
 * </ul>
 *
 * @param <T> Classe do objeto a ser lido
 * @author leila
 */
public class CSVReader<T> implements AutoCloseable {

    private final BufferedReader reader;
    private final Mapper<T> mapper;
    private Pattern patternCSV;

    public CSVReader(Reader reader, Mapper<T> mapper) {
        this.reader = new BufferedReader(reader);
        this.mapper = mapper;
    }

    /**
     * Lê os dados do arquivo CSV.
     *
     * @return uma Stream com os objetos lidos. Se o arquivo está vazio, a stream
     * não possui elementos;
     * @throws UncheckedIOException Se houve uma {@link java.io.IOException IOException}
     *                              lendo os dados.
     * @throws MapperException      Se houve um problema durante o mapeamento
     */
    public Stream<T> leDados() throws UncheckedIOException, MapperException {
        Stream<String> lines = reader.lines();
        List<String> cabecalho = leCabecalho(lines
                .filter(linha -> !linha.isBlank())
                .findFirst()
                .orElse(null));
        lines = reader.lines();
        return lines.filter(linha -> !linha.isBlank())
                .map(linha -> leCampos(cabecalho, linha));
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    private List<String> leCabecalho(String linha) {
        if (linha == null) {
            return Collections.emptyList();
        } else {
            return Arrays.asList(linha.split(","));
        }
    }

    private T leCampos(List<String> cabecalho, String linha) {
        List<String> valores = extraiValores(linha);
        if (cabecalho.size() != valores.size()) {
            throw new MapperException(String.format(
                    "Esperados %d valores, foram encontrados %d.",
                    cabecalho.size(), valores.size()
            ));
        }
        ListIterator<String> iteratorCabecalho = cabecalho.listIterator();
        mapper.reset();
        while (iteratorCabecalho.hasNext()) {
            String valor = valores.get(iteratorCabecalho.nextIndex());
            String campo = iteratorCabecalho.next();
            mapper.setValor(campo, valor);
        }
        return mapper.build();
    }

    private List<String> extraiValores(String linha) {
        Matcher matcherCSV = getPatternCSV().matcher(linha);

        List<String> valores = new ArrayList<>();
        while (matcherCSV.find()) {
            if (matcherCSV.group(1) == null) {
                valores.add(matcherCSV.group(0));
            } else {
                valores.add(matcherCSV.group(1));
            }

        }

        return valores;
    }

    private Pattern getPatternCSV() {
        if (patternCSV == null) {
            StringBuilder builder = new StringBuilder();
            adicionaGrupoEntreAspas(builder);
            builder.append('|');
            adicionaGrupoSemVirgula(builder);
            patternCSV = Pattern.compile(builder.toString());
        }
        return patternCSV;
    }

    private void adicionaGrupoEntreAspas(StringBuilder builder) {
        final String aspas = Pattern.quote("\"");
        builder.append(aspas)
                .append("([^").append(aspas).append("]+)")
                .append(aspas);
    }

    private void adicionaGrupoSemVirgula(StringBuilder builder) {
        final String virgula = Pattern.quote(",");
        builder.append("([^").append(virgula).append("]+)");

    }
}
