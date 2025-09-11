DELETE FROM patients;

INSERT INTO patients (id, first_name, last_name, birthdate, gender, address, phone) VALUES
   (1, 'Jean', 'Dupont', '1985-05-15', 'M', '123 rue de Paris, 75001', '+33123456789'),
   (2, 'Marie', 'Martin', '1990-08-22', 'F', '456 avenue des Champs, 75008', '+33198765432'),
   (3, 'Pierre', 'Durand', '1978-12-03', 'M', '789 boulevard Saint-Germain, 75006', '+33234567890');

SELECT 'Données de test chargées dans patient_test_db' AS status;
