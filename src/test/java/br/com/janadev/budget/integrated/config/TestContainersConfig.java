package br.com.janadev.budget.integrated.config;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public abstract class TestContainersConfig {

    protected static final String USER_ADMIN_EMAIL = "admin@test.com";
    protected static final String USER_ADMIN_PASSWORD = "12345";
    private static final String JWT_TOKEN_SECRET = "12345678";
    private static final PostgreSQLContainer<?> postgresContainer;

    static {
        System.setProperty("testcontainers.reuse.enable", "true");
        postgresContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16"))
                .withReuse(true);
        postgresContainer.start();
    }
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @AfterEach
    void tearDown(){
        jdbcTemplate.execute("DELETE FROM incomes");
        jdbcTemplate.execute("DELETE FROM expenses");
    }
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("api.security.token.secret", ()-> JWT_TOKEN_SECRET);
        registry.add("user.admin.default.email", ()-> USER_ADMIN_EMAIL);
        registry.add("user.admin.default.password", ()-> USER_ADMIN_PASSWORD);
    }
}
