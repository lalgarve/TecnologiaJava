package br.edu.infnet.tecnologiajava.services.bancodados;

import java.util.ArrayList;
import java.util.List;


public class ValorComDependente implements ValorBD<Integer, ValorComDependente> {

    private final Integer chave;
    private final List<ValorSemDependente> valoresDependentes;

    public ValorComDependente(Integer chave) {
        this.chave = chave;
        valoresDependentes = new ArrayList<>();
    }

    public ValorComDependente(ValorComDependente aSerCopiado) {
        this.chave = aSerCopiado.chave;
        valoresDependentes = new ArrayList<>();
        aSerCopiado.valoresDependentes.forEach(
                (original) -> valoresDependentes.add(original.criaInstanciaCopiaSegura())
        );
    }

    @Override
    public Integer getChave() {
        return chave;
    }

    public void adcionaValorDependente(ValorSemDependente valor) {
        valoresDependentes.add(valor);
    }

    public void removeValorDependente(ValorSemDependente valor) {
        valoresDependentes.remove(valor);
    }

    @Override
    public ValorComDependente criaInstanciaCopiaSegura() {
        return new ValorComDependente(this);
    }

    @Override
    public boolean podeSerGravadoNoBanco() {
        return valoresDependentes.isEmpty();
    }
}
