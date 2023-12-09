package br.edu.infnet.tecnologiajava.controller;

import br.edu.infnet.tecnologiajava.ValidadorException;
import br.edu.infnet.tecnologiajava.model.view.RespostaErro;
import br.edu.infnet.tecnologiajava.services.bancodados.BancoDadosException;
import br.edu.infnet.tecnologiajava.services.mapper.MapperException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControladoresExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {MapperException.class})
    protected ResponseEntity<Object> erroMapeamentoEValidacao(MapperException ex, WebRequest request){
        if(ex.getCause() != null && (ex.getCause() instanceof ValidadorException excecaoValidacao)){
            return erroValidacao(excecaoValidacao, request);
        }
        RespostaErro respostaErro = new RespostaErro();
        respostaErro.setTitle("Erro processando JSON.");
        respostaErro.setDetail(ex.getMessage());
        respostaErro.setInstance(((ServletWebRequest)request).getRequest().getRequestURI());
        respostaErro.setStatus(HttpStatus.BAD_REQUEST.value());
        return handleExceptionInternal(ex, respostaErro, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }



    @ExceptionHandler(value = {ValidadorException.class})
    protected ResponseEntity<Object> erroValidacao(ValidadorException ex, WebRequest request){
        RespostaErro respostaErro = new RespostaErro();
        respostaErro.setTitle("Erro validação.");
        respostaErro.setDetail(ex.getValidador().getMensagensConcatenadas());
        respostaErro.setInstance(((ServletWebRequest)request).getRequest().getRequestURI());
        respostaErro.setStatus(HttpStatus.BAD_REQUEST.value());
        return handleExceptionInternal(ex, respostaErro, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        if(ex.getRootCause()!=null && ex.getRootCause() instanceof ValidadorException validadorException){
            return erroValidacao(validadorException, request);
        }
        return super.handleHttpMessageNotReadable(ex, headers, status, request);
    }

    @ExceptionHandler(value = {BancoDadosException.class})
    protected ResponseEntity<Object> erroBancoDeDados(BancoDadosException ex, WebRequest request){
        RespostaErro respostaErro = new RespostaErro();
        respostaErro.setTitle("Erro acessando banco de dados.");
        respostaErro.setDetail(ex.getMessage());
        respostaErro.setInstance(((ServletWebRequest)request).getRequest().getRequestURI());
        respostaErro.setStatus(HttpStatus.CONFLICT.value());
        return handleExceptionInternal(ex, respostaErro, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }



    }
