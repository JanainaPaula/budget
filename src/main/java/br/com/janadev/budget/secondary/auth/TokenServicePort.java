package br.com.janadev.budget.secondary.auth;

public interface TokenServicePort {
    String generate(BudgetUserDetails user);
    String getSubject(String jwtToken);
}
