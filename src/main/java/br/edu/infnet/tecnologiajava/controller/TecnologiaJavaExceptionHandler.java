package br.edu.infnet.tecnologiajava.controller;

import br.edu.infnet.tecnologiajava.model.domain.ValidadorException;
import br.edu.infnet.tecnologiajava.model.view.RespostaErro;
import br.edu.infnet.tecnologiajava.services.mapper.MapperException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
public class TecnologiaJavaExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {MapperException.class})
    protected ResponseEntity<Object> erroMapeamentoEValidacao(MapperException ex, WebRequest request){
        RespostaErro respostaErro = new RespostaErro();
        respostaErro.setMensagem("Conteúdo JSON inválido.");
        if(ex.getCause()==null || !(ex.getCause() instanceof ValidadorException)) {
            respostaErro.setDetalhes(List.of(ex.getMessage()));
        }
        else {
            ValidadorException validadorException = (ValidadorException) ex.getCause();
            respostaErro.setDetalhes(validadorException.getValidador().getMensagens());
        }
        return handleExceptionInternal(ex, respostaErro, new HttpHeaders(), HttpStatus.valueOf(422), request);
    }
}
