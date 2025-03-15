package br.com.janadev.budget.primary.user;

import br.com.janadev.budget.secondary.user.dbo.UserDBO;

public interface UserSecondaryPort {
    UserDBO register(UserDBO user);
    void delete(Long id);
    UserDBO update(Long id, UserDBO user);
}
