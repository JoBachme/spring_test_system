INSERT INTO
    students (first_name, last_name)
VALUES
    ('Ada', 'Lovelace'),
    ('Grace', 'Hopper'),
    ('Katherine', 'Johnson'),
    ('Alan', 'Turing'),
    ('Edsger', 'Dijkstra'),
    ('Barbara', 'Liskov'),
    ('Donald', 'Knuth');

INSERT INTO
    tests (test_name)
VALUES
    ('Big Data'),
    ('Software Engineering'),
    ('Logic'),
    ('NLP'),
    ('Systems Design'),
    ('Physics'),
    ('Chemistry'),
    ('Biology');

INSERT INTO
    notification_sample (text_sample)
VALUES
    ('Hello $firstName $lastName,\nYour result for $testName is: $result.\nRegards, Spring Test System.');

INSERT INTO
    students_tests (student_id, test_id, passed_flag, tries)
VALUES
    (1, 3, TRUE, 1), (1, 4, FALSE, 2), (1, 5, TRUE, 1), (1, 7, FALSE, 3), (1, 8, FALSE, 3),
    (2, 1, FALSE, 1), (2, 6, TRUE, 2), (2, 7, FALSE, 1), (2, 8, TRUE, 1),
    (3, 2, FALSE, 1), (3, 4, TRUE, 1), (3, 6, TRUE, 1), (3, 7, FALSE, 3),
    (4, 1, TRUE, 1), (4, 3, TRUE, 1), (4, 4, FALSE, 3),
    (5, 1, FALSE, 2), (5, 2, FALSE, 1), (5, 3, FALSE, 3), (5, 6, FALSE, 2), (5, 7, TRUE, 2), (5, 8, TRUE, 1),
    (7, 2, TRUE, 1), (7, 3, TRUE, 1), (7, 5, FALSE, 3), (7, 6, TRUE, 1);

INSERT INTO
    students_tests (student_id, test_id)
VALUES
    (1, 1), (1, 2), (2, 2), (2, 3), (3, 1), (4, 8), (6, 1), (6, 3), (6, 5), (7, 7), (7, 8);
