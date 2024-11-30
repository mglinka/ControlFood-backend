-- V1__create_categories_table.sql

-- Tworzymy tabelę categories
CREATE TABLE category (
                            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),   -- UUID jako klucz główny, generowany automatycznie
                            version BIGINT NOT NULL,                         -- Pole wersji do optymistycznego blokowania
                            name VARCHAR(255) NOT NULL UNIQUE                -- Unikalna nazwa kategorii
);

-- Wstawiamy przykładowe dane do tabeli categories
INSERT INTO category (name, version) VALUES
                                           ('Nabiał', 1),
                                           ('Mięso', 1),
                                           ('Zupy', 1),
                                           ('Warzywa', 1),
                                           ('Owoce', 1),
                                           ('Bezglutenowe', 1),
                                           ('Przekąski', 1),
                                           ('Batoniki białkowe', 1),
                                           ('Herbaty', 1),
                                           ('Śniadanie', 1);

