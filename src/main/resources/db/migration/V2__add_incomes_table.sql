CREATE TABLE incomes (
                         id         BIGINT AUTO_INCREMENT PRIMARY KEY,
                         amount     DECIMAL(38,2)    NOT NULL,
                         source     VARCHAR(255)     NOT NULL,
                         date       DATETIME(6)      NOT NULL,
                         user_id    BIGINT           NOT NULL,
                         created_at DATETIME(6)      DEFAULT CURRENT_TIMESTAMP(6),
                         updated_at DATETIME(6)      DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
                         CONSTRAINT fk_income_user FOREIGN KEY (user_id) REFERENCES users (id)
);
