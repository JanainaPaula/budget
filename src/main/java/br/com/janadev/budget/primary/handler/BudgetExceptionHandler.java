package br.com.janadev.budget.primary.handler;

import br.com.janadev.budget.domain.exceptions.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class BudgetExceptionHandler {

    @ExceptionHandler(DomainException.class)
    private ResponseEntity<ErrorResponse> domainValidationException(DomainException exception, WebRequest request){
        var errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST.toString(), exception,
                request.getDescription(false));
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
