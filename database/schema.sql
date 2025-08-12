-- Library Management System Database Schema
-- Run this script in MySQL to create the database structure

CREATE DATABASE IF NOT EXISTS library_management;
USE library_management;

-- Create users table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phoneNumber VARCHAR(20) NOT NULL,
    borrowLimit INT NOT NULL,
    borrowedBooksCount INT NOT NULL
);

-- Create documents table
-- Create documents table
CREATE TABLE documents (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    language VARCHAR(100),
    pages INT,
    author VARCHAR(255) NOT NULL,
    publication_year INT NOT NULL,
    is_available BOOLEAN DEFAULT TRUE
);

-- Create borrows table
CREATE TABLE borrowed_documents (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    document_id INT NOT NULL,
    borrow_date DATETIME NOT NULL,
    return_date DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (document_id) REFERENCES documents(id)
);

CREATE TABLE accounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
ALTER TABLE accounts
ADD COLUMN phone VARCHAR(15) AFTER password;
ALTER TABLE accounts
ADD COLUMN email VARCHAR(100);