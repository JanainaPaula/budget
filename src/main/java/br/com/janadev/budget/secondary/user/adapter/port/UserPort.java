package br.com.janadev.budget.secondary.user.adapter.port;

import br.com.janadev.budget.secondary.user.dbo.UserDBO;

public interface UserPort {
    UserDBO findById(Long id);
}
