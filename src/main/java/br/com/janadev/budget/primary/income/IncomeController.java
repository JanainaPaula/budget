package br.com.janadev.budget.primary.income;

import br.com.janadev.budget.domain.income.commands.RegisterIncomeCommand;
import br.com.janadev.budget.domain.income.ports.primary.FindAllIncomesPort;
import br.com.janadev.budget.domain.income.ports.primary.RegisterIncomePort;
import br.com.janadev.budget.primary.income.dto.IncomeRequestDTO;
import br.com.janadev.budget.primary.income.dto.IncomeResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    public IncomeController(RegisterIncomePort incomeDomainPort, FindAllIncomesPort findAllIncomesPort) {
        this.incomeDomainPort = incomeDomainPort;
        this.findAllIncomesPort = findAllIncomesPort;
    }

    @PostMapping
    public ResponseEntity<IncomeResponseDTO> registerIncome(@RequestBody IncomeRequestDTO request){
        RegisterIncomeCommand command = RegisterIncomeCommand.of(request.description(), request.amount(), request.date());
        IncomeResponseDTO response = IncomeResponseDTO.toDTO(incomeDomainPort.registerIncome(command));
        return ResponseEntity.created(URI.create(String.format("/incomes/%s", response.id()))).body(response);
    }

    @GetMapping
    public ResponseEntity<List<IncomeResponseDTO>> findAll(){
        List<IncomeResponseDTO> incomes = findAllIncomesPort.findAll().stream().map(IncomeResponseDTO::toDTO).toList();
        return ResponseEntity.ok(incomes);
    }
}
