-- V2__add_account_id_foreign_key_to_allergy_profile.sql

DO $$
    BEGIN
        -- Sprawdź, czy kolumna account_id istnieje
        IF NOT EXISTS (
            SELECT 1
            FROM information_schema.columns
            WHERE table_name='allergy_profile' AND column_name='account_id'
        ) THEN
            -- Jeśli kolumna nie istnieje, dodaj ją
            ALTER TABLE allergy_profile ADD COLUMN account_id UUID;
        END IF;
    END $$;


