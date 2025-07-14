CREATE TABLE ip_audit_log
(
    id           UUID NOT NULL,
    user_id      UUID,
    ip_address   VARCHAR(255),
    country      VARCHAR(255),
    country_code VARCHAR(255),
    raw_data     JSONB,
    CONSTRAINT pk_ipauditlog PRIMARY KEY (id)
);

ALTER TABLE ip_audit_log
    ADD CONSTRAINT FK_IPAUDITLOG_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);