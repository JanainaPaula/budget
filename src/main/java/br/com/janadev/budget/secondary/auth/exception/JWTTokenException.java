package br.com.janadev.budget.secondary.auth.exception;

import br.com.janadev.budget.secondary.exception.SecondaryException;

public class JWTTokenException extends SecondaryException {
    public JWTTokenException(String message) {
        super(message);
    }
}
