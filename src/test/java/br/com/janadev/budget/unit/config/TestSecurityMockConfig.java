package br.com.janadev.budget.unit.config;

import br.com.janadev.budget.primary.utils.AuthUserUtil;
import br.com.janadev.budget.secondary.auth.config.SecurityFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mockStatic;

public abstract class TestSecurityMockConfig {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockitoBean
    protected SecurityFilter securityFilter;
    protected MockedStatic<AuthUserUtil> mockedStatic;

    @BeforeEach
    void setUp(){
        mockedStatic = mockStatic(AuthUserUtil.class);
        mockedStatic.when(AuthUserUtil::getAuthenticatedUserId).thenReturn(3L);
        JacksonTester.initFields(this, objectMapper);
    }

    @AfterEach
    void tearDown(){
        mockedStatic.close();
    }
}
