package br.com.janadev.budget.secondary.auth.user.service;

import br.com.janadev.budget.secondary.auth.user.UserDBO;

public interface UserServicePort {
    UserDBO register(UserDBO user);
    UserDBO getUserByUsername(String username);
    void delete(Long id);
}
