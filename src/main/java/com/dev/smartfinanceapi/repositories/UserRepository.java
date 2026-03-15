package com.dev.smartfinanceapi.repositories;

import com.dev.smartfinanceapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Para que tu código antiguo (Auth, Expense) no se rompa
    User findByEmail(String email);

    // Para que tu nuevo código del Webhook funcione seguro
    User findFirstByEmail(String email);
    User findFirstByPhoneNumber(String phoneNumber);
}