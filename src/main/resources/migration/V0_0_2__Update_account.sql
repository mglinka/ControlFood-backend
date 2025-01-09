-- Check if the column "role_id" exists, and only add it if it doesn't

-- Migration script to create all necessary tables with foreign keys added at the end
-- Filename: V1__create_all_entities_with_roles_accounts_and_allergens.sql





-- 1. Create the Role table
CREATE TABLE role (
                      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),   -- UUID as primary key, auto-generated
                      version BIGINT NOT NULL,                         -- Optimistic locking version field
                      name VARCHAR(32) NOT NULL UNIQUE                 -- Enum stored as a string
);

-- Insert roles into the Role table
INSERT INTO role (id, version, name) VALUES
                                         ('550e8400-e29b-41d4-a716-446655440000', 0, 'ROLE_ADMIN'),
                                         ('cd8ab1c1-2431-4e28-88b5-fdd54de3d92a', 0, 'ROLE_USER'),
                                         ('4c90f86a-0d82-4c51-b72c-80e20949a3b9', 0, 'ROLE_SPECIALIST');

-- 2. Create the AllergyProfile table
CREATE TABLE allergy_profile (
                                 profile_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),   -- UUID as primary key, auto-generated
                                 version BIGINT NOT NULL                                 -- Optimistic locking version field
);

-- 3. Create the Account table
CREATE TABLE account (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),          -- UUID as primary key, auto-generated
                         version BIGINT NOT NULL,                                -- Optimistic locking version field
                         email VARCHAR(255) NOT NULL,                            -- Email column (indexed)
                         first_name VARCHAR(255),                                -- First name of the user
                         last_name VARCHAR(255),                                 -- Last name of the user
                         password VARCHAR(255) NOT NULL,                         -- Password field
                         username VARCHAR(255) NOT NULL,                         -- Unique username
                         allergy_profile_id UUID,                                -- Foreign key to AllergyProfile (will be added later)
                         CONSTRAINT unique_username UNIQUE (username)            -- Ensure unique usernames
);

-- Optional indexes for faster queries
CREATE INDEX idx_account_email ON account(email);
CREATE INDEX idx_account_allergy_profile_id ON account(allergy_profile_id);

-- 4. Create the Allergen table
CREATE TABLE allergen (
                          allergen_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),  -- UUID as primary key, auto-generated
                          version BIGINT NOT NULL,                                 -- Optimistic locking version field
                          name VARCHAR(255) NOT NULL UNIQUE                        -- Unique name of the allergen
);

-- Insert allergens into the Allergen table
INSERT INTO allergen(allergen_id, version, name)
VALUES
    ('2362a32b-30fb-4a53-9c95-ba4ec24a2cf7', 0, 'soja'),
    ('c07c9cfd-d84b-4be7-bc08-73cb7e28a9e2', 0, 'orzechy'),
    ('c17c9cfd-d84b-4be7-bc08-73cb7e28a9e2', 0, 'mleko'),
    ('c27c9cfd-d84b-4be7-bc08-73cb7e28a9e2', 0, 'gluten'),
    ('c37c9cfd-d84b-4be7-bc08-73cb7e28a9e2', 0, 'seler'),
    ('c47c9cfd-d84b-4be7-bc08-73cb7e28a9e2', 0, 'gorczyca'),
    ('c67c9cfd-d84b-4be7-bc08-73cb7e28a9e2', 0, 'mąka pszenna'),
    ('c77c9cfd-d84b-4be7-bc08-73cb7e28a9e2', 0, 'kasza jęczmienna'),
    ('c87c9cfd-d84b-4be7-bc08-73cb7e28a9e2', 0, 'pszenica'),
    ('c97c9cfd-d84b-4be7-bc08-73cb7e28a9e2', 0, 'jaja'),
    ('c99c9cfd-d84b-4be7-bc08-73cb7e28a9e2', 0, 'sezam'),
    ('c44c9cfd-d84b-4be7-bc08-73cb7e28a9e2', 0, 'migdały');

-- 5. Create the ProfileAllergen table with a composite primary key
CREATE TABLE profile_allergen (
                                  profile_id UUID NOT NULL,                                  -- Foreign key to AllergyProfile
                                  allergen_id UUID NOT NULL,                                 -- Foreign key to Allergen
                                  intensity VARCHAR(255),                                    -- Optional intensity column
                                  PRIMARY KEY (profile_id, allergen_id)                      -- Composite primary key
);

-- 6. Create the AccountRole table to map users to roles
CREATE TABLE account_role (
                              account_id UUID NOT NULL,                                  -- Foreign key to Account
                              role_id UUID NOT NULL,                                     -- Foreign key to Role
                              PRIMARY KEY (account_id, role_id)                          -- Composite primary key
);

-- Add foreign keys at the end

-- 1. Foreign key linking account to allergy_profile
ALTER TABLE account
    ADD CONSTRAINT fk_allergy_profile FOREIGN KEY (allergy_profile_id) REFERENCES allergy_profile(profile_id);

-- 2. Foreign key linking allergy_profile to account
ALTER TABLE allergy_profile
    ADD CONSTRAINT fk_account FOREIGN KEY (profile_id) REFERENCES account(id) ON DELETE SET NULL;

-- 3. Foreign key linking profile_allergen to allergy_profile
ALTER TABLE profile_allergen
    ADD CONSTRAINT fk_profile_allergy FOREIGN KEY (profile_id) REFERENCES allergy_profile(profile_id);

-- 4. Foreign key linking profile_allergen to allergen
ALTER TABLE profile_allergen
    ADD CONSTRAINT fk_allergen FOREIGN KEY (allergen_id) REFERENCES allergen(allergen_id);

-- 5. Foreign key linking account_role to account
ALTER TABLE account_role
    ADD CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES account(id);

-- 6. Foreign key linking account_role to role
ALTER TABLE account_role
    ADD CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES role(id);






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
