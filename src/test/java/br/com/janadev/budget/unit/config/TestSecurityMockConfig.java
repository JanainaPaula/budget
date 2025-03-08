package br.com.janadev.budget.unit.config;

import br.com.janadev.budget.secondary.auth.SecurityFilter;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

public abstract class TestSecurityMockConfig {
    @MockitoBean
    public SecurityFilter securityFilter;
}
