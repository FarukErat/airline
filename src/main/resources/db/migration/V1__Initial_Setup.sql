-- Create Airline Table
CREATE TABLE airline (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         callsign VARCHAR(255),
                         founded_year INT NOT NULL,
                         base_airport VARCHAR(255)
);

-- Create Aircraft Table
CREATE TABLE aircraft (
                          id SERIAL PRIMARY KEY,
                          manufacturer_serial_number VARCHAR(255) NOT NULL,
                          type VARCHAR(255),
                          model VARCHAR(255),
                          number_of_engines INT NOT NULL,
                          airline_id BIGINT,
                          FOREIGN KEY (airline_id) REFERENCES airline (id) ON DELETE SET NULL
);

-- Create UserDetails Table
CREATE TABLE user_details (
                              id SERIAL PRIMARY KEY,
                              username VARCHAR(255) NOT NULL UNIQUE,
                              hashed_password VARCHAR(255) NOT NULL,
                              roles INT NOT NULL
);