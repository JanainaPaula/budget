package br.com.janadev.budget.outbound.user.exception;

import br.com.janadev.budget.outbound.exception.OutboundException;

public class UserAlreadyExistsException extends OutboundException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
