package com.dev.smartfinanceapi.repositories;

import com.dev.smartfinanceapi.dtos.CategoryTotal;
import com.dev.smartfinanceapi.models.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUserId(Long userId);

    // 1. Sumar TODOS los gastos de un usuario
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = :userId")
    BigDecimal getTotalExpensesByUserId(@Param("userId") Long userId);

    // 2. Agrupar y sumar los gastos por categoría (Ideal para gráficos circulares)
    @Query("SELECT c.name AS categoryName, SUM(e.amount) AS totalAmount " +
            "FROM Expense e JOIN e.category c " +
            "WHERE e.user.id = :userId GROUP BY c.name")
    List<CategoryTotal> getTotalExpensesByCategory(@Param("userId") Long userId);

    @Query("SELECT e FROM Expense e WHERE e.user.id = :userId " +
            "AND (:categoryId IS NULL OR e.category.id = :categoryId) " +
            "AND (:startDate IS NULL OR e.date >= :startDate) " +
            "AND (:endDate IS NULL OR e.date <= :endDate)")
    org.springframework.data.domain.Page<Expense> findExpensesWithFilters(
            @Param("userId") Long userId,
            @Param("categoryId") Long categoryId,
            @Param("startDate") java.time.LocalDateTime startDate,
            @Param("endDate") java.time.LocalDateTime endDate,
            org.springframework.data.domain.Pageable pageable
    );

}