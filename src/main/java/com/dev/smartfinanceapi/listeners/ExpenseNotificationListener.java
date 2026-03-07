package com.dev.smartfinanceapi.listeners;

import com.dev.smartfinanceapi.config.RabbitMQConfig;
import com.dev.smartfinanceapi.models.Expense;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ExpenseNotificationListener {

    // Este método está "escuchando" la cola 24/7 sin bloquear a la aplicación
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleExpenseCreated(Expense expense) {
        System.out.println("=====================================================");
        System.out.println("✅ [TRABAJO ASÍNCRONO EN SEGUNDO PLANO INICIADO]");
        System.out.println("💌 Simulando envío de correo al Usuario ID: " + expense.getUser().getId());
        System.out.println("📊 Enviando gasto de S/" + expense.getAmount() + " a la IA de Python para predicción...");
        System.out.println("=====================================================");
    }
}