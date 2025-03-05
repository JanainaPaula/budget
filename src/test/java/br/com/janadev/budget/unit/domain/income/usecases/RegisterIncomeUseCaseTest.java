package br.com.janadev.budget.unit.domain.income.usecases;

import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.exception.IncomeAlreadyExistsException;
import br.com.janadev.budget.domain.income.ports.secondary.IncomeDatabasePort;
import br.com.janadev.budget.domain.income.usecases.RegisterIncomeUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterIncomeUseCaseTest {

    @Mock
    private IncomeDatabasePort incomeDatabasePort;

    @InjectMocks
    private RegisterIncomeUseCase registerIncomeUseCase;

    @Test
    void shouldRegisterIncomeSuccessfully(){
        var description = "Venda Enjoei";
        double amount = 159.90;
        var date = LocalDate.of(2025, Month.JANUARY, 21);
        var incomeCommand = Income.of(description, amount, date, 2L);
        var incomeExpected = Income.of(2L, description, amount, date, 2L);

        when(incomeDatabasePort.save(any())).thenReturn(incomeExpected);

        Income income = registerIncomeUseCase.register(incomeCommand);

        assertNotNull(income);
        assertAll(
                () -> assertEquals(incomeExpected.getId(), income.getId()),
                () -> assertEquals(incomeExpected.getDescription(), income.getDescription()),
                () -> assertEquals(incomeExpected.getAmount(), income.getAmount()),
                () -> assertEquals(incomeExpected.getDate(), income.getDate())
        );
    }

    @Test
    void shouldThrowIncomeAlreadyExistsExceptionWhenTryRegisterIncomeWithDescriptionThatAlreadyExists(){
        var description = "Venda Enjoei";
        double amount = 159.90;
        var date = LocalDate.of(2025, Month.JANUARY, 21);
        var incomeCommand = Income.of(description, amount, date, 2L);

        when(incomeDatabasePort.descriptionAlreadyExists(any(), any(), any())).thenReturn(true);

        assertThrows(IncomeAlreadyExistsException.class,
                () -> registerIncomeUseCase.register(incomeCommand)
        );
    }
}