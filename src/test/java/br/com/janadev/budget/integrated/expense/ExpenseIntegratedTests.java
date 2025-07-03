package br.com.janadev.budget.integrated.expense;

import br.com.janadev.budget.domain.expense.Category;
import br.com.janadev.budget.integrated.config.IntegratedTestBaseConfig;
import br.com.janadev.budget.inbound.expense.dto.ExpenseRequestDTO;
import br.com.janadev.budget.inbound.expense.dto.ExpenseResponseDTO;
import br.com.janadev.budget.inbound.handler.ErrorResponse;
import br.com.janadev.budget.inbound.income.dto.IncomeResponseDTO;
import br.com.janadev.budget.outbound.expense.ExpenseDBO;
import br.com.janadev.budget.outbound.expense.ExpenseRepository;
import br.com.janadev.budget.outbound.user.dbo.UserDBO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

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
public class ExpenseIntegratedTests extends IntegratedTestBaseConfig {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Test
    void shouldRegisterExpenseSuccessfully(){
        var request = new ExpenseRequestDTO("Luz", 150.0,
                LocalDate.of(2025, Month.JANUARY, 28), Category.HOUSE.getName());

        HttpEntity<ExpenseRequestDTO> entity = new HttpEntity<>(request, getAuthorizationHeader());

        ResponseEntity<ExpenseResponseDTO> responseEntity =
                restTemplate.exchange("/expenses", HttpMethod.POST, entity, ExpenseResponseDTO.class);

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
        UserDBO user = getUser();
        var januaryLuz = ExpenseDBO.of("Luz", 150.0,
                LocalDate.of(2025, Month.JANUARY, 28), Category.HOUSE.getName(), user);
        var januaryGas = ExpenseDBO.of("Gás", 30.0,
                LocalDate.of(2025, Month.JANUARY, 27), Category.HOUSE.getName(), user);

        expenseRepository.saveAll(List.of(januaryLuz, januaryGas));

        var request = new ExpenseRequestDTO("Gás", 50.0,
                LocalDate.of(2025, Month.JANUARY, 28), Category.HOUSE.getName());

        HttpEntity<ExpenseRequestDTO> entity = new HttpEntity<>(request, getAuthorizationHeader());
        ResponseEntity<ExpenseResponseDTO> responseEntity =
                restTemplate.exchange("/expenses", HttpMethod.POST, entity, ExpenseResponseDTO.class);

        assertEquals(400, responseEntity.getStatusCode().value());

        var requestGasToFebruary = new ExpenseRequestDTO("Gás", 50.0,
                LocalDate.of(2025, Month.FEBRUARY, 2), Category.HOUSE.getName());


        HttpEntity<ExpenseRequestDTO> entity1 = new HttpEntity<>(requestGasToFebruary, getAuthorizationHeader());
        ResponseEntity<ExpenseResponseDTO> responseEntity1 =
                restTemplate.exchange("/expenses", HttpMethod.POST, entity1, ExpenseResponseDTO.class);

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
        UserDBO user = getUser();
        var expenseExpected = expenseRepository.save(ExpenseDBO.of("Luz", 150.0,
                LocalDate.of(2025, Month.FEBRUARY, 1), "House", user)
        );

        HttpEntity<String> entity = new HttpEntity<>(getAuthorizationHeader());
        ResponseEntity<ExpenseResponseDTO> responseEntity =
                restTemplate.exchange("/expenses/{id}", HttpMethod.GET, entity, ExpenseResponseDTO.class, expenseExpected.getId());

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
        HttpEntity<String> entity = new HttpEntity<>(getAuthorizationHeader());
        ResponseEntity<ErrorResponse> responseEntity =
                restTemplate.exchange("/expenses/{id}", HttpMethod.GET, entity, ErrorResponse.class, 2);

        ErrorResponse errorResponse = responseEntity.getBody();

        assertEquals(400, responseEntity.getStatusCode().value());
        assertAll(
                () -> assertEquals(EXPENSE_NOT_FOUND.getMessage(), errorResponse.getMessage()),
                () -> assertEquals("DomainNotFoundException", errorResponse.getException()),
                () -> assertEquals("/expenses/2", errorResponse.getPath())
        );
    }
    
