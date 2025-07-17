package br.com.janadev.budget.inbound.handler;

import br.com.janadev.budget.domain.exceptions.DomainException;
import br.com.janadev.budget.outbound.exception.OutboundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class BudgetExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(BudgetExceptionHandler.class);


    @ExceptionHandler(DomainException.class)
    private ResponseEntity<ErrorResponse> domainValidationException(DomainException exception, WebRequest request){
        logger.warn("Domain error [{}]: {} | Path: {}", exception.getClass().getSimpleName(),
                exception.getMessage(), request.getDescription(false));
        var errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST.toString(), exception,
                request.getDescription(false));
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(OutboundException.class)
    private ResponseEntity<ErrorResponse> outboundException(OutboundException exception, WebRequest request){
        logger.warn("Outbound error [{}]: {} | Path: {}", exception.getClass().getSimpleName(),
                exception.getMessage(), request.getDescription(false));
        var errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST.toString(), exception,
                request.getDescription(false));
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    private ResponseEntity<ErrorResponse> usernameNotFoundException(BadCredentialsException exception, WebRequest request){
        logger.warn("Login attempt with invalid credentials [{}]: {} | Path: {}", exception.getClass().getSimpleName(),
                exception.getMessage(), request.getDescription(false));
        var errorResponse = ErrorResponse.of(HttpStatus.UNAUTHORIZED.toString(), exception,
                request.getDescription(false));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException exception, WebRequest request){
        logger.warn("Validation error [{}]: {} | Path: {}", exception.getClass().getSimpleName(),
                exception.getMessage(), request.getDescription(false));
        var errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST.toString(), exception,
                request.getDescription(false));
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    private ResponseEntity<ErrorResponse> accessDeniedException(AccessDeniedException exception, WebRequest request){
        logger.warn("Access denied [{}]: {} | Path: {}", exception.getClass().getSimpleName(),
                exception.getMessage(), request.getDescription(false));
        var errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN.toString(), exception,
                request.getDescription(false));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<ErrorResponse> exception(Exception exception, WebRequest request){
        logger.error("Unexpected error [{}]: {} | Path: {}", exception.getClass().getSimpleName(),
                exception.getMessage(), request.getDescription(false), exception);
        var errorResponse = ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.toString(), exception,
                request.getDescription(false));
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
