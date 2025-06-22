DROP DATABASE IF EXISTS books;
CREATE DATABASE books;
USE books;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
)

CREATE TABLE IF NOT EXISTS books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(50) NOT NULL,
    year_published INT,
    genre VARCHAR(50),
    rating DECIMAL(2, 1) CHECK (rating >= 0 AND rating <= 5),
    page_count INT CHECK (page_count > 0),
);

CREATE TABLE IF NOT EXISTS user_books (
    user_id INT NOT NULL,
    book_id INT NOT NULL,
    status ENUM('NOT_STARTED', 'IN_PROGRESS', 'COMPLETED') NOT NULL DEFAULT 'NOT_STARTED',
    user_rating INT CHECK (user_rating >= 0 AND user_rating <= 5),
    current_page INT CHECK (current_page >= 0),
    PRIMARY KEY (user_id, book_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
)