    @Test
    void shouldFindAllExpensesSuccessfully(){
        UserDBO user = getUser();
        List<ExpenseDBO> expenseExpected = List.of(
                ExpenseDBO.of("Luz", 150.0, LocalDate.of(2025, Month.JANUARY, 29),
                        Category.HOUSE.getName(), user),
                ExpenseDBO.of("Gás", 30.0, LocalDate.of(2025, Month.JANUARY, 30),
                        Category.HOUSE.getName(), user)
        );
        
        expenseRepository.saveAll(expenseExpected);

        HttpEntity<String> entity = new HttpEntity<>(getAuthorizationHeader());
        ResponseEntity<List<IncomeResponseDTO>> responseEntity = restTemplate.exchange("/expenses", HttpMethod.GET,
                entity,
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
        HttpEntity<String> entity = new HttpEntity<>(getAuthorizationHeader());
        ResponseEntity<List<IncomeResponseDTO>> responseEntity = restTemplate.exchange("/expenses", HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        List<IncomeResponseDTO> response = responseEntity.getBody();

        assertEquals(200, responseEntity.getStatusCode().value());
        assertTrue(response.isEmpty());
    }

    @Test
    void shouldDeleteExpenseSuccessfully(){
        UserDBO user = getUser();
        ExpenseDBO expenseSaved = expenseRepository.save(
                ExpenseDBO.of("Luz", 150.0, LocalDate.of(2025, Month.JANUARY, 29),
                        Category.HOUSE.getName(), user)
        );

        HttpEntity<String> entity = new HttpEntity<>(getAuthorizationHeader());
        ResponseEntity<Void> responseEntity = restTemplate.exchange("/expenses/{id}", HttpMethod.DELETE,
                entity, Void.class, expenseSaved.getId());

        assertEquals(204, responseEntity.getStatusCode().value());
        assertFalse(expenseRepository.findById(expenseSaved.getId()).isPresent());
    }

    @Test
    void shouldThrowDomainNotFoundExceptionWhenTryDeleteExpenseThatNotExist(){
        HttpEntity<String> entity = new HttpEntity<>(getAuthorizationHeader());
        ResponseEntity<ErrorResponse> responseEntity = restTemplate.exchange("/expenses/{id}", HttpMethod.DELETE,
                entity, ErrorResponse.class, 2);

        ErrorResponse errorResponse = responseEntity.getBody();

        assertEquals(400, responseEntity.getStatusCode().value());
        assertAll(
                () -> assertEquals(EXPENSE_DELETE_FAILED_NOT_FOUND.getMessage(), errorResponse.getMessage()),
                () -> assertEquals("DomainNotFoundException", errorResponse.getException()),
                () -> assertEquals("/expenses/2", errorResponse.getPath())
        );
    }

    @Test
    void shouldUpdateExpenseSuccessfully(){
        UserDBO user = getUser();
        var expenseDBO = expenseRepository.save(ExpenseDBO.of("Luz", 150.0,
                LocalDate.of(2025, Month.JANUARY, 30), Category.HOUSE.getName(), user));

        var expenseRequest = new ExpenseRequestDTO("Gás", 50.0,
                LocalDate.of(2025, Month.JANUARY, 30), Category.HOUSE.getName());
        HttpEntity<ExpenseRequestDTO> request = new HttpEntity<>(expenseRequest, getAuthorizationHeader());

        ResponseEntity<ExpenseResponseDTO> responseEntity =
                restTemplate.exchange("/expenses/{id}", HttpMethod.PUT, request,
                        ExpenseResponseDTO.class, expenseDBO.getId());

        ExpenseResponseDTO response = responseEntity.getBody();

        assertEquals(200, responseEntity.getStatusCode().value());
        assertAll(
                () -> assertEquals(expenseDBO.getId(), response.id()),
                () -> assertEquals("Gás", response.description()),
                () -> assertEquals(50.0, response.amount()),
                () -> assertEquals(expenseDBO.getDate(), response.date())
        );
    }

    @Test
    void shouldFindExpensesByDescriptionSuccessfully(){
        UserDBO user = getUser();
        List<ExpenseDBO> expensesExpected = List.of(
                ExpenseDBO.of("Luz", 150.0,
                        LocalDate.of(2025, Month.FEBRUARY, 15), Category.HOUSE.getName(), user),
                ExpenseDBO.of("Mensalidade da Faculdade", 1550.0,
                        LocalDate.of(2025, Month.FEBRUARY, 15), Category.EDUCATION.getName(), user)
        );
        expenseRepository.saveAll(expensesExpected);

        String uri = UriComponentsBuilder.fromPath("/expenses")
                .queryParam("description", "faculdade")
                .toUriString();
        HttpEntity<String> entity = new HttpEntity<>(getAuthorizationHeader());
        ResponseEntity<List<ExpenseResponseDTO>> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, entity,
                new ParameterizedTypeReference<>() {
        });

        List<ExpenseResponseDTO> response = responseEntity.getBody();

        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals(1, response.size());
        assertAll(
                () -> assertEquals(expensesExpected.get(1).getId(), response.get(0).id()),
                () -> assertEquals(expensesExpected.get(1).getDescription(), response.get(0).description()),
                () -> assertEquals(expensesExpected.get(1).getAmount(), response.get(0).amount()),
                () -> assertEquals(expensesExpected.get(1).getDate(), response.get(0).date())
        );

        String uriGas = UriComponentsBuilder.fromPath("/expenses")
                .queryParam("description", "gas")
                .toUriString();
        HttpEntity<String> entity1 = new HttpEntity<>(getAuthorizationHeader());
        ResponseEntity<List<ExpenseResponseDTO>> responseEntity1 = restTemplate.exchange(uriGas, HttpMethod.GET, entity1,
                new ParameterizedTypeReference<>() {
                });

        List<ExpenseResponseDTO> response1 = responseEntity1.getBody();

        assertEquals(200, responseEntity1.getStatusCode().value());
        assertTrue(response1.isEmpty());
    }

    @Test
    void shouldFindExpensesByMonthSuccessfully(){
        UserDBO user = getUser();
        List<ExpenseDBO> expensesExpected = List.of(
                ExpenseDBO.of("Luz", 150.0,
                        LocalDate.of(2025, Month.FEBRUARY, 15), Category.HOUSE.getName(), user),
                ExpenseDBO.of("Mensalidade da Faculdade", 1550.0,
                        LocalDate.of(2025, Month.JANUARY, 15), Category.EDUCATION.getName(), user)
        );
        expenseRepository.saveAll(expensesExpected);

        HttpEntity<String> entity = new HttpEntity<>(getAuthorizationHeader());
        ResponseEntity<List<ExpenseResponseDTO>> responseEntity = restTemplate.exchange("/expenses/{year}/{month}",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {},
                2025,
                2
        );

        List<ExpenseResponseDTO> response = responseEntity.getBody();

        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals(1, response.size());
        assertAll(
                () -> assertEquals(expensesExpected.get(0).getId(), response.get(0).id()),
                () -> assertEquals(expensesExpected.get(0).getDescription(), response.get(0).description()),
                () -> assertEquals(expensesExpected.get(0).getAmount(), response.get(0).amount()),
                () -> assertEquals(expensesExpected.get(0).getDate(), response.get(0).date()),
                () -> assertEquals(expensesExpected.get(0).getCategory(), response.get(0).category())
        );

        HttpEntity<String> entity1 = new HttpEntity<>(getAuthorizationHeader());
        ResponseEntity<List<ExpenseResponseDTO>> responseEntity1 = restTemplate.exchange("/expenses/{year}/{month}",
                HttpMethod.GET,
                entity1,
                new ParameterizedTypeReference<>() {},
                2024,
                2
        );

        List<ExpenseResponseDTO> response1 = responseEntity1.getBody();

        assertEquals(200, responseEntity1.getStatusCode().value());
        assertTrue(response1.isEmpty());
    }
}
