package br.edu.infnet.tecnologiajava.repository;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.Iterator;

import br.edu.infnet.tecnologiajava.model.domain.Bebida;
import br.edu.infnet.tecnologiajava.model.domain.Comida;
import br.edu.infnet.tecnologiajava.model.domain.Pedido;
import br.edu.infnet.tecnologiajava.model.domain.Produto;
import br.edu.infnet.tecnologiajava.model.domain.Sobremesa;
import br.edu.infnet.tecnologiajava.model.mapper.BebidaMapper;
import br.edu.infnet.tecnologiajava.model.mapper.ComidaMapper;
import br.edu.infnet.tecnologiajava.model.mapper.SobremesaMapper;
import br.edu.infnet.tecnologiajava.services.bancodados.BancoDadosException;
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
        SobremesaMapper mapper = new SobremesaMapper();
        try (CSVReader<Sobremesa> csvReader = new CSVReader<>(reader, mapper)){        
            Iterator<Sobremesa> iterator =  csvReader.leDados().iterator();
            RepositorioProduto repositorioProduto = RepositorioProduto.getInstance();
            while(iterator.hasNext()){
                repositorioProduto.adiciona(iterator.next());
            }
        }
        catch(IOException | UncheckedIOException ex){
            throw new BancoDadosException("Erro lendo dados da sobremesa.", ex);
        }
        catch(CSVMapperException ex){
            throw new BancoDadosException("Erro nos campos da sobremesa.",ex);
        }
    }

    public static void carregaBebida(Reader reader) throws BancoDadosException{
        BebidaMapper mapper = new BebidaMapper();
        try (CSVReader<Bebida> csvReader = new CSVReader<>(reader, mapper)){        
            Iterator<Bebida> iterator =  csvReader.leDados().iterator();
            RepositorioProduto repositorioProduto = RepositorioProduto.getInstance();
            while(iterator.hasNext()){                
                repositorioProduto.adiciona(iterator.next());
            }
        }
        catch(IOException | UncheckedIOException ex){
            throw new BancoDadosException("Erro lendo dados da bebida.", ex);
        }
        catch(CSVMapperException ex){
            throw new BancoDadosException("Erro nos campos da bebida.",ex);
        }        
    }


    public static void carregaComida(Reader reader) throws BancoDadosException{
        ComidaMapper mapper = new ComidaMapper();
        try (CSVReader<Comida> csvReader = new CSVReader<>(reader, mapper)){        
            Iterator<Comida> iterator =  csvReader.leDados().iterator();
            RepositorioProduto repositorioProduto = RepositorioProduto.getInstance();
            while(iterator.hasNext()){
                repositorioProduto.adiciona(iterator.next());
            }
        }
        catch(IOException | UncheckedIOException ex){
            throw new BancoDadosException("Erro lendo dados da bebida.", ex);
        }
        catch(CSVMapperException ex){
            throw new BancoDadosException("Erro nos campos da bebida.",ex);
        }        
    }    
    
}
