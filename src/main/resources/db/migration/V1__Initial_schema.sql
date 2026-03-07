-- V1__Initial_schema.sql

CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       created_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
                       updated_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)
);

CREATE TABLE categories (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(100) NOT NULL,
                            description VARCHAR(255)
);

CREATE TABLE expenses (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          amount DECIMAL(38,2) NOT NULL,
                          description VARCHAR(255) NOT NULL,
                          date DATETIME(6) NOT NULL,
                          category_id BIGINT NOT NULL,
                          user_id BIGINT NOT NULL,
                          created_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
                          updated_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
                          CONSTRAINT fk_expense_category FOREIGN KEY (category_id) REFERENCES categories (id),
                          CONSTRAINT fk_expense_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE goals (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       target_amount DECIMAL(38,2) NOT NULL,
                       current_amount DECIMAL(38,2) DEFAULT 0.00,
                       deadline DATE NOT NULL,
                       user_id BIGINT NOT NULL,
                       CONSTRAINT fk_goal_user FOREIGN KEY (user_id) REFERENCES users (id)
);