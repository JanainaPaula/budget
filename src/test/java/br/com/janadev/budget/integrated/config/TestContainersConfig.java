package br.com.janadev.budget.integrated.config;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public abstract class TestContainersConfig {

    protected static final String USER_ADMIN_EMAIL = "admin@test.com";
    protected static final String USER_ADMIN_PASSWORD = "12345";
    private static final String JWT_TOKEN_SECRET = "12345678";
    private static final MySQLContainer<?> mysqlContainer;

    static {
        System.setProperty("testcontainers.reuse.enable", "true");
        mysqlContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.2.0"))
                .withReuse(true);
        mysqlContainer.start();
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
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("api.security.token.secret", ()-> JWT_TOKEN_SECRET);
        registry.add("user.admin.default.email", ()-> USER_ADMIN_EMAIL);
        registry.add("user.admin.default.password", ()-> USER_ADMIN_PASSWORD);
    }
}
