package com.dev.smartfinanceapi.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

    @Bean
    public Flyway flyway(DataSource dataSource) {
        // Configuramos Flyway a mano y lo conectamos a nuestro DataSource de MySQL
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .load();

        // Le obligamos a ejecutar la migración inmediatamente
        flyway.migrate();

        return flyway;
    }
}