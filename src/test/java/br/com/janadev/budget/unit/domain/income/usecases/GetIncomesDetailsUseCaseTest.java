package br.com.janadev.budget.unit.domain.income.usecases;

import br.com.janadev.budget.domain.exceptions.DomainNotFoundException;
import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.ports.secondary.IncomeDatabasePort;
import br.com.janadev.budget.domain.income.usecases.GetIncomesDetailsUseCase;
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
class GetIncomesDetailsUseCaseTest {

    @Mock
    private IncomeDatabasePort incomeDatabasePort;

    @InjectMocks
    private GetIncomesDetailsUseCase getIncomesDetailsUseCase;

    @Test
    void shouldGetDetailsOfIncomeSuccessfully(){
        Income incomeExpected = Income.of(2L, "Salário", 2000.0,
                LocalDate.of(2025, Month.JANUARY, 23));

        when(incomeDatabasePort.findById(any())).thenReturn(incomeExpected);

        Income income = getIncomesDetailsUseCase.getIncomeDetails(2L);

        assertNotNull(income);
        assertAll(
                () -> assertEquals(incomeExpected.getId(), income.getId()),
                () -> assertEquals(incomeExpected.getDescription(), income.getDescription()),
                () -> assertEquals(incomeExpected.getAmount(), income.getAmount()),
                () -> assertEquals(incomeExpected.getDate(), income.getDate())
        );
    }

    @Test
    void shouldThrowsDomainNotFoundExceptionWhenIncomeNotFound(){
        when(incomeDatabasePort.findById(any())).thenReturn(null);

        assertThrows(DomainNotFoundException.class,
                () -> getIncomesDetailsUseCase.getIncomeDetails(1L));
    }

}