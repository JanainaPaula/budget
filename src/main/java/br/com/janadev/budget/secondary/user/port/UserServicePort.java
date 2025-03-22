package br.com.janadev.budget.secondary.user.port;

import br.com.janadev.budget.secondary.user.dbo.UserDBO;

public interface UserServicePort {
    UserDBO findById(Long id);
}
