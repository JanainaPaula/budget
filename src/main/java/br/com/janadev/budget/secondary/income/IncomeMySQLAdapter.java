package br.com.janadev.budget.secondary.income;

import br.com.janadev.budget.domain.income.Income;
import br.com.janadev.budget.domain.income.ports.secondary.IncomeDatabasePort;
import br.com.janadev.budget.secondary.user.adapter.port.UserPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class IncomeMySQLAdapter implements IncomeDatabasePort {

    private final IncomeRepository repository;
    private final UserPort userPort;

    public IncomeMySQLAdapter(IncomeRepository repository, UserPort userServicePort) {
        this.repository = repository;
        this.userPort = userServicePort;
    }


    @Override
    @Transactional
    public Income save(Income income) {
        var user = userPort.findById(income.getUserId());
        var incomeDBO = IncomeDBO.of(income.getDescription(), income.getAmount(), income.getDate(), user);
        return repository.save(incomeDBO).toDomain();
    }

    @Override
    public List<Income> findAllByUserId(Long userId) {
        return repository.findByUserId(userId).stream().map(IncomeDBO::toDomain).toList();
    }

    @Override
    public Income findById(Long id) {
        return repository.findById(id).map(IncomeDBO::toDomain).orElse(null);
    }

    @Override
    public Income updateById(Income income) {
        var user = userPort.findById(income.getUserId());
        var incomeDBO = IncomeDBO.of(income.getId(), income.getDescription(), income.getAmount(), income.getDate(), user);
        return repository.save(incomeDBO).toDomain();
    }

    @Override
    public boolean descriptionAlreadyExists(Long userId, String description, LocalDate startDate, LocalDate endDate) {
        return repository.existsByUserIdAndDescriptionAndDateBetween(userId, description, startDate, endDate);
    }

    @Override
    public void delete(Income income) {
        repository.deleteById(income.getId());
    }

    @Override
    public List<Income> findByDescription(Long userId, String description) {
        return repository.findByUserIdAndDescriptionContainingIgnoreCase(userId, description).stream()
                .map(IncomeDBO::toDomain).toList();
    }

    @Override
    public List<Income> findAllByMonth(Long userId, int year, int month) {
        return repository.findByUserIdAndDateYearAndDateMonth(userId, year, month).stream()
                .map(IncomeDBO::toDomain).toList();
    }

    @Override
    public double sumTotalAmountByMonth(int year, int month) {
        return repository.sumTotalAmountByMonth(year, month);
    }
}
