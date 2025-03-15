package br.com.janadev.budget.secondary.user;

import br.com.janadev.budget.secondary.user.dbo.UserDBO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDBO, Long> {
    Optional<UserDBO> findByEmail(String email);
    boolean existsByEmail(String email);
}
