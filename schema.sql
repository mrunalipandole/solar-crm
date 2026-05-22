-- Solar CRM Database Schema

CREATE TABLE roles (
    id    BIGSERIAL PRIMARY KEY,
    name  VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE users (
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL UNIQUE,
    provider     VARCHAR(50)  NOT NULL,
    provider_id  VARCHAR(255),
    created_at   TIMESTAMP NOT NULL,
    updated_at   TIMESTAMP
);

CREATE TABLE user_roles (
    user_id  BIGINT REFERENCES users(id),
    role_id  BIGINT REFERENCES roles(id),
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE leads (
    id                 BIGSERIAL PRIMARY KEY,
    customer_name      VARCHAR(255) NOT NULL,
    phone_number       VARCHAR(20)  NOT NULL UNIQUE,
    address            TEXT         NOT NULL,
    electricity_bill   DOUBLE PRECISION NOT NULL,
    lead_source        VARCHAR(100) NOT NULL,
    status             VARCHAR(50)  NOT NULL DEFAULT 'NEW',
    assigned_agent_id  BIGINT REFERENCES users(id),
    created_by_id      BIGINT REFERENCES users(id),
    created_at         TIMESTAMP NOT NULL,
    updated_at         TIMESTAMP
);