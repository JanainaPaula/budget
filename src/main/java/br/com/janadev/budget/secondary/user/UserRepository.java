package br.com.janadev.budget.secondary.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserDBO, Long> {
    UserDBO findByEmail(String email);
}
