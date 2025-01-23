package br.com.janadev.budget.unit.domain.income.usecases;

import br.com.janadev.budget.domain.exceptions.DomainNotFoundException;
import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.commands.IncomeCommand;
import br.com.janadev.budget.domain.income.ports.secondary.IncomeDatabasePort;
import br.com.janadev.budget.domain.income.usecases.UpdateIncomeUseCase;
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
class UpdateIncomeUseCaseTest {

    @Mock
    private IncomeDatabasePort incomeDatabasePort;

    @InjectMocks
    private UpdateIncomeUseCase updateIncomeUseCase;

    @Test
    void shouldUpdateIncomeSuccessfully(){
        Long id = 2L;
        var description = "Salário";
        double amount = 3000.0;
        var date = LocalDate.of(2025, Month.JANUARY, 23);
        var income = Income.of(id, description, amount, date);
        var incomeCommand = IncomeCommand.of(description, 5000.0, LocalDate.of(2025, Month.JANUARY, 15));
        var incomeUpdatedExpected = Income.of(id, description, incomeCommand.getAmount(), incomeCommand.getDate());

        when(incomeDatabasePort.findById(any())).thenReturn(income);
        when(incomeDatabasePort.save(any())).thenReturn(incomeUpdatedExpected);

        Income incomeUpdated = updateIncomeUseCase.update(id, incomeCommand);

        assertNotNull(incomeUpdated);
        assertAll(
                () -> assertEquals(incomeUpdatedExpected.getId(), incomeUpdated.getId()),
                () -> assertEquals(incomeUpdatedExpected.getDescription(), incomeUpdated.getDescription()),
                () -> assertEquals(incomeUpdatedExpected.getAmount(), incomeUpdated.getAmount()),
                () -> assertEquals(incomeUpdatedExpected.getDate(),incomeUpdated.getDate())
        );
    }

    @Test
    void shouldThrowsDomainNotFoundExceptionWhenIncomeNotFound(){
        var incomeCommand = IncomeCommand.of("Salário", 3000.0, LocalDate.of(2025, Month.JANUARY, 23));
        when(incomeDatabasePort.findById(any())).thenReturn(null);

        assertThrows(DomainNotFoundException.class,
                () -> updateIncomeUseCase.update(2L, incomeCommand));
    }

}