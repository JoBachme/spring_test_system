CREATE TABLE IF NOT EXISTS notification_sample (
    notification_name VARCHAR(300) PRIMARY KEY DEFAULT 'notification_sample',
    text_sample TEXT
);

CREATE TABLE IF NOT EXISTS students (
    id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(300) NOT NULL CHECK (LENGTH(first_name) > 0),
    last_name VARCHAR(300) NOT NULL CHECK (LENGTH(last_name) > 0)
);

CREATE TABLE IF NOT EXISTS tests (
    id INT PRIMARY KEY AUTO_INCREMENT,
    test_name VARCHAR(300) NOT NULL CHECK (LENGTH(test_name) > 0),
    graded BOOLEAN DEFAULT false
);

CREATE TABLE IF NOT EXISTS students_tests (
    student_id INT,
    test_id INT,
    passed_flag BOOLEAN DEFAULT false,
    tries INT DEFAULT 0 CHECK (tries BETWEEN 0 AND 3),
    PRIMARY KEY (student_id, test_id),
    CONSTRAINT fk_students_tests_student FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE,
    CONSTRAINT fk_students_tests_test FOREIGN KEY (test_id) REFERENCES tests (id) ON DELETE CASCADE
);
