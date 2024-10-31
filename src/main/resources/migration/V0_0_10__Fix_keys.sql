-- zmiana klucza
ALTER TABLE allergy_profile
    DROP CONSTRAINT IF EXISTS fk_account;

ALTER TABLE allergy_profile
    ADD CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE SET NULL;
