package br.com.janadev.budget.inbound.user.port;

import br.com.janadev.budget.outbound.user.dbo.UserDBO;

import java.util.Set;

public interface UserOutboundPort {
    UserDBO register(String email, String password, Set<String> roles);
    void delete(Long id);
    UserDBO update(Long id, String email, String password);
}
