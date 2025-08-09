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
    remain_docs INT DEFAULT 0
);

-- Create borrows table
CREATE TABLE borrowed_documents (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    document_id INT NOT NULL,
    MODIFY borrow_date DATETIME NOT NULL,
    MODIFY return_date DATETIME;
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (document_id) REFERENCES documents(id)
);
