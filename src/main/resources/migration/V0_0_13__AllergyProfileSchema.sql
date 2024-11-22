-- 1. Create the allergy_profile_schema table
CREATE TABLE allergy_profile_schema (
                                        schema_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),   -- UUID as primary key, auto-generated
                                        version BIGINT NOT NULL,                                -- Optimistic locking version field
                                        name VARCHAR(255) NOT NULL                              -- Name column, cannot be null
);


-- 1. Create the allergy_profile_schema_allergen table
CREATE TABLE allergy_profile_schema_allergen (
                                                 schema_id UUID NOT NULL,                                -- Foreign key to allergy_profile_schema
                                                 allergen_id UUID NOT NULL,                              -- Foreign key to allergen
                                                 PRIMARY KEY (schema_id, allergen_id),                   -- Composite primary key to ensure uniqueness
                                                 CONSTRAINT fk_schema FOREIGN KEY (schema_id) REFERENCES allergy_profile_schema(schema_id) ON DELETE CASCADE,
                                                 CONSTRAINT fk_allergen FOREIGN KEY (allergen_id) REFERENCES allergen(allergen_id) ON DELETE CASCADE
);