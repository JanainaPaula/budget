package br.com.janadev.budget.secondary.user.exception;

import br.com.janadev.budget.secondary.exception.SecondaryException;

public class UsernameNotFoundException extends SecondaryException {
    public UsernameNotFoundException(String message) {
        super(message);
    }
}
