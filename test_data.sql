-- Test data for test_relacije database
USE test_relacije;

-- Clear existing data (optional)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE poruka;
TRUNCATE TABLE upisi;
TRUNCATE TABLE adresa;
TRUNCATE TABLE zaposlenik;
TRUNCATE TABLE kategorija;
TRUNCATE TABLE korisnik;
TRUNCATE TABLE osoba;
TRUNCATE TABLE odjel;
TRUNCATE TABLE student;
TRUNCATE TABLE kolegij;
SET FOREIGN_KEY_CHECKS = 1;

-- ONE TO ONE: osoba i adresa
INSERT INTO osoba (ime) VALUES 
('Marko Marković'),
('Ana Anić'),
('Petar Petrović'),
('Maja Majić'),
('Ivo Ivić');

-- Adresa koristi isti ID kao osoba (ONE TO ONE)
INSERT INTO adresa (id, ulica) VALUES 
(1, 'Ilica 10, Zagreb'),
(2, 'Trg bana Jelačića 5, Zagreb'),
(3, 'Vukovarska 12, Osijek'),
(4, 'Riva 8, Split'),
(5, 'Strossmayerov trg 3, Rijeka');

-- MANY TO ONE / ONE TO MANY: odjel i zaposlenik
INSERT INTO odjel (naziv) VALUES 
('IT Odjel'),
('Računovodstvo'),
('Marketing'),
('Prodaja'),
('HR');

INSERT INTO zaposlenik (ime, odjel_id) VALUES 
('Milan Milanović', 1),    -- IT
('Sandra Sandrić', 1),     -- IT
('Tomislav Tomić', 2),     -- Računovodstvo
('Lucija Lucić', 2),       -- Računovodstvo
('Stjepan Stjepanović', 3), -- Marketing
('Petra Petrić', 3),       -- Marketing
('Danijel Danić', 4),      -- Prodaja
('Monika Monić', 4),       -- Prodaja
('Robert Robertić', 5),    -- HR
('Kristina Kristić', 1);   -- IT

-- MANY TO MANY: student, kolegij, upisi
INSERT INTO student (ime) VALUES 
('Luka Lukić'),
('Tea Teić'),
('Filip Filipović'),
('Sara Sarić'),
('David Davidović'),
('Nina Ninić');

INSERT INTO kolegij (naziv) VALUES 
('Programiranje u Javi'),
('Baze podataka'),
('Web razvoj'),
('Algoritmi i strukture podataka'),
('Objektno orijentirano programiranje'),
('Matematika');

-- Upisi studenata na kolegije (MANY TO MANY)
INSERT INTO upisi (student_id, kolegij_id, datum) VALUES 
(1, 1, '2024-09-01 09:00:00'),  -- Luka - Java
(1, 2, '2024-09-01 10:00:00'),  -- Luka - Baze
(1, 4, '2024-09-01 11:00:00'),  -- Luka - Algoritmi
(2, 1, '2024-09-02 09:00:00'),  -- Tea - Java
(2, 3, '2024-09-02 10:00:00'),  -- Tea - Web
(2, 5, '2024-09-02 11:00:00'),  -- Tea - OOP
(3, 2, '2024-09-03 09:00:00'),  -- Filip - Baze
(3, 3, '2024-09-03 10:00:00'),  -- Filip - Web
(3, 6, '2024-09-03 11:00:00'),  -- Filip - Matematika
(4, 1, '2024-09-04 09:00:00'),  -- Sara - Java
(4, 4, '2024-09-04 10:00:00'),  -- Sara - Algoritmi
(4, 5, '2024-09-04 11:00:00'),  -- Sara - OOP
(5, 2, '2024-09-05 09:00:00'),  -- David - Baze
(5, 3, '2024-09-05 10:00:00'),  -- David - Web
(5, 6, '2024-09-05 11:00:00'),  -- David - Matematika
(6, 1, '2024-09-06 09:00:00'),  -- Nina - Java
(6, 5, '2024-09-06 10:00:00'),  -- Nina - OOP
(6, 6, '2024-09-06 11:00:00');  -- Nina - Matematika

-- SELF REFERENCING: kategorija (hijerarhijska struktura)
INSERT INTO kategorija (naziv, id_nadkategorija) VALUES 
('Elektronika', NULL),          -- 1 - root kategorija
('Računala', 1),                -- 2 - podkategorija od Elektronika
('Telefoni', 1),                -- 3 - podkategorija od Elektronika
('Laptopi', 2),                 -- 4 - podkategorija od Računala
('Desktop računala', 2),        -- 5 - podkategorija od Računala
('Gaming laptopi', 4),          -- 6 - podkategorija od Laptopi
('Poslovni laptopi', 4),        -- 7 - podkategorija od Laptopi
('Android telefoni', 3),        -- 8 - podkategorija od Telefoni
('iPhone', 3),                  -- 9 - podkategorija od Telefoni
('Odjeća', NULL),               -- 10 - druga root kategorija
('Muška odjeća', 10),           -- 11 - podkategorija od Odjeća
('Ženska odjeća', 10),          -- 12 - podkategorija od Odjeća
('Košulje', 11),                -- 13 - podkategorija od Muška odjeća
('Hlače', 11),                  -- 14 - podkategorija od Muška odjeća
('Haljine', 12),                -- 15 - podkategorija od Ženska odjeća
('Suknje', 12);                 -- 16 - podkategorija od Ženska odjeća

-- KOMPOZITNI KLJUČ: korisnik i poruke
INSERT INTO korisnik (korisnicko_ime, domena, email) VALUES 
('marko', 'gmail.com', 'marko@gmail.com'),
('ana', 'hotmail.com', 'ana@hotmail.com'),
('petar', 'yahoo.com', 'petar@yahoo.com'),
('maja', 'gmail.com', 'maja@gmail.com'),
('ivo', 'outlook.com', 'ivo@outlook.com'),
('admin', 'company.hr', 'admin@company.hr'),
('user1', 'company.hr', 'user1@company.hr'),
('test', 'test.com', 'test@test.com');

INSERT INTO poruka (sadrzaj, posiljatelj_ime, posiljatelj_domena) VALUES 
('Pozdrav svima!', 'marko', 'gmail.com'),
('Kako ste danas?', 'ana', 'hotmail.com'),
('Trebam pomoć s projektom.', 'petar', 'yahoo.com'),
('Hvala na pomoći!', 'maja', 'gmail.com'),
('Dobar dan!', 'ivo', 'outlook.com'),
('Systemska poruka - server je aktivan.', 'admin', 'company.hr'),
('Molim provjeru dokumentacije.', 'user1', 'company.hr'),
('Test poruka 1', 'test', 'test.com'),
('Test poruka 2', 'test', 'test.com'),
('Još jedna poruka od Marka', 'marko', 'gmail.com'),
('Ana ponovno piše', 'ana', 'hotmail.com'),
('Petar šalje update', 'petar', 'yahoo.com'),
('Maja odgovara', 'maja', 'gmail.com'),
('Ivo se javlja ponovo', 'ivo', 'outlook.com'),
('Admin poruka 2', 'admin', 'company.hr');
