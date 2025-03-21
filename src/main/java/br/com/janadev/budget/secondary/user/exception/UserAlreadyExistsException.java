package br.com.janadev.budget.secondary.user.exception;

import br.com.janadev.budget.secondary.exception.SecondaryException;

public class UserAlreadyExistsException extends SecondaryException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
