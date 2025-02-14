package br.com.janadev.budget.unit.domain.expense.usecases;

import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.ports.secondary.IncomeDatabasePort;
import br.com.janadev.budget.domain.income.usecases.FindIncomesByDescriptionUseCase;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindIncomesByDescriptionUseCaseTest {

    @Mock
    private IncomeDatabasePort incomeDatabasePort;

    @InjectMocks
    private FindIncomesByDescriptionUseCase findIncomesByDescriptionUseCase;

    @Test
    void shouldReturnIncomesWithDescription(){
        List<Income> incomesExpected = List.of(
                Income.of(2L, "Luz", 150.0, LocalDate.of(2025, Month.FEBRUARY, 14)),
                Income.of(3L, "Luz", 150.0, LocalDate.of(2025, Month.JANUARY, 30))
        );

        when(incomeDatabasePort.findByDescription(any())).thenReturn(incomesExpected);

        List<Income> incomes = findIncomesByDescriptionUseCase.findByDescription("Luz");

        assertNotNull(incomes);
        assertFalse(incomes.isEmpty());
        assertAll(
                () -> assertEquals(incomesExpected.get(0).getId(), incomes.get(0).getId()),
                () -> assertEquals(incomesExpected.get(0).getDescription(), incomes.get(0).getDescription()),
                () -> assertEquals(incomesExpected.get(0).getAmount(), incomes.get(0).getAmount()),
                () -> assertEquals(incomesExpected.get(0).getDate(), incomes.get(0).getDate()),
                () -> assertEquals(incomesExpected.get(1).getId(), incomes.get(1).getId()),
                () -> assertEquals(incomesExpected.get(1).getDescription(), incomes.get(1).getDescription()),
                () -> assertEquals(incomesExpected.get(1).getAmount(), incomes.get(1).getAmount()),
                () -> assertEquals(incomesExpected.get(1).getDate(), incomes.get(1).getDate())
        );
    }

}