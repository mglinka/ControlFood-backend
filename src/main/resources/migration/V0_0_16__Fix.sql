ALTER TABLE allergen
    ALTER COLUMN type TYPE type_enum USING type::text::type_enum;
