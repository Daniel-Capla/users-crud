ALTER TABLE audit_file
    DROP COLUMN json;

ALTER TABLE audit_file
    ADD json JSONB;