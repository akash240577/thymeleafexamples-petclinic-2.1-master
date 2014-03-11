CREATE DATABASE IF NOT EXISTS petclinic;
GRANT ALL PRIVILEGES ON petclinic.* TO pc@localhost
IDENTIFIED BY 'pc';

USE petclinic;

CREATE TABLE IF NOT EXISTS vets (
  id         INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(30),
  last_name  VARCHAR(30),
  INDEX (last_name)
)
  ENGINE =InnoDB;

CREATE TABLE IF NOT EXISTS specialties (
  id   INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(80),
  INDEX (name)
)
  ENGINE =InnoDB;

CREATE TABLE IF NOT EXISTS vet_specialties (
  vet_id       INT UNSIGNED NOT NULL,
  specialty_id INT UNSIGNED NOT NULL,
  FOREIGN KEY (vet_id) REFERENCES vets (id),
  FOREIGN KEY (specialty_id) REFERENCES specialties (id),
  UNIQUE (vet_id, specialty_id)
)
  ENGINE =InnoDB;

CREATE TABLE IF NOT EXISTS types (
  id   INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(80),
  INDEX (name)
)
  ENGINE =InnoDB;

CREATE TABLE IF NOT EXISTS owners (
  id         INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(30),
  last_name  VARCHAR(30),
  address    VARCHAR(255),
  city       VARCHAR(80),
  telephone  VARCHAR(20),
  INDEX (last_name)
)
  ENGINE =InnoDB;

CREATE TABLE IF NOT EXISTS pets (
  id         INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name       VARCHAR(30),
  birth_date DATE,
  type_id    INT UNSIGNED NOT NULL,
  owner_id   INT UNSIGNED NOT NULL,
  INDEX (name),
  FOREIGN KEY (owner_id) REFERENCES owners (id),
  FOREIGN KEY (type_id) REFERENCES types (id)
)
  ENGINE =InnoDB;

CREATE TABLE IF NOT EXISTS visits (
  id          INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  pet_id      INT UNSIGNED NOT NULL,
  visit_date  DATE,
  description VARCHAR(255),
  FOREIGN KEY (pet_id) REFERENCES pets (id)
)
  ENGINE =InnoDB;

CREATE TABLE IF NOT EXISTS users (
  id           INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  username     VARCHAR(255) NOT NULL UNIQUE,
  password     VARCHAR(30)  NOT NULL,
  first_name   VARCHAR(30),
  last_name    VARCHAR(30),
  address      VARCHAR(255),
  city         VARCHAR(80),
  telephone    VARCHAR(20),
  expired TINYINT(1) NOT NULL DEFAULT '0',
  created_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_date TIMESTAMP    NOT NULL DEFAULT '0000-00-00 00:00:00',
  INDEX (username)
)
  ENGINE =InnoDB;

CREATE TABLE IF NOT EXISTS roles (
  id   INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL UNIQUE,
  INDEX (name)
)
  ENGINE =InnoDB;

CREATE TABLE IF NOT EXISTS user_roles (
  id      INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user_id INT REFERENCES `users`,
  role_id INT REFERENCES `roles`
)
  ENGINE =InnoDB;