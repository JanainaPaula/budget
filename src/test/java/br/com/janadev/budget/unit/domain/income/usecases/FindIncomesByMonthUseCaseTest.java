package br.com.janadev.budget.unit.domain.income.usecases;

import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.ports.secondary.IncomeDatabasePort;
import br.com.janadev.budget.domain.income.usecases.FindIncomesByMonthUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindIncomesByMonthUseCaseTest {

    @Mock
    private IncomeDatabasePort incomeDatabasePort;

    @InjectMocks
    private FindIncomesByMonthUseCase findAllIncomesByMonthUseCase;

    @Test
    void shouldFindAllIncomesByMonthSuccessfully(){
        Income incomeExpected = Income.of(2L, "Salário", 5000.0,
                LocalDate.of(2025, Month.FEBRUARY, 15), 3L);

        when(incomeDatabasePort.findAllByMonth(anyInt(), anyInt())).thenReturn(List.of(incomeExpected));

        List<Income> incomes = findAllIncomesByMonthUseCase.findAllByMonth(2025, 2);

        assertEquals(1, incomes.size());
        assertAll(
                () -> assertEquals(incomeExpected.getId(), incomes.get(0).getId()),
                () -> assertEquals(incomeExpected.getDescription(), incomes.get(0).getDescription()),
                () -> assertEquals(incomeExpected.getAmount(), incomes.get(0).getAmount()),
                () -> assertEquals(incomeExpected.getDate(), incomes.get(0).getDate())
        );
    }

}