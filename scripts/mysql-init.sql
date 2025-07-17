CREATE TABLE IF NOT EXISTS patients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    birthdate DATE,
    gender VARCHAR(1),
    address VARCHAR(255),
    phone VARCHAR(50)
    );

INSERT INTO patients (id, first_name, last_name, birthdate, gender, address, phone) VALUES
                                                                                     (1, 'Test', 'TestNone', '1966-12-31', 'F', '1 Brookside St', '100-222-3333'),
                                                                                     (2, 'Test', 'TestBorderline', '1945-06-24', 'M', '2 High St', '200-333-4444'),
                                                                                     (3, 'Test', 'TestInDanger', '2004-06-18', 'M', '3 Club Road', '300-444-5555'),
                                                                                     (4, 'Test', 'TestEarlyOnset', '2002-06-28', 'F', '4 Valley Dr', '400-555-6666');
