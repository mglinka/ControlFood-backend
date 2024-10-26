-- Tworzenie tabeli `jwt_whitelist_token`
CREATE TABLE IF NOT EXISTS jwt_whitelist_token (
                                                   id UUID PRIMARY KEY, -- zakładamy, że jest w AbstractEntity
                                                   version BIGINT NOT NULL,
                                                   token VARCHAR(255) NOT NULL UNIQUE, -- kolumna dla tokenu JWT
                                                   expiration_date DATE, -- kolumna dla daty wygaśnięcia
                                                   account_id UUID NOT NULL, -- klucz obcy dla konta
                                                   CONSTRAINT fk_account -- nazwa ograniczenia dla klucza obcego
                                                       FOREIGN KEY (account_id)
                                                           REFERENCES account(id) -- odniesienie do tabeli account
                                                           ON DELETE CASCADE -- usunięcie tokenu, gdy konto zostanie usunięte
);