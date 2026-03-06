package com.dev.smartfinanceapi.services;

import com.dev.smartfinanceapi.dtos.GoalRequest;
import com.dev.smartfinanceapi.models.Goal;
import com.dev.smartfinanceapi.models.User;
import com.dev.smartfinanceapi.repositories.GoalRepository;
import com.dev.smartfinanceapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class GoalService {

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private UserRepository userRepository;

    public Goal createGoal(GoalRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Goal goal = new Goal();
        goal.setName(request.getName());
        goal.setTargetAmount(request.getTargetAmount());
        goal.setDeadline(request.getDeadline());
        goal.setUser(user);
        // currentAmount empieza en 0 por defecto gracias a nuestra entidad

        return goalRepository.save(goal);
    }

    public List<Goal> getGoalsByUserId(Long userId) {
        return goalRepository.findByUserId(userId);
    }

    // ¡La función estrella! Para sumarle ahorros a nuestra meta
    public Goal addFundsToGoal(Long goalId, BigDecimal amountToAdd) {
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Meta no encontrada"));

        // Sumamos el dinero nuevo al que ya teníamos
        BigDecimal newAmount = goal.getCurrentAmount().add(amountToAdd);
        goal.setCurrentAmount(newAmount);

        return goalRepository.save(goal);
    }

    public void deleteGoal(Long id) {
        goalRepository.deleteById(id);
    }
}