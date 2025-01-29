package br.com.janadev.budget.integrated.expense;

import br.com.janadev.budget.integrated.config.TestContainersConfig;
import br.com.janadev.budget.primary.expense.dto.ExpenseRequestDTO;
import br.com.janadev.budget.primary.expense.dto.ExpenseResponseDTO;
import br.com.janadev.budget.secondary.expense.ExpenseDBO;
import br.com.janadev.budget.secondary.expense.ExpenseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureJsonTesters
public class ExpenseControllerIntegratedTest extends TestContainersConfig {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Test
    void shouldRegisterExpenseSuccessfully(){
        var request = new ExpenseRequestDTO("Luz", 150.0,
                LocalDate.of(2025, Month.JANUARY, 28));

        ResponseEntity<ExpenseResponseDTO> responseEntity =
                restTemplate.postForEntity("/expenses", request, ExpenseResponseDTO.class);

        ExpenseResponseDTO response = responseEntity.getBody();

        assertEquals(201, responseEntity.getStatusCode().value());
        assertAll(
                () -> assertNotNull(response.id()),
                () -> assertEquals(response.description(), request.description()),
                () -> assertEquals(response.amount(), request.amount()),
                () -> assertEquals(response.date(), request.date())
        );
    }

    @Test
    void shouldThrowExpenseAlreadyExistExceptionWhenTryRegisterExpenseWithDescriptionThatAlreadyExistInTheMonth() throws IOException {
        var januaryLuz = ExpenseDBO.of("Luz", 150.0,
                LocalDate.of(2025, Month.JANUARY, 28));
        var januaryGas = ExpenseDBO.of("Gás", 30.0,
                LocalDate.of(2025, Month.JANUARY, 27));

        expenseRepository.saveAll(List.of(januaryLuz, januaryGas));

        var request = new ExpenseRequestDTO("Gás", 50.0,
                LocalDate.of(2025, Month.JANUARY, 28));

        ResponseEntity<ExpenseResponseDTO> responseEntity =
                restTemplate.postForEntity("/expenses", request, ExpenseResponseDTO.class);

        assertEquals(400, responseEntity.getStatusCode().value());

        var requestGasToFebruary = new ExpenseRequestDTO("Gás", 50.0,
                LocalDate.of(2025, Month.FEBRUARY, 2));

        ResponseEntity<ExpenseResponseDTO> responseEntity1 =
                restTemplate.postForEntity("/expenses", requestGasToFebruary, ExpenseResponseDTO.class);

        ExpenseResponseDTO response = responseEntity1.getBody();

        assertEquals(201, responseEntity1.getStatusCode().value());
        assertAll(
                () -> assertNotNull(response.id()),
                () -> assertEquals(requestGasToFebruary.description(), response.description()),
                () -> assertEquals(requestGasToFebruary.amount(), response.amount()),
                () -> assertEquals(requestGasToFebruary.date(), response.date())
        );
    }
}
