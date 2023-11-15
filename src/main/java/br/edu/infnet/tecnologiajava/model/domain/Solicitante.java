package br.edu.infnet.tecnologiajava.model.domain;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Streams;

import br.edu.infnet.tecnologiajava.services.bancodados.Imutavel;
import br.edu.infnet.tecnologiajava.services.bancodados.ValorBD;

public class Solicitante implements ValorBD<String>, Imutavel {

    private final String email;
    private final String cpf;
    private final String nome;

    public Solicitante(String cpf, String nome, String email) throws ValidadorException {
        this.email = email;
        this.cpf = cpf;
        this.nome = nome;
        Validador validador = new Validador();
        validaCPF(validador);
        validaEmail(validador);
        validaNome (validador); 
        if(validador.temErro()) {
            throw new ValidadorException("Há erros nos dados do solicitante", validador);
        }
    }

    public String getEmail() {
        return email;
    }

    public String getCpf() {
        return cpf;
    }

    public String getNome() {
        return nome;
    }

    private void validaCPF(Validador validador) {
        if (validaCPFPreenchido(validador) && validaFormatoCPF(validador)) {
            int[] digitos = cpf.chars()
                    .filter((digito) -> digito != '.' && digito != '-')
                    .map((digito) -> digito - '0')
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

    // https://www.baeldung.com/java-email-validation-regex
    // Usando validação OWASP
    private void validaEmail(Validador validador) {
        validador.valida("O email não pode ser nulo", email != null);
        validador.valida("O email não pode estar em branco", email==null || !email.isBlank());
        boolean emailPreenchido = email != null && !email.isBlank();
        if(emailPreenchido){
            String emailOWASPPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
            validador.valida("O email é inválido", Pattern.matches(emailOWASPPattern, email));
        }
    }

    private void validaNome(Validador validador) {
        validador.valida("O nome não pode ser nulo", nome != null);
        validador.valida("O nome não pode estar em branco", nome==null || !nome.isBlank());
    }

    @Override
    public String getChave() {
        return cpf;
    }

    @Override
    public ValorBD<String> getInstanciaCopiaSegura() {
        return this;
    }

}
