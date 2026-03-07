package com.dev.smartfinanceapi.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Nombres de nuestra infraestructura de mensajes
    public static final String QUEUE_NAME = "expense_queue";
    public static final String EXCHANGE_NAME = "expense_exchange";
    public static final String ROUTING_KEY = "expense_routing_key";

    // 1. Creamos la Cola (El buzón)
    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true);
    }

    // 2. Creamos el Exchange (El cartero)
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    // 3. Conectamos el buzón con el cartero
    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    // 4. Magia para que Java traduzca los objetos a formato JSON automáticamente
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // 5. La plantilla que usaremos para enviar los mensajes
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}