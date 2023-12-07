package br.edu.infnet.tecnologiajava.repository;

import br.edu.infnet.tecnologiajava.model.domain.Produto;
import br.edu.infnet.tecnologiajava.services.bancodados.BancoDadosException;
import br.edu.infnet.tecnologiajava.services.bancodados.TabelaDependente;

import java.util.Arrays;
import java.util.List;

public class RepositorioProduto  extends TabelaDependente<Integer, Produto>  implements BuscaTexto<Produto> {

    public RepositorioProduto() {
        super("produto");
    }


    @Override
    public List<Produto> buscaPorTexto(String[] palavras) throws BancoDadosException {
        return super.getValores(produto ->  Arrays.stream(palavras).allMatch(palavra -> produtoContemPalavra(produto,palavra)));
    }

    private boolean produtoContemPalavra(Produto produto, String palavra){
        String palavraNormalizada = normalizaTexto(palavra);
        String nomeNormalizado = normalizaTexto(produto.getNome());
        String detalhesNormalizado = normalizaTexto(produto.getDetalhe());
        return nomeNormalizado.contains(palavraNormalizada) ||
                detalhesNormalizado.contains(palavraNormalizada);
    }
}
