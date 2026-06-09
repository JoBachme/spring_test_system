CREATE TABLE test_attempts (
    attempt_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    test_id INT NOT NULL,
    attempt_number INT NOT NULL CHECK (attempt_number BETWEEN 1 AND 3),
    result VARCHAR(20) NOT NULL CHECK (result IN ('PASSED', 'FAILED')),
    submitted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_test_attempts_student FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE,
    CONSTRAINT fk_test_attempts_test FOREIGN KEY (test_id) REFERENCES tests (id) ON DELETE CASCADE,
    CONSTRAINT uk_test_attempts_student_test_attempt UNIQUE (student_id, test_id, attempt_number)
);

UPDATE tests
SET graded = TRUE
WHERE id IN (
    SELECT DISTINCT test_id
    FROM students_tests
    WHERE tries > 0
);

INSERT INTO test_attempts (student_id, test_id, attempt_number, result, submitted_at)
SELECT student_id, test_id, 1,
       CASE WHEN tries = 1 AND passed_flag = TRUE THEN 'PASSED' ELSE 'FAILED' END,
       CURRENT_TIMESTAMP
FROM students_tests
WHERE tries >= 1
UNION ALL
SELECT student_id, test_id, 2,
       CASE WHEN tries = 2 AND passed_flag = TRUE THEN 'PASSED' ELSE 'FAILED' END,
       CURRENT_TIMESTAMP
FROM students_tests
WHERE tries >= 2
UNION ALL
SELECT student_id, test_id, 3,
       CASE WHEN tries = 3 AND passed_flag = TRUE THEN 'PASSED' ELSE 'FAILED' END,
       CURRENT_TIMESTAMP
FROM students_tests
WHERE tries >= 3;
