-- V1: Script inicial para crear las tablas de usuarios y teléfonos, incluyendo todos los campos heredados.

CREATE SCHEMA IF NOT EXISTS schema_test;

-- Tabla para almacenar la información de los usuarios.
CREATE TABLE schema_test.usuario_t (
    -- Campos heredados de la clase Control
                           id          UUID         NOT NULL PRIMARY KEY,
                           created     TIMESTAMP    NOT NULL,
                           modified    TIMESTAMP,
                           last_login  TIMESTAMP    NOT NULL,
                           token       VARCHAR(255) NOT NULL,
                           active      BOOLEAN      NOT NULL,

    -- Campos propios de la clase Usuario
                           name        VARCHAR(255) NOT NULL,
                           email       VARCHAR(255) NOT NULL UNIQUE,
                           password    VARCHAR(255) NOT NULL
);

-- Tabla para almacenar los teléfonos asociados a los usuarios.
CREATE TABLE schema_test.phone_t (
                         id          UUID         NOT NULL PRIMARY KEY,
                         number      VARCHAR(255) NOT NULL,
                         city_code   VARCHAR(255) NOT NULL,
                         contry_code VARCHAR(255) NOT NULL,
                         usuario_id  UUID         NOT NULL,
                         CONSTRAINT fk_phone_to_usuario FOREIGN KEY (usuario_id) REFERENCES usuario_t(id)
);