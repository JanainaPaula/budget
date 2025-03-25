package br.com.janadev.budget.unit.domain.income.usecases;

import br.com.janadev.budget.domain.exceptions.DomainNotFoundException;
import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.ports.secondary.IncomeDatabasePort;
import br.com.janadev.budget.domain.income.usecases.DeleteIncomeUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteIncomeUseCaseTest {

    @Mock
    private IncomeDatabasePort incomeDatabasePort;

    @InjectMocks
    private DeleteIncomeUseCase deleteIncomeUseCase;

    @Test
    void shouldDeleteIncomeSuccessfully(){
        var income = Income.of(2L, "Salário", 2500.0,
                LocalDate.of(2025, Month.JANUARY, 24), 3L);

        when(incomeDatabasePort.findById(any())).thenReturn(income);
        doNothing().when(incomeDatabasePort).delete(any());

        assertDoesNotThrow(() -> deleteIncomeUseCase.delete(2L));
    }

    @Test
    void shouldThrowsDomainNotFoundExceptionWhenTryDeleteIncomeThatNotExists(){
        when(incomeDatabasePort.findById(any())).thenReturn(null);

        assertThrows(DomainNotFoundException.class, () -> deleteIncomeUseCase.delete(2L));
    }

}