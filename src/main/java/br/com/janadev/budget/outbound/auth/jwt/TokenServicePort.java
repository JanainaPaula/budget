package br.com.janadev.budget.outbound.auth.jwt;

import br.com.janadev.budget.outbound.auth.user.BudgetUserDetails;

public interface TokenServicePort {
    String generate(BudgetUserDetails user);
    String getSubject(String jwtToken);
}
