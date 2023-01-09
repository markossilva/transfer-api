package br.com.itau.transferapi.application.exception;

import br.com.itau.transferapi.domain.exception.DomainException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  protected ResponseEntity<Object> handleEntityNotFound(
      EntityNotFoundException ex) {
    return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, ex));
  }

  @ExceptionHandler(DomainException.class)
  protected ResponseEntity<?> handleDomainException(DomainException ex) {
    return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex));
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                HttpHeaders headers, HttpStatus status,
                                                                WebRequest request) {
    return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex));
  }

  private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }
}
