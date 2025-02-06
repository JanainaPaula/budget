package br.com.janadev.budget.primary.expense;

import br.com.janadev.budget.domain.expense.Expense;
import br.com.janadev.budget.domain.expense.ports.primary.DeleteExpensePort;
import br.com.janadev.budget.domain.expense.ports.primary.FindAllExpensesPort;
import br.com.janadev.budget.domain.expense.ports.primary.GetExpenseDetailsPort;
import br.com.janadev.budget.domain.expense.ports.primary.RegisterExpensePort;
import br.com.janadev.budget.primary.expense.dto.ExpenseRequestDTO;
import br.com.janadev.budget.primary.expense.dto.ExpenseResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final RegisterExpensePort registerExpensePort;
    private final GetExpenseDetailsPort getExpenseDetailsPort;
    private final FindAllExpensesPort findAllExpensesPort;
    private final DeleteExpensePort deleteExpensePort;

    public ExpenseController(RegisterExpensePort registerExpensePort,
                             GetExpenseDetailsPort getExpenseDetailsPort,
                             FindAllExpensesPort findAllExpensesPort,
                             DeleteExpensePort deleteExpensePort) {
        this.registerExpensePort = registerExpensePort;
        this.getExpenseDetailsPort = getExpenseDetailsPort;
        this.findAllExpensesPort = findAllExpensesPort;
        this.deleteExpensePort = deleteExpensePort;
    }

    @PostMapping
    public ResponseEntity<ExpenseResponseDTO> register(@RequestBody ExpenseRequestDTO request){
        Expense response = registerExpensePort.register(
                Expense.of(request.description(), request.amount(), request.date()));
        return ResponseEntity.created(URI.create(String.format("/expense/%s", response.getId())))
                .body(ExpenseResponseDTO.toDTO(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> getDetails(@PathVariable Long id){
        Expense expense = getExpenseDetailsPort.getDetails(id);
        return ResponseEntity.ok(ExpenseResponseDTO.toDTO(expense));
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponseDTO>> findAll(){
        List<ExpenseResponseDTO> expenses =
                findAllExpensesPort.findAll().stream().map(ExpenseResponseDTO::toDTO).toList();
        return ResponseEntity.ok(expenses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        deleteExpensePort.delete(id);
        return ResponseEntity.noContent().build();
    }
}
