package br.com.janadev.budget.integrated.config;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public abstract class TestContainersConfig {

    @Container
    private static MySQLContainer<?> mysqlContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.2.0"));

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeAll
    static void beforeAll(){
        mysqlContainer.start();
    }

    @AfterAll
    static void afterAll(){
        mysqlContainer.stop();
    }

    @AfterEach
    void tearDown(){
        jdbcTemplate.execute("TRUNCATE TABLE incomes");
        jdbcTemplate.execute("TRUNCATE TABLE expenses");
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }
}
