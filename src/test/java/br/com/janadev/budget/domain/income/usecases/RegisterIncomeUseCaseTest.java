package br.com.janadev.budget.domain.income.usecases;

import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.commands.RegisterIncomeCommand;
import br.com.janadev.budget.domain.income.ports.secondary.IncomeDatabasePort;
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
        var description = "Internet";
        double amount = 159.90;
        var date = LocalDate.of(2025, Month.JANUARY, 21);
        var incomeCommand = RegisterIncomeCommand.of(description, amount, date);
        var incomeExpected = Income.of(2L, description, amount, date);

        when(incomeDatabasePort.save(any())).thenReturn(incomeExpected);

        Income income = registerIncomeUseCase.registerIncome(incomeCommand);

        assertNotNull(income);
        assertAll(
                () -> assertEquals(incomeExpected.getId(), income.getId()),
                () -> assertEquals(incomeExpected.getDescription(), income.getDescription()),
                () -> assertEquals(incomeExpected.getAmount(), income.getAmount()),
                () -> assertEquals(incomeExpected.getDate(), income.getDate())
        );
    }
}