-- Tworzenie użytkowników
CREATE USER admin WITH PASSWORD 'admin';
CREATE USER auth WITH PASSWORD 'auth';
CREATE USER mok WITH PASSWORD 'mok';
CREATE USER mopa WITH PASSWORD 'mopa';

-- Nadanie pełnych uprawnień do schematu 'public' dla użytkownika 'admin'
GRANT ALL PRIVILEGES ON SCHEMA public TO admin WITH GRANT OPTION;

-- Nadanie pełnych uprawnień do bazy danych 'postgres' dla użytkownika 'admin'
GRANT ALL PRIVILEGES ON DATABASE postgres TO admin;
