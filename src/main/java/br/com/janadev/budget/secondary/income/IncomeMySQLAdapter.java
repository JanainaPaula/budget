package br.com.janadev.budget.secondary.income;

import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.ports.secondary.IncomeDatabasePort;
import br.com.janadev.budget.secondary.auth.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class IncomeMySQLAdapter implements IncomeDatabasePort {

    private final IncomeRepository repository;
    private final UserRepository userRepository;

    public IncomeMySQLAdapter(IncomeRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public Income save(Income income) {
        var incomeDBO = IncomeDBO.of(income.getDescription(), income.getAmount(), income.getDate());
        return repository.save(incomeDBO).toDomain();
    }

    @Override
    public List<Income> findAll() {
        return repository.findAll().stream().map(IncomeDBO::toDomain).toList();
    }

    @Override
    public Income findById(Long id) {
        return repository.findById(id).map(IncomeDBO::toDomain).orElse(null);
    }

    @Override
    public Income updateById(Income income) {
        var incomeDBO = IncomeDBO.of(income.getId(), income.getDescription(), income.getAmount(), income.getDate());
        return repository.save(incomeDBO).toDomain();
    }

    @Override
    public boolean descriptionAlreadyExists(String description, LocalDate startDate, LocalDate endDate) {
        return repository.existsByDescriptionAndDateBetween(description, startDate, endDate);
    }

    @Override
    public void delete(Income income) {
        var incomeDBO = IncomeDBO.of(income.getId(), income.getDescription(), income.getAmount(), income.getDate());
        repository.delete(incomeDBO);
    }

    @Override
    public List<Income> findByDescription(String description) {
        return repository.findByDescriptionContainingIgnoreCase(description).stream()
                .map(IncomeDBO::toDomain).toList();
    }

    @Override
    public List<Income> findAllByMonth(int year, int month) {
        return repository.findByDateYearAndDateMonth(year, month).stream()
                .map(IncomeDBO::toDomain).toList();
    }

    @Override
    public double sumTotalAmountByMonth(int year, int month) {
        return repository.sumTotalAmountByMonth(year, month);
    }
}
