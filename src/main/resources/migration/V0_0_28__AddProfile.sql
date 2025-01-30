-- Wstawienie nowego rekordu do tabeli allergy_profile
INSERT INTO allergy_profile (profile_id, version)
VALUES ('f472ad38-70a9-418d-8aba-2851c5ecc174',0);  -- Przykładowa wartość wersji

-- Przykład wstawiania danych do profile_allergen
INSERT INTO profile_allergen (profile_id, allergen_id, intensity)
VALUES
    ('f472ad38-70a9-418d-8aba-2851c5ecc174', 'ff5f8578-3075-4f72-b446-4ac3a95cfb60', 'High'),   -- Przykład 1 żyto
    ('f472ad38-70a9-418d-8aba-2851c5ecc174', 'a92e583c-3521-4e98-b4b7-32cb87baf8eb', 'Medium'),  -- Przykład 2 jęczmień
    ('f472ad38-70a9-418d-8aba-2851c5ecc174', 'b861387d-f5cc-42a0-8dc5-c8fbd40eb547', 'Low'),     -- Przykład 3 owies
    ('f472ad38-70a9-418d-8aba-2851c5ecc174', 'c17c9cfd-d84b-4be7-bc08-73cb7e28a9e2', 'High'),    -- Przykład 4 mleko
    ('f472ad38-70a9-418d-8aba-2851c5ecc174', 'c92b2d99-40bb-4e29-a7f2-f6d5a9e6cc13', 'Low'),     -- Przykład 5 kamut
    ('f472ad38-70a9-418d-8aba-2851c5ecc174', '23bca9d7-8c3f-4a74-b8f2-1b673d09fa53', 'Low'), -- fruktoza
    ('f472ad38-70a9-418d-8aba-2851c5ecc174', '5920d68d-73e2-4a62-8436-dcbb3e3b1787', 'High');-- nikiel

UPDATE public.account
SET allergy_profile_id = 'f472ad38-70a9-418d-8aba-2851c5ecc174'
WHERE id = '038e200a-188c-4eb5-acf5-00f296efe0f9';
