package com.dev.smartfinanceapi.repositories;
import com.dev.smartfinanceapi.models.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // Buscar todos los gastos de un usuario específico
    List<Expense> findByUserId(Long userId);
}