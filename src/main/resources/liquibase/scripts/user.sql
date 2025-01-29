-- liquibase formatted sql

-- changeset jrembo:1
CREATE TABLE students (
    id SERIAL,
    name TEXT
)
CREATE TABLE faculty (
    id SERIAL,
    name TEXT,
    color TEXT
)

CREATE INDEX students_name_index ON students (name);
CREATE INDEX faculty_name_color_index ON faculty (name, color);