package com.dev.smartfinanceapi.repositories;
import com.dev.smartfinanceapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Spring adivina la consulta SQL solo con este nombre:
    User findByEmail(String email);
}