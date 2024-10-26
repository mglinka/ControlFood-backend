-- Sprawdzenie, czy tabela już istnieje, jeśli nie, utwórz ją
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables
                   WHERE table_name = 'account_confirmation') THEN
        CREATE TABLE account_confirmation (
                                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                              version BIGINT NOT NULL,
                                              token VARCHAR(255) NOT NULL UNIQUE,
                                              account_id UUID NOT NULL,
                                              expiration_date TIMESTAMP NOT NULL,
                                              CONSTRAINT fk_account_confirmation_account FOREIGN KEY (account_id) REFERENCES account(id)
        );
    END IF;
END $$;
