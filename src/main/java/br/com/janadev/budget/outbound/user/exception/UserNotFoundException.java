package br.com.janadev.budget.outbound.user.exception;

import br.com.janadev.budget.outbound.exception.OutboundException;

public class UserNotFoundException extends OutboundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
