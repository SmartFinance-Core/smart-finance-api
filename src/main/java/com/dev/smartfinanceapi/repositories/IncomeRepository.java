package com.dev.smartfinanceapi.repositories;

import com.dev.smartfinanceapi.models.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    // Método para buscar todos los ingresos de un usuario específico
    List<Income> findByUserIdOrderByDateDesc(Long userId);
}