package br.com.janadev.budget.secondary.income;

import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.ports.secondary.IncomeDatabasePort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class IncomeMySQLAdapter implements IncomeDatabasePort {

    private final IncomeRepository repository;

    public IncomeMySQLAdapter(IncomeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Income save(Income income) {
        IncomeDBO incomeDBO = IncomeDBO.toIncomeDBO(income);
        return repository.save(incomeDBO).toDomain();
    }

    @Override
    public List<Income> findAll() {
        return repository.findAll().stream().map(IncomeDBO::toDomain).toList();
    }

    @Override
    public Income findById(Long id) {
        return Objects.requireNonNull(
                repository.findById(id).orElse(null))
                .toDomain();
    }

}
