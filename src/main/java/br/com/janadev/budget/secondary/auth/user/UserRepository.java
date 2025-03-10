package br.com.janadev.budget.secondary.auth.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDBO, Long> {
    Optional<UserDBO> findByEmail(String email);
    boolean existsByEmail(String email);
}
