package br.com.janadev.budget.secondary.auth.jwt;

import br.com.janadev.budget.secondary.auth.user.BudgetUserDetails;

public interface TokenServicePort {
    String generate(BudgetUserDetails user);
    String getSubject(String jwtToken);
}
