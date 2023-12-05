USE test_library;

CREATE TABLE AudioBook
(
    book_id BIGINT PRIMARY KEY unique not null,
    FOREIGN KEY (book_id) REFERENCES Book (id),
    runtime BIGINT                    not null
);
CREATE TABLE Ebook
(
    book_id       BIGINT PRIMARY KEY unique not null,
    FOREIGN KEY (book_id) REFERENCES Book (id),
    document_type VARCHAR(50)               not null
);

-- Insert data into Book table
INSERT INTO Book (author, title, publishedDate, quantity)
VALUES ('Author1', 'Book1', '2022-01-01', 8912),
       ('Author2', 'Book2', '2022-02-15', 6969),
       ('Author3', 'Book3', '2022-03-20', 3),
       ('Author4', 'Book4', '2022-04-10', 80085),
       ('Author5', 'Book5', '2022-05-25', 1);


-- Insert data into AudioBook table
INSERT INTO AudioBook (book_id, runtime)
VALUES (1, 3600), -- 1 hour
       (5, 3000);
-- 50 minutes

-- Insert data into Ebook table
INSERT INTO Ebook (book_id, document_type)
VALUES (3, 'PDF'),
       (4, 'EPUB');

