package br.com.janadev.budget.secondary.auth.user;

import br.com.janadev.budget.secondary.user.dbo.UserDBO;

public interface UserAuthDatabasePort {
    UserDBO getUserByUsername(String username);
}
