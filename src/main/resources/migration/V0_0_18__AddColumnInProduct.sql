-- V2__add_category_id_to_product.sql

-- Dodanie kolumny category_id do tabeli product
ALTER TABLE product
    ADD COLUMN category_id UUID, -- Dodanie kolumny category_id jako UUID

-- Dodanie klucza obcego do tabeli category
    ADD CONSTRAINT fk_product
        FOREIGN KEY (category_id)
            REFERENCES category (id)
            ON DELETE SET NULL;
