package br.com.janadev.budget.unit.domain.expense.usecases;

import br.com.janadev.budget.domain.exceptions.DomainNotFoundException;
import br.com.janadev.budget.domain.expense.Expense;
import br.com.janadev.budget.domain.expense.ports.secondary.ExpenseDatabasePort;
import br.com.janadev.budget.domain.expense.usecases.DeleteExpenseUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteExpenseUseCaseTest {

    @Mock
    private ExpenseDatabasePort expenseDatabasePort;

    @InjectMocks
    private DeleteExpenseUseCase deleteExpenseUseCase;

    @Test
    void shouldDeleteExpenseSuccessfully(){
        when(expenseDatabasePort.findById(any())).thenReturn(
                Expense.of(2L, "Luz", 150.0,
                        LocalDate.of(2025, Month.JANUARY, 29))
        );
        doNothing().when(expenseDatabasePort).delete(any());
        assertDoesNotThrow(()-> deleteExpenseUseCase.delete(2L));
    }

    @Test
    void shouldThrowsDomainNotFoundExceptionWhenExpenseDoesNotExist(){
        when(expenseDatabasePort.findById(any())).thenReturn(null);
        assertThrows(DomainNotFoundException.class,
                () -> deleteExpenseUseCase.delete(2L));
    }
}