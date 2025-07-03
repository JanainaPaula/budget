package br.com.janadev.budget.outbound.user.adapter.port;

import br.com.janadev.budget.outbound.user.dbo.UserDBO;

public interface UserPort {
    UserDBO findById(Long id);
}
