package br.com.janadev.budget.outbound.auth.exception;

import br.com.janadev.budget.outbound.exception.OutboundException;

public class JWTTokenException extends OutboundException {
    public JWTTokenException(String message) {
        super(message);
    }
}
