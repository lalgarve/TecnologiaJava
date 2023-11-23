package br.edu.infnet.tecnologiajava.model.domain;

import br.edu.infnet.tecnologiajava.services.bancodados.Imutavel;
import br.edu.infnet.tecnologiajava.services.bancodados.ValorBD;

import java.util.regex.Pattern;

public class Solicitante implements ValorBD<String, Solicitante>, Imutavel {

    private final String email;
    private final String cpf;
    private final String nome;
    private static Solicitante vazio;

    private static final String CPF_VAZIO = "000.000.000-00";

    public Solicitante(String cpf, String nome, String email) throws ValidadorException {
        this.email = email;
        this.cpf = cpf;
        this.nome = nome;
        Validador validador = new Validador();
        validaCPF(validador);
        validaEmail(validador);
        validaNome(validador);
        if (validador.temErro()) {
            throw new ValidadorException("Há erros nos dados do solicitante", validador);
        }
    }

    public Solicitante(String cpf) throws ValidadorException{
        this.cpf = cpf;
        nome = "";
        email = "";
        Validador validador = new Validador();
        validaCPF(validador);
        if (validador.temErro()) {
            throw new ValidadorException("Há erros nos dados do solicitante", validador);
        }
    }

    private Solicitante() {
        this.cpf = CPF_VAZIO;
        nome = "";
        email = "";
    }

    public static Solicitante getVazio() {
        if (vazio == null) {
            vazio = new Solicitante();
        }
        return vazio;
    }

    @Override
    public boolean podeSerGravadoNoBanco() {
        return cpf.equals(CPF_VAZIO) || !nome.isBlank();
    }

    public String getEmail() {
        return email;
    }

    public String getCPF() {
        return cpf;
    }

    public String getNome() {
        return nome;
    }

    private void validaCPF(Validador validador) {
        if (validaCPFPreenchido(validador) && validaFormatoCPF(validador)) {
            int[] digitos = cpf.chars()
                    .filter(digito -> digito != '.' && digito != '-')
                    .map(digito -> digito - '0')
                    .toArray();
            int digitoVerificador1 = calculaDigitoVerificador(digitos, 10, 9);
            int digitoVerificador2 = calculaDigitoVerificador(digitos, 11, 10);
            validador.valida("O CPF é inválido", digitos[9] == digitoVerificador1 && digitos[10] == digitoVerificador2);
        }
    }

    private boolean validaCPFPreenchido(Validador validador) {
        boolean resultado = cpf != null && !cpf.isBlank();
        validador.valida("O CPF não pode ser nulo", cpf != null);
        validador.valida("O CPF não pode estar em branco", cpf == null || !cpf.isBlank());
        return resultado;
    }

    private boolean validaFormatoCPF(Validador validador) {
        boolean valido = Pattern.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", cpf);
        validador.valida("O formato do CPF é inválido", valido);
        return valido;
    }

    private int calculaDigitoVerificador(int[] digitos, int multiplicador, int numeroDigitos) {
        int soma = 0;
        for (int pos = 0; pos < numeroDigitos; pos++) {
            soma += digitos[pos] * (multiplicador - pos);
        }
        int resto = (soma * 10) % 11;
        return resto == 10 ? 0 : resto;
    }

    /*
     * https://www.baeldung.com/java-email-validation-regex
     * Usando validação OWASP
     * SonarLint java:S59980 Teste: Solicitante.testLimiteEmail
     * Email não pode ser grande demais
     */
    private void validaEmail(Validador validador) {
        validador.valida("O email não pode ser nulo", email != null);
        validador.valida("O email não pode estar em branco", email == null || !email.isBlank());
        validador.valida("O email pode ter no máximo 150 caracteres", email == null || email.length() <= 150);
        if (!validador.temErro()) {
            String emailOWASPPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
            validador.valida("O email é inválido", Pattern.matches(emailOWASPPattern, email));
        }
    }

    private void validaNome(Validador validador) {
        validador.valida("O nome não pode ser nulo", nome != null);
        validador.valida("O nome não pode estar em branco", nome == null || !nome.isBlank());
    }

    @Override
    public String getChave() {
        return cpf;
    }

    @Override
    public Solicitante criaInstanciaCopiaSegura() {
        return this;
    }

    @Override
    public String toString() {
        if (cpf.equals(CPF_VAZIO)) {
            return "Solicitante vazio";
        } else {
            return String.format("Solicitante: cpf=%s, nome=%s, email=%s", cpf, nome, email);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + email.hashCode();
        result = prime * result + cpf.hashCode();
        result = prime * result + nome.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Solicitante other = (Solicitante) obj;
        if (!email.equals(other.email))
            return false;
        if (!cpf.equals(other.cpf))
            return false;
        return nome.equals(other.nome);
    }


}
