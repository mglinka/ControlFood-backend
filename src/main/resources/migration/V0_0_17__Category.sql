-- V1__create_categories_table.sql

-- Tworzymy tabelę categories
CREATE TABLE category (
                            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),   -- UUID jako klucz główny, generowany automatycznie
                            version BIGINT NOT NULL,                         -- Pole wersji do optymistycznego blokowania
                            name VARCHAR(255) NOT NULL UNIQUE                -- Unikalna nazwa kategorii
);


INSERT INTO public.category (id, version, name) VALUES ('dcb3f437-bc54-46f1-b683-b73a4c46f6fe', 1, 'Nabiał');
INSERT INTO public.category (id, version, name) VALUES ('c759d843-5e26-4155-b470-b2e7c3469078', 1, 'Mięso');
INSERT INTO public.category (id, version, name) VALUES ('3d502078-d917-48ef-a303-71e044a2f7ae', 1, 'Zupy');
INSERT INTO public.category (id, version, name) VALUES ('585f1b28-7091-4b63-abce-0765991bd608', 1, 'Warzywa');
INSERT INTO public.category (id, version, name) VALUES ('4d108b27-9a76-462c-a925-f52f5a0e1577', 1, 'Owoce');
INSERT INTO public.category (id, version, name) VALUES ('785a5683-6500-432a-ab02-0cd4fab24bfe', 1, 'Bezglutenowe');
INSERT INTO public.category (id, version, name) VALUES ('142aefbf-b503-4328-8773-b9858efd645f', 1, 'Przekąski');
INSERT INTO public.category (id, version, name) VALUES ('2f91f09c-7438-461a-8058-869a8efb46cd', 1, 'Batoniki białkowe');
INSERT INTO public.category (id, version, name) VALUES ('22dde531-6095-4d7c-b08f-264e1156bdb3', 1, 'Herbaty');
INSERT INTO public.category (id, version, name) VALUES ('27bd04e8-e748-4768-a1ec-7b361c20ff41', 1, 'Śniadanie');
INSERT INTO public.category (id, version, name) VALUES ('82a6a5fd-d160-4519-94ae-ca590d1f68b8', 1, 'Tłuszcze');
INSERT INTO public.category (id, version, name) VALUES ('bf06e3f7-eaef-4586-942f-1bd7b169f5a8', 1, 'Napoje');
