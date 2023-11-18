package br.edu.infnet.tecnologiajava.repository;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.Iterator;

import br.edu.infnet.tecnologiajava.model.domain.*;
import br.edu.infnet.tecnologiajava.model.mapper.BebidaMapper;
import br.edu.infnet.tecnologiajava.model.mapper.ComidaMapper;
import br.edu.infnet.tecnologiajava.model.mapper.SobremesaMapper;
import br.edu.infnet.tecnologiajava.model.mapper.SolicitanteMapper;
import br.edu.infnet.tecnologiajava.services.bancodados.BancoDadosException;
import br.edu.infnet.tecnologiajava.services.bancodados.TabelaBD;
import br.edu.infnet.tecnologiajava.services.bancodados.ValorBD;
import br.edu.infnet.tecnologiajava.services.csv.CSVMapper;
import br.edu.infnet.tecnologiajava.services.csv.CSVMapperException;
import br.edu.infnet.tecnologiajava.services.csv.CSVReader;

public class ControladorRepositorio {

    public static void inicializa(){
        RepositorioPedido.criaRepositorio();
        RepositorioProduto.criaRepositorio();
        RepositorioSolicitante.criaRepositorio();
        Produto.inicializaContadorCodigo();
        Pedido.inicializaContadorCodigo();
    }

    public static void carregaSobremesa(Reader reader) throws BancoDadosException{
        carrega(reader, new SobremesaMapper(), RepositorioProduto.getInstance());
    }

    public static void carregaBebida(Reader reader) throws BancoDadosException{
        carrega(reader, new BebidaMapper(), RepositorioProduto.getInstance());
    }

    public static void carregaComida(Reader reader) throws BancoDadosException{
        carrega(reader, new ComidaMapper(), RepositorioProduto.getInstance());
    }

    public static void carregaSolicitante(Reader reader) throws BancoDadosException{
        carrega(reader, new SolicitanteMapper(), RepositorioSolicitante.getInstance());
    }

    private static <T extends ValorBD<?,T>> void carrega (Reader reader, CSVMapper<T> mapper, TabelaBD<?,T> repositorio)
            throws BancoDadosException {
        try (CSVReader<T> csvReader = new CSVReader<>(reader, mapper)){
            Iterator<T> iterator =  csvReader.leDados().iterator();
            while(iterator.hasNext()){
                repositorio.adiciona(iterator.next());
            }
        }
        catch(IOException | UncheckedIOException ex){
            throw new BancoDadosException("Erro lendo dados da "+repositorio.getNome()+".", ex);
        }
        catch(CSVMapperException ex){
            throw new BancoDadosException("Erro nos campos da "+repositorio.getNome()+".",ex);
        }
    }
    
}
