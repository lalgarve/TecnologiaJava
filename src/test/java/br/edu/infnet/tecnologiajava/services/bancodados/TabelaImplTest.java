package br.edu.infnet.tecnologiajava.services.bancodados;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class TabelaImplTest {
    private TabelaImpl<Integer,ValorSemDependente> tabela = new TabelaImpl<>("minhatabela");

    @TestFactory
    public Collection<DynamicTest> testEquals() throws BancoDadosException{
        TabelaDependente<?,?> tabelaDependente = new TabelaDependente<>("minhatabela");
        tabela.adiciona(new ValorSemDependente(0)); 
        TabelaImpl<Integer,ValorSemDependente> tabelaMesmaClasseIgual = new TabelaImpl<>("minhatabela");
        TabelaImpl<Integer,ValorSemDependente> tabelaMesmaClasseDiferente = new TabelaImpl<>("minhatabela1");

        return Arrays.asList(
            dynamicTest("Compara com null", () -> testEquals(null, false)),
            dynamicTest("Mesma instancia", () -> testEquals(tabela, true)),
            dynamicTest("Diferente classe", () -> testEquals(tabelaDependente, false)),
            dynamicTest("Mesma classe igual", () -> testEquals(tabelaMesmaClasseIgual, true)),
            dynamicTest("Mesma classe diferente", () -> testEquals(tabelaMesmaClasseDiferente, false))
        );
    }

    private void testEquals(Object object, boolean equals){
        assertEquals(equals, tabela.equals(object));
    }

}
