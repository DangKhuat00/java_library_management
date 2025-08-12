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
    ADD COLUMN role VARCHAR(50) AFTER phone;

    ALTER TABLE accounts
    ADD COLUMN email VARCHAR(100);


ALTER TABLE users ADD CONSTRAINT unique_phone UNIQUE (phoneNumber);



-- Insert sample documents
INSERT INTO documents (title, language, pages, author, publication_year, is_available) VALUES
('Truyện Kiều', 'Tiếng Việt', 254, 'Nguyễn Du', 1820, TRUE),
('Nỗi Buồn Chiến Tranh', 'Tiếng Việt', 283, 'Bảo Ninh', 1991, TRUE),
('Kính Vạn Hoa – tổng hợp', 'Tiếng Việt', 300, 'Nguyễn Nhật Ánh', 1984, TRUE),
('Cho Tôi Xin Một Vé Đi Tuổi Thơ', 'Tiếng Việt', 200, 'Nguyễn Nhật Ánh', 2008, TRUE),
('Cánh Đồng Bất Tận', 'Tiếng Việt', 152, 'Nguyễn Ngọc Tư', 2005, TRUE),
('Số Đỏ', 'Tiếng Việt', 180, 'Vũ Trọng Phụng', 1936, TRUE),
('Paradise of the Blind', 'Tiếng Anh', 208, 'Dương Thu Hương', 1988, TRUE),
('Root Fractures', 'Tiếng Anh', 80, 'Diana Khoi Nguyen', 2024, TRUE),
('The Sympathizer', 'Tiếng Anh', 384, 'Viet Thanh Nguyen', 2015, TRUE),
('The Mountains Sing', 'Tiếng Anh', 336, 'Nguyễn Phan Quế Mai', 2020, TRUE),
('On Earth We’re Briefly Gorgeous', 'Tiếng Anh', 256, 'Ocean Vuong', 2019, TRUE),
('The Sorrow of War', 'Tiếng Anh', 283, 'Bảo Ninh', 1991, TRUE),
('The Vietnamese Gulag', 'Tiếng Anh', 351, 'Doan Van Toai', 1979, TRUE),
('Journey of Childhood', 'Tiếng Anh', 220, 'Dương Thu Hương', 1985, TRUE),
('Ghost of', 'Tiếng Anh', 72, 'Diana Khoi Nguyen', 2018, TRUE),
('Beyond Illusions', 'Tiếng Anh', 240, 'Dương Thu Hương', 1987, TRUE),
('Idol Fields / Cánh Đồng Bất Tận (bản Anh)', 'Tiếng Anh', 152, 'Nguyễn Ngọc Tư', 2005, TRUE),
('Endless Field', 'Tiếng Anh', 200, 'Nguyễn Ngọc Tư', 2005, TRUE),
('The Land I Lost', 'Tiếng Anh', 144, 'Quang-Nhuong Huynh', 1982, TRUE),
('Anam', 'Tiếng Anh', 220, 'Andre Dao', 2023, TRUE);
