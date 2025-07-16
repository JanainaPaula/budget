package br.com.janadev.budget.outbound.auth.user;

import br.com.janadev.budget.outbound.user.dbo.UserDBO;

public interface UserAuthDatabasePort {
    UserDBO getUserByUsername(String username);
}
