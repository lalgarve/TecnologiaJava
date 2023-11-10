package br.edu.infnet.tecnologiajava.services.bancodados;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indica que o objeto é carregado parcialmente do banco de dados
 * quando uma lista é retornada
 * @author leila
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CarregamentoParcial {
  
}
