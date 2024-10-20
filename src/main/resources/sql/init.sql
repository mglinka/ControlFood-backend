INSERT INTO role (id, version, name) VALUES ('550e8400-e29b-41d4-a716-446655440000', 0, 'ROLE_ADMIN');
INSERT INTO role (id, version, name) VALUES ('4c90f86a-0d82-4c51-b72c-80e20949a3b9', 0, 'ROLE_SPECIALIST');
INSERT INTO role (id, version, name) VALUES ('cd8ab1c1-2431-4e28-88b5-fdd54de3d92a', 0, 'ROLE_USER');


--ADMIN--
INSERT INTO account (id,email, first_name, last_name, password, username, is_enabled, version)
VALUES ('fdaf785a-d57c-4782-b70e-ce3afb428e68','jane.smith@example.com', 'Jane', 'Smith', '$2a$10$DEft/NS6EIRj9SQeLlm4NO2Ol30L.K5qsyVXGx2skcnTyyPyvCAG.', 'jane', true, 0);
INSERT INTO public.account_roles (account_id,roles_id) VALUES ('fdaf785a-d57c-4782-b70e-ce3afb428e68','550e8400-e29b-41d4-a716-446655440000');


----------------SPECIALIST----
INSERT INTO account (is_enabled, version, allergy_profile_id, id, email, first_name, last_name, password, username)
VALUES (true, 0, '4fbb95af-6ede-44e4-9461-09b119262a5b', 'f08d2726-6b03-4745-a1a0-1cbca5b2e3fa','alice.jones@gmail.com', 'Alice', 'Jones', '$2a$10$wnfIPPOwjTDfdDsS/ftUme2wTJKIylqgvfTMIT09dZWsEf3/egywe', 'alice');
INSERT INTO public.account_roles (account_id,roles_id) VALUES ('f08d2726-6b03-4745-a1a0-1cbca5b2e3fa','4c90f86a-0d82-4c51-b72c-80e20949a3b9');

INSERT INTO allergen (allergen_id, name)
VALUES ('2362a32b-30fb-4a53-9c95-ba4ec24a2cf7', 'soja');
INSERT INTO allergen (allergen_id, name)
VALUES ('c07c9cfd-d84b-4be7-bc08-73cb7e28a9e2', 'orzechy');
INSERT INTO allergen (allergen_id, name)
VALUES ('4762a32b-30fb-4a53-9c95-ba4ec24a2cf7', 'laktoza');
INSERT INTO allergen (allergen_id, name)
VALUES ('d07c9cfd-e84b-4be7-bc08-73cb7e28a9e2', 'marchewka');

INSERT INTO allergy_profile(account_id, profile_id )
VALUES ('4c90f86a-0d82-4c51-b72c-80e20949a3b9', '4fbb95af-6ede-44e4-9461-09b119262a5b');
INSERT INTO profile_allergen(intensity, allergen_id, profile_id)
VALUES ('1', '2362a32b-30fb-4a53-9c95-ba4ec24a2cf7', '4fbb95af-6ede-44e4-9461-09b119262a5b');


----------------------------------------------------------

INSERT INTO role (id, version, name) VALUES ('550e8400-e29b-41d4-a716-446655440000', 0, 'ROLE_ADMIN');
INSERT INTO role (id, version, name) VALUES ('4c90f86a-0d82-4c51-b72c-80e20949a3b9', 0, 'ROLE_SPECIALIST');
INSERT INTO role (id, version, name) VALUES ('cd8ab1c1-2431-4e28-88b5-fdd54de3d92a', 0, 'ROLE_USER');

-- Krok 1: Wstaw konto (account)
INSERT INTO account (is_enabled, version, id, email, first_name, last_name, password, username)
VALUES
    (true, 0, '4c90f86a-0d82-4c51-b72c-80e20949a3b9', 'alice.jones@gmail.com', 'Alice', 'Jones', '$2a$10$wnfIPPOwjTDfdDsS/ftUme2wTJKIylqgvfTMIT09dZWsEf3/egywe', 'alice');

-- Krok 2: Wstaw profil alergii (allergy_profile) - teraz masz account_id
INSERT INTO allergy_profile (version, account_id, profile_id)
VALUES (0, '4c90f86a-0d82-4c51-b72c-80e20949a3b9', '4fbb95af-6ede-44e4-9461-09b119262a5b');

-- Krok 3: Wstaw alergeny (allergen)
INSERT INTO allergen(allergen_id, name)
VALUES
    ('2362a32b-30fb-4a53-9c95-ba4ec24a2cf7', 'soja'),
    ('c07c9cfd-d84b-4be7-bc08-73cb7e28a9e2', 'orzechy'),
    ('c17c9cfd-d84b-4be7-bc08-73cb7e28a9e2', 'mleko'),
    ('c27c9cfd-d84b-4be7-bc08-73cb7e28a9e2', 'gluten'),
    ('c37c9cfd-d84b-4be7-bc08-73cb7e28a9e2', 'seler'),
    ('c47c9cfd-d84b-4be7-bc08-73cb7e28a9e2', 'gorczyca'),
    ('c67c9cfd-d84b-4be7-bc08-73cb7e28a9e2', 'mąka pszenna'),
    ('c77c9cfd-d84b-4be7-bc08-73cb7e28a9e2', 'kasza jęczmienna'),
    ('c87c9cfd-d84b-4be7-bc08-73cb7e28a9e2', 'pszenica'),
    ('c97c9cfd-d84b-4be7-bc08-73cb7e28a9e2', 'jaja'),
    ('c99c9cfd-d84b-4be7-bc08-73cb7e28a9e2', 'sezam'),
    ('c44c9cfd-d84b-4be7-bc08-73cb7e28a9e2', 'migdały');

-- Krok 4: Powiąż alergeny z profilem alergii (profile_allergen)
INSERT INTO profile_allergen (intensity, allergen_id, profile_id)
VALUES
    (1, '2362a32b-30fb-4a53-9c95-ba4ec24a2cf7', '4fbb95af-6ede-44e4-9461-09b119262a5b'),
    (1, 'c07c9cfd-d84b-4be7-bc08-73cb7e28a9e2', '4fbb95af-6ede-44e4-9461-09b119262a5b');

