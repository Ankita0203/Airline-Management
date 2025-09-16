-- Drop old database if exists
DROP DATABASE IF EXISTS airline;

-- Create database
CREATE DATABASE airline;
USE airline;

-- Create flights table
CREATE TABLE flights (
    flight_id INT AUTO_INCREMENT PRIMARY KEY,
    flight_code VARCHAR(50) NOT NULL UNIQUE
);

-- Insert sample flights (no duplicates)
INSERT INTO flights (flight_code)
VALUES ('AI101'), ('AI102'), ('AI201'), ('AI202'), ('AI303');

-- Create bookings table
CREATE TABLE bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    passenger_name VARCHAR(100) NOT NULL,
    flight_id INT NOT NULL,
    travel_date DATE NOT NULL,
    FOREIGN KEY (flight_id) REFERENCES flights(flight_id)
);
