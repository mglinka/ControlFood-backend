DO $$
    BEGIN
        IF EXISTS (SELECT 1 FROM information_schema.columns
                   WHERE table_name = 'account' AND column_name = 'username') THEN
            ALTER TABLE account DROP COLUMN username;
        END IF;
    END $$;
