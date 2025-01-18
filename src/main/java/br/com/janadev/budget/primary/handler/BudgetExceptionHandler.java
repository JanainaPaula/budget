package br.com.janadev.budget.primary.handler;

import br.com.janadev.budget.domain.exceptions.DomainValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class BudgetExceptionHandler {

    @ExceptionHandler(DomainValidationException.class)
    private ResponseEntity<ErrorResponse> domainValidationException(DomainValidationException exception, WebRequest request){
        var errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST.toString(), exception,
                request.getDescription(false));
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<ErrorResponse> genericException(Exception exception, WebRequest request){
        var errorResponse = ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.toString(), exception,
                request.getDescription(false));
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
