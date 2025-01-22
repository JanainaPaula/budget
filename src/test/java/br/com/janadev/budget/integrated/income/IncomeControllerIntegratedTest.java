package br.com.janadev.budget.integrated.income;

import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.integrated.config.TestContainersConfig;
import br.com.janadev.budget.primary.income.dto.IncomeRequestDTO;
import br.com.janadev.budget.primary.income.dto.IncomeResponseDTO;
import br.com.janadev.budget.secondary.income.IncomeDBO;
import br.com.janadev.budget.secondary.income.IncomeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IncomeControllerIntegratedTest extends TestContainersConfig {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private IncomeRepository incomeRepository;

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

    @Test
    void shouldFindAllIncomesSuccessfully(){
        Income salario = Income.of("Salario", 5000.0, LocalDate.of(2025, Month.JANUARY, 21));
        Income vendas = Income.of("Vendas enjoei", 700.0, LocalDate.of(2025, Month.JANUARY, 21));

        List<IncomeDBO> incomesExpected = incomeRepository.saveAll(
                List.of(IncomeDBO.toIncomeDBO(salario), IncomeDBO.toIncomeDBO(vendas)));

        ResponseEntity<List<IncomeResponseDTO>> responseEntity = restTemplate.exchange("/incomes", HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {}
        );

        List<IncomeResponseDTO> response = responseEntity.getBody();

        assertEquals(200, responseEntity.getStatusCode().value());
        assertAll(
                () -> assertEquals(2, response.size()),
                () -> assertNotNull(response.get(0).id()),
                () -> assertNotNull(response.get(1).id()),
                () -> assertEquals(incomesExpected.get(0).getId(), response.get(0).id()),
                () -> assertEquals(incomesExpected.get(1).getId(), response.get(1).id()),
                () -> assertEquals(incomesExpected.get(0).getDescription(), response.get(0).description()),
                () -> assertEquals(incomesExpected.get(1).getDescription(), response.get(1).description()),
                () -> assertEquals(incomesExpected.get(0).getAmount(), response.get(0).amount()),
                () -> assertEquals(incomesExpected.get(1).getAmount(), response.get(1).amount()),
                () -> assertEquals(incomesExpected.get(0).getDate(), response.get(0).date()),
                () -> assertEquals(incomesExpected.get(1).getDate(), response.get(1).date())
        );
    }
}