CREATE TABLE actions (
    id SERIAL PRIMARY KEY,
    ip VARCHAR(255),
    input_text TEXT,
    output_text TEXT
);