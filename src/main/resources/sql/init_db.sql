DROP DATABASE IF EXISTS blowords;
CREATE DATABASE IF NOT EXISTS blowords;

USE blowords;

DROP TABLE IF EXISTS blowords.user;
CREATE TABLE IF NOT EXISTS blowords.user (
    userid INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    role VARCHAR(10) DEFAULT 'user',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO blowords.user (username, password, email, role) 
VALUES ('admin998', '2443531e9+7', 'admin@example.com', 'admin');

