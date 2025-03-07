package br.com.janadev.budget.secondary.auth;

import br.com.janadev.budget.secondary.user.UserDBO;

public interface TokenServicePort {
    String generate(BudgetUserDetails user);
}
