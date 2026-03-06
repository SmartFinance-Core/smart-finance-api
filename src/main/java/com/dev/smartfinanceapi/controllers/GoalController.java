package com.dev.smartfinanceapi.controllers;

import com.dev.smartfinanceapi.dtos.GoalRequest;
import com.dev.smartfinanceapi.models.Goal;
import com.dev.smartfinanceapi.services.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/goals")
@CrossOrigin(origins = "*")
public class GoalController {

    @Autowired
    private GoalService goalService;

    @PostMapping
    public ResponseEntity<Goal> createGoal(@RequestBody GoalRequest request) {
        Goal newGoal = goalService.createGoal(request);
        return new ResponseEntity<>(newGoal, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Goal>> getUserGoals(@PathVariable Long userId) {
        return new ResponseEntity<>(goalService.getGoalsByUserId(userId), HttpStatus.OK);
    }

    // Usamos PATCH porque solo vamos a actualizar un pedacito de la meta (el dinero ahorrado)
    @PatchMapping("/{id}/add-funds")
    public ResponseEntity<Goal> addFunds(@PathVariable Long id, @RequestBody Map<String, BigDecimal> request) {
        BigDecimal amount = request.get("amount");
        Goal updatedGoal = goalService.addFundsToGoal(id, amount);
        return new ResponseEntity<>(updatedGoal, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long id) {
        goalService.deleteGoal(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}