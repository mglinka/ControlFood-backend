-- INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('1e9d8f57-9357-43ea-861f-26df156b7e92', 0, 'gluten', 'ALLERGEN');
-- INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('bb537281-1b90-404e-b574-b14a472b4d84', 0, 'pszenica', 'ALLERGEN');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('ff5f8578-3075-4f72-b446-4ac3a95cfb60', 0, 'żyto', 'ALLERGEN');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('a92e583c-3521-4e98-b4b7-32cb87baf8eb', 0, 'jęczmień', 'ALLERGEN');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('b861387d-f5cc-42a0-8dc5-c8fbd40eb547', 0, 'owies', 'ALLERGEN');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('715cd2e2-31a8-4e94-a0c3-5fb4290a0872', 0, 'orkisz', 'ALLERGEN');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('c92b2d99-40bb-4e29-a7f2-f6d5a9e6cc13', 0, 'kamut', 'ALLERGEN');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('d455e3f8-1c83-4c7a-a4e8-e7e1b823a64b', 0, 'skorupiaki', 'ALLERGEN');
-- INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('9a6149b5-98c2-438d-92d7-499640aa78b6', 0, 'jaja', 'ALLERGEN');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('c7f54cc4-0f8f-4c0b-b409-f16384c1e1f4', 0, 'ryby', 'ALLERGEN');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('ca8c0c4c-b8a3-4fa4-b550-53d1b95fbc72', 0, 'orzeszki ziemne', 'ALLERGEN');
-- INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('ae57bc1c-429c-44da-9946-3e4de8c55d33', 0, 'soja', 'ALLERGEN');
-- INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('b0fdc2e5-1e25-4cc5-b1c9-7aabc11cf342', 0, 'mleko', 'ALLERGEN');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('afc05894-2179-49b8-9186-43dd3177a960', 0, 'laktoza', 'ALLERGEN');
-- INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('9c20b5f9-0583-4b57-b79d-b8c4768db2b4', 0, 'migdały', 'ALLERGEN');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('85eeb1b6-00bb-46b5-b65f-e8d6f08a0c45', 0, 'orzechy laskowe', 'ALLERGEN');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('da348d4a-d422-4a4b-86ba-d192f9de2e4b', 0, 'orzechy włoskie', 'ALLERGEN');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('f946792e-0e26-4934-b659-908774b39d11', 0, 'orzeszki pekan', 'ALLERGEN');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('f5e19944-e13c-4a89-88c1-9ae9d7b460d3', 0, 'orzechy brazylijskie', 'ALLERGEN');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('aa4b8769-0d7d-4cfb-870c-91e8b70e8542', 0, 'pistacje', 'ALLERGEN');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('d053a173-9aa5-4a7c-b184-611d1ab94b8e', 0, 'orzechy makadamia', 'ALLERGEN');
-- INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('df45c2c6-1e3d-4a7e-bc2f-b35f9d8d6bc2', 0, 'seler', 'ALLERGEN');
-- INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('84a4f32f-fbf1-4649-8dcb-d9b2f3b876d1', 0, 'gorczyca', 'ALLERGEN');
-- INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('69bf48d2-0827-4a47-9064-8edfb5c29d89', 0, 'sezam', 'ALLERGEN');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('c6496498-f5ab-4f5b-bb4d-82c2d22f3145', 0, 'dwutlenek siarki', 'ALLERGEN');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('c88c56e2-cdc9-4baf-9a74-1c41735c718b', 0, 'siarczyny', 'ALLERGEN');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('e6f4c3f3-7d93-4e17-b8a4-d59db5d3fdbc', 0, 'łubin', 'ALLERGEN');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('37a9b542-0bdc-45fc-9e3a-5b5f9d5e51c5', 0, 'mięczaki', 'ALLERGEN');

UPDATE allergen
SET type = 'ALLERGEN'
WHERE allergen_id IN (
                      '2362a32b-30fb-4a53-9c95-ba4ec24a2cf7',
                      'c07c9cfd-d84b-4be7-bc08-73cb7e28a9e2',
                      'c17c9cfd-d84b-4be7-bc08-73cb7e28a9e2',
                      'c27c9cfd-d84b-4be7-bc08-73cb7e28a9e2',
                      'c37c9cfd-d84b-4be7-bc08-73cb7e28a9e2',
                      'c47c9cfd-d84b-4be7-bc08-73cb7e28a9e2',
                      'c67c9cfd-d84b-4be7-bc08-73cb7e28a9e2',
                      'c77c9cfd-d84b-4be7-bc08-73cb7e28a9e2',
                      'c87c9cfd-d84b-4be7-bc08-73cb7e28a9e2',
                      'c97c9cfd-d84b-4be7-bc08-73cb7e28a9e2',
                      'c99c9cfd-d84b-4be7-bc08-73cb7e28a9e2',
                      'c44c9cfd-d84b-4be7-bc08-73cb7e28a9e2'
    );


INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('23bca9d7-8c3f-4a74-b8f2-1b673d09fa53', 0, 'fruktoza', 'INTOLERANT_INGREDIENT');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('c9143b45-ec7a-48a5-a48a-785e67c499d4', 0, 'sorbitol', 'INTOLERANT_INGREDIENT');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('b63a5977-3d7c-4566-b9fd-63a8f2f3b76e', 0, 'ksylitol', 'INTOLERANT_INGREDIENT');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('6e1b8e29-5cb9-4a27-8477-f7a25f6b5b69', 0, 'manitol', 'INTOLERANT_INGREDIENT');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('bf6d3c82-02a9-4935-b49c-45e518f296be', 0, 'glutaminian sodu (MSG)', 'INTOLERANT_INGREDIENT');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('794aa6f1-8b0b-4649-92e2-f90be4f6e365', 0, 'benzoesan sodu', 'INTOLERANT_INGREDIENT');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('1672d3a8-1f8c-40d4-b173-fc02645dd3bb', 0, 'aspartam', 'INTOLERANT_INGREDIENT');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('f2544f64-2c89-4fa1-88b4-bf5a8432c665', 0, 'kazeina', 'INTOLERANT_INGREDIENT');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('caf827a5-5649-4e1d-a35a-16314dcb374a', 0, 'kofeina', 'INTOLERANT_INGREDIENT');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('a7345e2b-6294-4b92-9755-792a9b45f3be', 0, 'histamina', 'INTOLERANT_INGREDIENT');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('fb173c37-0cf6-4c95-b45e-3a2d569e3f47', 0, 'salicylany', 'INTOLERANT_INGREDIENT');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('e1ab426e-3b8d-437f-a6d5-9f72c5e2a72e', 0, 'glikoalkaloidy', 'INTOLERANT_INGREDIENT');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('74bd1ff8-495d-4ad2-b8d9-168d9b6e16b7', 0, 'alkaloidy', 'INTOLERANT_INGREDIENT');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('5920d68d-73e2-4a62-8436-dcbb3e3b1787', 0, 'nikiel', 'INTOLERANT_INGREDIENT');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('396f3623-5b5f-4b14-8a19-7f84c6236b41', 0, 'lecytyna sojowa', 'INTOLERANT_INGREDIENT');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('b418c286-7ae8-4640-a45a-1c1f2bdc9e83', 0, 'fenyloalanina', 'INTOLERANT_INGREDIENT');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('e4a9b3c5-f741-4c1c-8654-4a26f61e9b73', 0, 'gluten wolny od pszenicy', 'INTOLERANT_INGREDIENT');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('5ecf5d7f-172b-4d72-b8c6-bb8c731b4289', 0, 'alkohol', 'INTOLERANT_INGREDIENT');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('91ad7b96-1e88-41e6-9b75-c9a4dcb7d1b6', 0, 'sól', 'INTOLERANT_INGREDIENT');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('837be18b-fb35-41d4-9e32-2ea31d482896', 0, 'cukier', 'INTOLERANT_INGREDIENT');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('aa762ed6-4308-4a99-b9fd-601df92eb9c7', 0, 'ocet', 'INTOLERANT_INGREDIENT');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('d51e679a-6ec6-44e9-93a7-b1d3cf186eb6', 0, 'białko grochu', 'INTOLERANT_INGREDIENT');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('68f147c2-c5e8-45e4-9a33-1cd4d4a94d89', 0, 'drożdże', 'INTOLERANT_INGREDIENT');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('3725f0d9-8f14-4085-8739-94eecf21f654', 0, 'karagen', 'INTOLERANT_INGREDIENT');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('b56fef74-d3e4-4869-bcdd-58c6b5d9fcf2', 0, 'guar', 'INTOLERANT_INGREDIENT');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('87a07df4-8a62-4a73-9216-7337b8b53bd8', 0, 'guma ksantanowa', 'INTOLERANT_INGREDIENT');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('c7df2a46-5e5b-4ae7-bc9a-3447d4ecf456', 0, 'guma arabska', 'INTOLERANT_INGREDIENT');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('3bcbe905-28f3-40bb-aab7-18cf5c891de4', 0, 'karmel amoniakalny', 'INTOLERANT_INGREDIENT');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('c913d013-6384-4a99-8d75-5eae3cfb75e6', 0, 'błonnik pszenny', 'INTOLERANT_INGREDIENT');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('f6c428b3-6b0a-4643-a72f-bfcecc60e4d7', 0, 'kukurydza', 'INTOLERANT_INGREDIENT');
INSERT INTO public.allergen (allergen_id, version, name, type) VALUES ('13f2497e-8a8a-4057-9009-bde19ebacb98', 0, 'grzyby', 'INTOLERANT_INGREDIENT');
