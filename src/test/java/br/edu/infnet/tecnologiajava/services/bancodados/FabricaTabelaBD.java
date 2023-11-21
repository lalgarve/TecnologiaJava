package br.edu.infnet.tecnologiajava.services.bancodados;

/**
 * Java permite parametrizar os testes com enumeracao
 *
 * @author leila
 */
public enum FabricaTabelaBD {
    TABELAIMPL((nome) -> new TabelaImpl<Integer, ValorSemDependente>(nome)),
    TABELA_DEPENDENTE((nome) -> new TabelaDependente<Integer, ValorSemDependente>(nome));

    private final FabricaTabela<Integer, ValorSemDependente> fabrica;

    FabricaTabelaBD(FabricaTabela<Integer, ValorSemDependente> fabrica) {
        this.fabrica = fabrica;
    }

    public TabelaBD<Integer, ValorSemDependente> constroiTabela(String nome) {
        return fabrica.constroiTabela(nome);
    }

}
