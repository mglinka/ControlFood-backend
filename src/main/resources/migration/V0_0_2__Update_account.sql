-- Check if the column "role_id" exists, and only add it if it doesn't
DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM information_schema.columns
                       WHERE table_name='account' AND column_name='role_id') THEN
            ALTER TABLE account ADD COLUMN role_id UUID UNIQUE;
        END IF;
    END $$;

-- Add column is_enabled if not exists
DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM information_schema.columns
                       WHERE table_name='account' AND column_name='is_enabled') THEN
            ALTER TABLE account ADD COLUMN is_enabled BOOLEAN DEFAULT FALSE;
        END IF;
    END $$;
