package br.com.janadev.budget.secondary.user.exception;

import br.com.janadev.budget.secondary.exception.SecondaryException;

public class UserNotFoundException extends SecondaryException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
