package br.com.janadev.budget.primary.income;

import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.commands.IncomeCommand;
import br.com.janadev.budget.domain.income.ports.primary.FindAllIncomesPort;
import br.com.janadev.budget.domain.income.ports.primary.GetIncomeDetailsPort;
import br.com.janadev.budget.domain.income.ports.primary.RegisterIncomePort;
import br.com.janadev.budget.primary.income.dto.IncomeRequestDTO;
import br.com.janadev.budget.primary.income.dto.IncomeResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/incomes")
public class IncomeController {

    private final RegisterIncomePort incomeDomainPort;
    private final FindAllIncomesPort findAllIncomesPort;
    private final GetIncomeDetailsPort getIncomeDetailsPort;

    public IncomeController(RegisterIncomePort incomeDomainPort,
                            FindAllIncomesPort findAllIncomesPort,
                            GetIncomeDetailsPort getIncomeDetailsPort) {
        this.incomeDomainPort = incomeDomainPort;
        this.findAllIncomesPort = findAllIncomesPort;
        this.getIncomeDetailsPort = getIncomeDetailsPort;
    }

    @PostMapping
    public ResponseEntity<IncomeResponseDTO> register(@RequestBody IncomeRequestDTO request){
        var command = IncomeCommand.of(request.description(), request.amount(), request.date());
        var response = IncomeResponseDTO.toDTO(incomeDomainPort.registerIncome(command));
        return ResponseEntity.created(URI.create(String.format("/incomes/%s", response.id()))).body(response);
    }

    @GetMapping
    public ResponseEntity<List<IncomeResponseDTO>> findAll(){
        var incomes = findAllIncomesPort.findAll().stream().map(IncomeResponseDTO::toDTO).toList();
        return ResponseEntity.ok(incomes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncomeResponseDTO> getDetails(@PathVariable Long id){
        Income incomeDetails = getIncomeDetailsPort.getIncomeDetails(id);
        return ResponseEntity.ok(IncomeResponseDTO.toDTO(incomeDetails));
    }
}
