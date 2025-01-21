package br.com.janadev.budget.integrated.income;

import br.com.janadev.budget.integrated.config.TestContainersConfig;
import br.com.janadev.budget.primary.income.dto.IncomeRequestDTO;
import br.com.janadev.budget.primary.income.dto.IncomeResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IncomeControllerIntegratedTest extends TestContainersConfig {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldRegisterIncomeSuccessfully(){
        var requestBody = new IncomeRequestDTO("Vendas Enjoei",
                2000.0,
                LocalDate.of(2025, Month.JANUARY, 21));

        ResponseEntity<IncomeResponseDTO> responseEntity =
                restTemplate.postForEntity("/incomes", requestBody, IncomeResponseDTO.class);

        IncomeResponseDTO response = responseEntity.getBody();

        assertEquals(201, responseEntity.getStatusCode().value());
        assertAll(
                () -> assertNotNull(response.id()),
                () -> assertEquals(requestBody.description(), response.description()),
                () -> assertEquals(requestBody.amount(), response.amount()),
                () -> assertEquals(requestBody.date(), response.date())
        );
    }
}