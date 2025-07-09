-- changeset author:liangquan
CREATE TABLE users (
    uuid VARCHAR(50),
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    mobile_no VARCHAR(20),
    status VARCHAR(20) NOT NULL,
    created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    version BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (uuid)
);

CREATE TABLE user_credential (
    uuid VARCHAR(50),
    user_uuid VARCHAR NOT NULL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (uuid),
    FOREIGN KEY (user_uuid) REFERENCES users(uuid)
);

CREATE TABLE token (
    uuid VARCHAR(50),
    user_uuid VARCHAR NOT NULL,
    token VARCHAR NOT NULL,
    refresh_token VARCHAR NOT NULL,
    expired_on TIMESTAMP NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    version BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (uuid),
    FOREIGN KEY (user_uuid) REFERENCES users(uuid)
);

CREATE TABLE payment_history (
    uuid VARCHAR(50),
    acc_no VARCHAR(50) NOT NULL,
    transaction_amount DECIMAL(19,2) NOT NULL,
    description VARCHAR(255),
    transaction_date DATE NOT NULL,
    transaction_time TIME NOT NULL,
    customer_uuid VARCHAR NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (uuid)
);

-- Add these after table creation
CREATE INDEX idx_token_user ON token(user_uuid);
CREATE INDEX idx_payment_customer_uuid ON payment_history(customer_uuid);
CREATE INDEX idx_payment_acc_no ON payment_history(acc_no);
CREATE INDEX idx_payment_trxDate ON payment_history(transaction_date);
CREATE INDEX idx_user_uuid ON users(uuid);
CREATE INDEX idx_user_credential_user ON user_credential(user_uuid);
