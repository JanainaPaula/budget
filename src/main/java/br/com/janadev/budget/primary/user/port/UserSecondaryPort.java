package br.com.janadev.budget.primary.user.port;

import br.com.janadev.budget.secondary.user.dbo.UserDBO;

import java.util.Set;

public interface UserSecondaryPort {
    UserDBO register(String email, String password, Set<String> roles);
    void delete(Long id);
    UserDBO update(Long id, UserDBO user);
}
