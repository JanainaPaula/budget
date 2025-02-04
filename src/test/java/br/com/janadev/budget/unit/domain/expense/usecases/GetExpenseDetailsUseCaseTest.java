package br.com.janadev.budget.unit.domain.expense.usecases;

import br.com.janadev.budget.domain.exceptions.DomainNotFoundException;
import br.com.janadev.budget.domain.expense.Expense;
import br.com.janadev.budget.domain.expense.ports.secondary.ExpenseDatabasePort;
import br.com.janadev.budget.domain.expense.usecases.GetExpenseDetailsUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetExpenseDetailsUseCaseTest {

    @Mock
    private ExpenseDatabasePort expenseDatabasePort;

    @InjectMocks
    private GetExpenseDetailsUseCase getExpenseDetailsUseCase;

    @Test
    void shouldGetExpenseDetailsSuccessfully(){
        var expenseExpected = Expense.of(2L, "Luz", 150.0,
                LocalDate.of(2025, Month.JANUARY, 30));

        when(expenseDatabasePort.findById(any())).thenReturn(expenseExpected);

        Expense expense = getExpenseDetailsUseCase.getDetails(2L);

        assertAll(
                () -> assertEquals(expenseExpected.getId(), expense.getId()),
                () -> assertEquals(expenseExpected.getDescription(), expense.getDescription()),
                () -> assertEquals(expenseExpected.getAmount(), expense.getAmount()),
                () -> assertEquals(expenseExpected.getDate(), expense.getDate())
        );
    }

    @Test
    void shouldThrowDomainNotFoundExceptionWhenExpenseDoesNotExists(){
        when(expenseDatabasePort.findById(any())).thenReturn(null);
        assertThrows(DomainNotFoundException.class,
                () -> getExpenseDetailsUseCase.getDetails(2L));
    }

}