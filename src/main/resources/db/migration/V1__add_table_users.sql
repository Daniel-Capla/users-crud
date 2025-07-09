CREATE TABLE users
(
    id         UUID NOT NULL,
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    user_name  VARCHAR(255),
    email      VARCHAR(255),
    password   VARCHAR(255),
    CONSTRAINT pk_users PRIMARY KEY (id)
);