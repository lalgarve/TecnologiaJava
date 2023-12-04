package br.edu.infnet.tecnologiajava.repository;

import br.edu.infnet.tecnologiajava.model.domain.Pedido;
import br.edu.infnet.tecnologiajava.model.domain.Produto;
import br.edu.infnet.tecnologiajava.model.mapper.*;
import br.edu.infnet.tecnologiajava.services.bancodados.BancoDadosException;
import br.edu.infnet.tecnologiajava.services.bancodados.TabelaBD;
import br.edu.infnet.tecnologiajava.services.bancodados.ValorBD;
import br.edu.infnet.tecnologiajava.services.csv.CSVMapper;
import br.edu.infnet.tecnologiajava.services.csv.CSVMapperException;
import br.edu.infnet.tecnologiajava.services.csv.CSVReader;

import java.io.*;
import java.util.Iterator;

public class InicializadorRepositorio {

    private InicializadorRepositorio() {
    }

    public static void carregaSobremesa(RepositorioProduto repositorio, String arquivo) throws BancoDadosException {
        try (FileReader reader = new FileReader(arquivo)){
            carrega(reader, new SobremesaMapper(), repositorio);
        } catch (IOException e) {
            throw new BancoDadosException(e);
        }
    }

    public static void carregaBebida(RepositorioProduto repositorio, String arquivo) throws BancoDadosException {
        try (FileReader reader = new FileReader(arquivo)){
            carrega(reader, new BebidaMapper(), repositorio);
        } catch (IOException e) {
            throw new BancoDadosException(e);
        }
    }

    public static void carregaComida(RepositorioProduto repositorio, String arquivo) throws BancoDadosException {
        try (FileReader reader = new FileReader(arquivo)){
            carrega(reader, new ComidaMapper(), repositorio);
        } catch (IOException e) {
            throw new BancoDadosException(e);
        }
    }

    public static void carregaSolicitante(RepositorioSolicitante repositorio, String arquivo) throws BancoDadosException {
        try (FileReader reader = new FileReader(arquivo)){
            carrega(reader, new SolicitanteMapper(), repositorio);
        } catch (IOException e) {
            throw new BancoDadosException(e);
        }
    }

    public static void carregaPedido(RepositorioPedido repositorio, String arquivo) throws BancoDadosException {
        try (FileReader reader = new FileReader(arquivo)){
            carrega(reader, new PedidoMapper(), repositorio);
        } catch (IOException e) {
            throw new BancoDadosException(e);
        }
    }

    private static <T extends ValorBD<?, T>> void carrega(Reader reader, CSVMapper<T> mapper, TabelaBD<?, T> repositorio)
            throws BancoDadosException {
        try (CSVReader<T> csvReader = new CSVReader<>(reader, mapper)) {
            Iterator<T> iterator = csvReader.leDados().iterator();
            while (iterator.hasNext()) {
                repositorio.adiciona(iterator.next());
            }
        } catch (IOException | UncheckedIOException ex) {
            throw new BancoDadosException("Erro lendo dados da " + repositorio.getNome() + ".", ex);
        } catch (CSVMapperException ex) {
            throw new BancoDadosException("Erro nos campos da " + repositorio.getNome() + ".", ex);
        }
    }

}
