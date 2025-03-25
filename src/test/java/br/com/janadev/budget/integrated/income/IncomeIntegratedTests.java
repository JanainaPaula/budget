package br.com.janadev.budget.integrated.income;

import br.com.janadev.budget.integrated.config.IntegratedTestBaseConfig;
import br.com.janadev.budget.primary.handler.ErrorResponse;
import br.com.janadev.budget.primary.income.dto.IncomeRequestDTO;
import br.com.janadev.budget.primary.income.dto.IncomeResponseDTO;
import br.com.janadev.budget.secondary.income.IncomeDBO;
import br.com.janadev.budget.secondary.income.IncomeRepository;
import br.com.janadev.budget.secondary.user.dbo.UserDBO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static br.com.janadev.budget.domain.income.exception.IncomeErrorMessages.INCOME_DELETE_FAILED_NOT_FOUND;
import static br.com.janadev.budget.domain.income.exception.IncomeErrorMessages.INCOME_WITH_THIS_DESCRIPTION_ALREADY_EXISTS;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IncomeIntegratedTests extends IntegratedTestBaseConfig {

    @Autowired
    private IncomeRepository incomeRepository;

    @Test
    void shouldRegisterIncomeSuccessfully(){
        var requestBody = new IncomeRequestDTO("Vendas Enjoei",
                2000.0,
                LocalDate.of(2025, Month.JANUARY, 21));
        HttpEntity<IncomeRequestDTO> entity = new HttpEntity<>(requestBody, getAuthorizationHeader());
        ResponseEntity<IncomeResponseDTO> responseEntity =
                restTemplate.exchange("/incomes", HttpMethod.POST, entity, IncomeResponseDTO.class);

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
    void shouldRespondStatus400WhenTryRegisterIncomeWithDescriptionThatAlreadyExists(){
        UserDBO user = getUser();
        var incomeDBO = IncomeDBO.of("Salário", 2000.0,
                LocalDate.of(2025, Month.JANUARY, 23), user);
        incomeRepository.save(incomeDBO);

        var request = new IncomeRequestDTO("Salário", 1000.0,
                LocalDate.of(2025, Month.JANUARY, 21));
        HttpEntity<IncomeRequestDTO> entity = new HttpEntity<>(request, getAuthorizationHeader());
        ResponseEntity<ErrorResponse> responseEntity =
                restTemplate.exchange("/incomes", HttpMethod.POST, entity, ErrorResponse.class);

        ErrorResponse errorResponse = responseEntity.getBody();

        assertEquals(400, responseEntity.getStatusCode().value());
        assertAll(
                () -> assertEquals(INCOME_WITH_THIS_DESCRIPTION_ALREADY_EXISTS.getMessage(), errorResponse.getMessage()),
                () -> assertEquals("IncomeAlreadyExistsException", errorResponse.getException()),
                () -> assertEquals("/incomes", errorResponse.getPath())
        );

        var requestIncomeInFebruary = new IncomeRequestDTO("Salário", 1000.0,
                LocalDate.of(2025, Month.FEBRUARY, 21));
        HttpEntity<IncomeRequestDTO> entity1 = new HttpEntity<>(requestIncomeInFebruary, getAuthorizationHeader());
        ResponseEntity<IncomeResponseDTO> responseEntity1 =
                restTemplate.exchange("/incomes", HttpMethod.POST, entity1, IncomeResponseDTO.class);

        IncomeResponseDTO response = responseEntity1.getBody();

        assertEquals(201, responseEntity1.getStatusCode().value());
        assertAll(
                () -> assertNotNull(response.id()),
                () -> assertEquals(requestIncomeInFebruary.description(), response.description()),
                () -> assertEquals(requestIncomeInFebruary.amount(), response.amount()),
                () -> assertEquals(requestIncomeInFebruary.date(), response.date())
        );
    }

    @Test
    void shouldFindAllIncomesSuccessfully(){
        UserDBO user = getUser();
        List<IncomeDBO> incomesExpected = incomeRepository.saveAll(
                List.of(
                        IncomeDBO.of("Salario", 5000.0,
                                LocalDate.of(2025, Month.JANUARY, 21), user),
                        IncomeDBO.of("Vendas enjoei", 700.0,
                                LocalDate.of(2025, Month.JANUARY, 21), user)
                )
        );
        HttpEntity<String> entity = new HttpEntity<>(getAuthorizationHeader());
        ResponseEntity<List<IncomeResponseDTO>> responseEntity = restTemplate.exchange("/incomes", HttpMethod.GET,
                entity,
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

    @Test
    void shouldGetIncomeDetailsSuccessfully(){
        UserDBO user = getUser();
        IncomeDBO savedIncome = incomeRepository.save(IncomeDBO.of("Salario", 5000.0,
                LocalDate.of(2025, Month.JANUARY, 21), user));

        HttpEntity<String> entity = new HttpEntity<>(getAuthorizationHeader());
        ResponseEntity<IncomeResponseDTO> responseEntity = restTemplate.exchange("/incomes/{id}", HttpMethod.GET,
                entity,
                IncomeResponseDTO.class, savedIncome.getId());

        IncomeResponseDTO response = responseEntity.getBody();

        assertEquals(200, responseEntity.getStatusCode().value());
        assertAll(
                () -> assertNotNull(response.id()),
                () -> assertEquals(savedIncome.getDescription(), response.description()),
                () -> assertEquals(savedIncome.getAmount(), response.amount()),
                () -> assertEquals(savedIncome.getDate(), response.date())
        );
    }

    @Test
    void shouldUpdateIncomeSuccessfully(){
        UserDBO user = getUser();
        IncomeDBO savedIncome = incomeRepository.save(IncomeDBO.of("Salário", 5000.0,
                LocalDate.of(2025, Month.JANUARY, 21), user));

        var incomeRequestDTO = new IncomeRequestDTO("Salário", 6000.0,
                LocalDate.of(2025, Month.JANUARY, 21));

        HttpEntity<IncomeRequestDTO> requestEntity = new HttpEntity<>(incomeRequestDTO, getAuthorizationHeader());

        ResponseEntity<IncomeResponseDTO> responseEntity = restTemplate.exchange("/incomes/{id}", HttpMethod.PUT,
                requestEntity,
                IncomeResponseDTO.class, savedIncome.getId());

        IncomeResponseDTO response = responseEntity.getBody();

        IncomeDBO incomeUpdated = incomeRepository.findById(savedIncome.getId()).get();

        assertEquals(200, responseEntity.getStatusCode().value());
        assertAll(
                () -> assertEquals(savedIncome.getId(), response.id()),
                () -> assertEquals(incomeUpdated.getDescription(), response.description()),
                () -> assertEquals(incomeUpdated.getAmount(), response.amount()),
                () -> assertEquals(incomeUpdated.getDate(), response.date())
        );
    }

    @Test
    void shouldReturnWithStatus400WhenTryUpdateIncomeDescriptionWithADescriptionAlreadyExistsInMonth(){
        UserDBO user = getUser();
        IncomeDBO incomeVendas = incomeRepository.saveAll(List.of(
                IncomeDBO.of("Salário", 7000.0, LocalDate.of(2025, Month.JANUARY, 29), user),
                IncomeDBO.of("Vendas enjoei", 200.0, LocalDate.of(2025, Month.JANUARY, 20), user)
        )).get(1);

        var request = new IncomeRequestDTO("Salário", 1000.0,
                LocalDate.of(2025, Month.JANUARY, 20));

        HttpEntity<IncomeRequestDTO> requestEntity = new HttpEntity<>(request, getAuthorizationHeader());

        ResponseEntity<ErrorResponse> responseEntity = restTemplate.exchange("/incomes/{id}", HttpMethod.PUT,
                requestEntity,
                ErrorResponse.class, incomeVendas.getId());

        ErrorResponse errorResponse = responseEntity.getBody();

        assertEquals(400, responseEntity.getStatusCode().value());
        assertAll(
                () -> assertEquals(INCOME_WITH_THIS_DESCRIPTION_ALREADY_EXISTS.getMessage(), errorResponse.getMessage()),
                () -> assertEquals("IncomeAlreadyExistsException", errorResponse.getException()),
                () -> assertEquals(String.format("/incomes/%s", incomeVendas.getId()), errorResponse.getPath())
        );

        var requestWithOtherDescription = new IncomeRequestDTO("Renda Extra", 1000.0,
                LocalDate.of(2025, Month.JANUARY, 20));

        HttpEntity<IncomeRequestDTO> requestEntity1 = new HttpEntity<>(requestWithOtherDescription, getAuthorizationHeader());

        ResponseEntity<IncomeResponseDTO> responseEntity1 = restTemplate.exchange("/incomes/{id}", HttpMethod.PUT,
                requestEntity1,
                IncomeResponseDTO.class, incomeVendas.getId());

        IncomeResponseDTO response = responseEntity1.getBody();

        assertEquals(200, responseEntity1.getStatusCode().value());
        assertNotNull(response);

    }

    @Test
    void shouldDeleteIncomeSuccessfully(){
        UserDBO user = getUser();
        var incomeDBO = IncomeDBO.of("Venda Enjoei", 1000.0,
                LocalDate.of(2025, Month.JANUARY, 24), user);
        IncomeDBO incomeSaved = incomeRepository.save(incomeDBO);

        HttpEntity<String> entity = new HttpEntity<>(getAuthorizationHeader());
        ResponseEntity<Void> responseEntity = restTemplate.exchange("/incomes/{id}", HttpMethod.DELETE,
                entity, Void.class, incomeSaved.getId());

        assertEquals(204, responseEntity.getStatusCode().value());
        assertFalse(incomeRepository.findById(incomeSaved.getId()).isPresent());
    }

    @Test
    void shouldRespondStatus400WhenTryDeleteIncomeWithIncomeIdDoesNotExists(){
        HttpEntity<String> entity = new HttpEntity<>(getAuthorizationHeader());
        ResponseEntity<ErrorResponse> responseEntity = restTemplate.exchange("/incomes/{id}", HttpMethod.DELETE,
                entity, ErrorResponse.class, 2);

        ErrorResponse errorResponse = responseEntity.getBody();

        assertEquals(400, responseEntity.getStatusCode().value());
        assertAll(
                () -> assertEquals(INCOME_DELETE_FAILED_NOT_FOUND.getMessage(), errorResponse.getMessage()),
                () -> assertEquals("DomainNotFoundException", errorResponse.getException()),
                () -> assertEquals("/incomes/2", errorResponse.getPath())
        );
    }

    @Test
    void shouldRespondStatus200WhenFindIncomesByDescriptionWithSuccess(){
        UserDBO user = getUser();
        List<IncomeDBO> incomesExpected = List.of(
                IncomeDBO.of("Salário", 5000.0, LocalDate.of(2025, Month.FEBRUARY, 15), user),
                IncomeDBO.of("Venda Tablet", 2000.0, LocalDate.of(2025, Month.FEBRUARY, 15), user)
        );
        incomeRepository.saveAll(incomesExpected);

        String uri = UriComponentsBuilder.fromPath("/incomes")
                .queryParam("description", "salario")
                .toUriString();

        HttpEntity<String> entity = new HttpEntity<>(getAuthorizationHeader());
        ResponseEntity<List<IncomeResponseDTO>> responseEntity = restTemplate.exchange(uri, HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        List<IncomeResponseDTO> response = responseEntity.getBody();

        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals(1, response.size());
        assertAll(
                () -> assertEquals(incomesExpected.get(0).getId(), response.get(0).id()),
                () -> assertEquals(incomesExpected.get(0).getDescription(), response.get(0).description()),
                () -> assertEquals(incomesExpected.get(0).getAmount(), response.get(0).amount()),
                () -> assertEquals(incomesExpected.get(0).getDate(), response.get(0).date())
        );

        String uriAluguel = UriComponentsBuilder.fromPath("/incomes")
                .queryParam("description", "aluguel")
                .toUriString();

        HttpEntity<String> entity1 = new HttpEntity<>(getAuthorizationHeader());
        ResponseEntity<List<IncomeResponseDTO>> responseEntity1 = restTemplate.exchange(uriAluguel, HttpMethod.GET,
                entity1,
                new ParameterizedTypeReference<>() {
                });

        List<IncomeResponseDTO> response1 = responseEntity1.getBody();

        assertEquals(200, responseEntity.getStatusCode().value());
        assertTrue(response1.isEmpty());
    }

    @Test
    void shouldFindAllIncomesByMonthSuccessfully(){
        UserDBO user = getUser();
        List<IncomeDBO> incomesExpected = List.of(
                IncomeDBO.of("Salário", 5000.0, LocalDate.of(2025, Month.FEBRUARY, 15), user),
                IncomeDBO.of("Venda Tablet", 2000.0, LocalDate.of(2025, Month.JANUARY, 15), user)
        );
        incomeRepository.saveAll(incomesExpected);

        HttpEntity<String> entity = new HttpEntity<>(getAuthorizationHeader());
        ResponseEntity<List<IncomeResponseDTO>> responseEntity = restTemplate.exchange("/incomes/{year}/{month}",
                HttpMethod.GET, entity,
                new ParameterizedTypeReference<>() {
                },
                2025, 2
        );

        List<IncomeResponseDTO> response = responseEntity.getBody();

        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals(1, response.size());
        assertAll(
                () -> assertEquals(incomesExpected.get(0).getId(), response.get(0).id()),
                () -> assertEquals(incomesExpected.get(0).getDescription(), response.get(0).description()),
                () -> assertEquals(incomesExpected.get(0).getAmount(), response.get(0).amount()),
                () -> assertEquals(incomesExpected.get(0).getDate(), response.get(0).date())
        );

        HttpEntity<String> entity1 = new HttpEntity<>(getAuthorizationHeader());
        ResponseEntity<List<IncomeResponseDTO>> responseEntity1 = restTemplate.exchange("/incomes/{year}/{month}",
                HttpMethod.GET, entity1,
                new ParameterizedTypeReference<>() {
                },
                2024, 2
        );

        List<IncomeResponseDTO> response1 = responseEntity1.getBody();

        assertEquals(200, responseEntity1.getStatusCode().value());
        assertTrue(response1.isEmpty());
    }
}