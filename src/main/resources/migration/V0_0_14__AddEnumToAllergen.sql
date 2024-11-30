-- V1__Add_allergen_type_column.sql

-- 1. Tworzymy typ ENUM w PostgreSQL, jeśli jeszcze nie istnieje
DO $$
    BEGIN
        -- Sprawdzamy, czy typ 'type_enum' już istnieje w bazie danych, jeśli nie, tworzymy go
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'type_enum') THEN
            CREATE TYPE type_enum AS ENUM ('ALLERGEN', 'INTOLERANT_INGREDIENT');
        END IF;
    END $$;

-- 2. Dodanie nowej kolumny 'type' do tabeli 'allergen' o typie ENUM
ALTER TABLE allergen
    ADD COLUMN type type_enum NOT NULL DEFAULT 'INTOLERANT_INGREDIENT';  -- Ustawiamy domyślną wartość na 'INTOLERANT_INGREDIENT'

-- 3. Opcjonalnie: Zaktualizowanie istniejących rekordów (jeśli jakieś mają NULL w polu 'type')
UPDATE allergen
SET type = 'INTOLERANT_INGREDIENT'
WHERE type IS NULL;

-- 4. Opcjonalnie: Można dodać indeks na kolumnie 'type' dla szybszego filtrowania
-- CREATE INDEX idx_allergen_type ON allergen(type);
