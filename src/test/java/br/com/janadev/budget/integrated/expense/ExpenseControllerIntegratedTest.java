package br.com.janadev.budget.integrated.expense;

import br.com.janadev.budget.integrated.config.TestContainersConfig;
import br.com.janadev.budget.primary.expense.dto.ExpenseRequestDTO;
import br.com.janadev.budget.primary.expense.dto.ExpenseResponseDTO;
import br.com.janadev.budget.primary.handler.ErrorResponse;
import br.com.janadev.budget.primary.income.dto.IncomeResponseDTO;
import br.com.janadev.budget.secondary.expense.ExpenseDBO;
import br.com.janadev.budget.secondary.expense.ExpenseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static br.com.janadev.budget.domain.expense.exception.ExpenseErrorMessages.EXPENSE_DELETE_FAILED_NOT_FOUND;
import static br.com.janadev.budget.domain.expense.exception.ExpenseErrorMessages.EXPENSE_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void shouldGetExpenseDetailsSuccessfully(){
        var expenseExpected = expenseRepository.save(ExpenseDBO.of("Luz", 150.0,
                LocalDate.of(2025, Month.FEBRUARY, 1))
        );

        ResponseEntity<ExpenseResponseDTO> responseEntity =
                restTemplate.getForEntity("/expenses/{id}", ExpenseResponseDTO.class, expenseExpected.getId());

        ExpenseResponseDTO response = responseEntity.getBody();

        assertEquals(200, responseEntity.getStatusCode().value());
        assertAll(
                () -> assertEquals(expenseExpected.getId(), response.id()),
                () -> assertEquals(expenseExpected.getDescription(), response.description()),
                () -> assertEquals(expenseExpected.getAmount(), response.amount()),
                () -> assertEquals(expenseExpected.getDate(), response.date())
        );
    }

    @Test
    void shouldRespondWithStatus400WhenTryGetDetailsOfExpenseThatNotExist(){
        ResponseEntity<ErrorResponse> responseEntity =
                restTemplate.getForEntity("/expenses/{id}", ErrorResponse.class, 2);

        ErrorResponse errorResponse = responseEntity.getBody();

        assertEquals(400, responseEntity.getStatusCode().value());
        assertAll(
                () -> assertEquals(EXPENSE_NOT_FOUND, errorResponse.getMessage()),
                () -> assertEquals("DomainNotFoundException", errorResponse.getException()),
                () -> assertEquals("/expenses/2", errorResponse.getPath())
        );
    }
    
    @Test
    void shouldFindAllExpensesSuccessfully(){
        List<ExpenseDBO> expenseExpected = List.of(
                ExpenseDBO.of("Luz", 150.0, LocalDate.of(2025, Month.JANUARY, 29)),
                ExpenseDBO.of("Gás", 30.0, LocalDate.of(2025, Month.JANUARY, 30))
        );
        
        expenseRepository.saveAll(expenseExpected);

        ResponseEntity<List<IncomeResponseDTO>> responseEntity = restTemplate.exchange("/expenses", HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {}
        );

        List<IncomeResponseDTO> response = responseEntity.getBody();

        assertEquals(200, responseEntity.getStatusCode().value());
        assertAll(
                () -> assertEquals(expenseExpected.get(0).getId(), response.get(0).id()),
                () -> assertEquals(expenseExpected.get(0).getDescription(), response.get(0).description()),
                () -> assertEquals(expenseExpected.get(0).getAmount(), response.get(0).amount()),
                () -> assertEquals(expenseExpected.get(0).getDate(), response.get(0).date()),
                () -> assertEquals(expenseExpected.get(1).getId(), response.get(1).id()),
                () -> assertEquals(expenseExpected.get(1).getDescription(), response.get(1).description()),
                () -> assertEquals(expenseExpected.get(1).getAmount(), response.get(1).amount()),
                () -> assertEquals(expenseExpected.get(1).getDate(), response.get(1).date())
        );
    }

    @Test
    void shouldReturnStatusCode200WhenThereNotExpenses(){
        ResponseEntity<List<IncomeResponseDTO>> responseEntity = restTemplate.exchange("/expenses", HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {}
        );

        List<IncomeResponseDTO> response = responseEntity.getBody();

        assertEquals(200, responseEntity.getStatusCode().value());
        assertTrue(response.isEmpty());
    }

    @Test
    void shouldDeleteExpenseSuccessfully(){
        ExpenseDBO expenseSaved = expenseRepository.save(
                ExpenseDBO.of("Luz", 150.0, LocalDate.of(2025, Month.JANUARY, 29))
        );

        ResponseEntity<Void> responseEntity = restTemplate.exchange("/expenses/{id}", HttpMethod.DELETE,
                null, Void.class, expenseSaved.getId());

        assertEquals(204, responseEntity.getStatusCode().value());
        assertFalse(expenseRepository.findById(expenseSaved.getId()).isPresent());
    }

    @Test
    void shouldThrowDomainNotFoundExceptionWhenTryDeleteExpenseThatNotExist(){
        ResponseEntity<ErrorResponse> responseEntity = restTemplate.exchange("/expenses/{id}", HttpMethod.DELETE,
                null, ErrorResponse.class, 2);

        ErrorResponse errorResponse = responseEntity.getBody();

        assertEquals(400, responseEntity.getStatusCode().value());
        assertAll(
                () -> assertEquals(EXPENSE_DELETE_FAILED_NOT_FOUND, errorResponse.getMessage()),
                () -> assertEquals("DomainNotFoundException", errorResponse.getException()),
                () -> assertEquals("/expenses/2", errorResponse.getPath())
        );
    }
}
