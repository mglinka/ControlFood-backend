ALTER TABLE account
    ADD CONSTRAINT unique_email UNIQUE (email);
