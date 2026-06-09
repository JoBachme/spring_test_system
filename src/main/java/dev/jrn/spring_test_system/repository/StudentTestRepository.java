package dev.jrn.spring_test_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dev.jrn.spring_test_system.entity.StudentTest;
import dev.jrn.spring_test_system.entity.StudentTestId;
import dev.jrn.spring_test_system.entity.StudentTestQueryProjection;

@Repository
public interface StudentTestRepository extends JpaRepository<StudentTest, StudentTestId> {
    
    StudentTest findByStudentTestId(StudentTestId studentTestId);

    @Query(value = "SELECT * FROM students_tests st", nativeQuery = true)
    List<StudentTest> getAllCombinations();

    @Query(value = "SELECT * FROM students_tests st WHERE st.student_id=?1 and st.test_id=?2", nativeQuery = true)
    StudentTest getCombination(Integer studentId, Integer testId);

    @Query(value = "SELECT s.first_name AS firstName, s.last_name AS lastName, t.test_name AS testName, st.passed_flag AS passedFlag, st.tries AS tries FROM students as s\n" + //
            "INNER JOIN students_tests AS st ON s.id = st.student_id\n" + //
            "INNER JOIN tests AS t ON st.test_id = t.id\n" + //
            "WHERE s.id = ?1 ORDER BY st.tries", nativeQuery = true)
    List<StudentTestQueryProjection> findAllByStudentId(Integer studentId);

    @Query(value = "SELECT s.first_name AS firstName, s.last_name AS lastName, t.test_name AS testName, st.passed_flag AS passedFlag, st.tries AS tries FROM tests as t\n" + //
            "INNER JOIN students_tests AS st ON t.id = st.test_id\n" + //
            "INNER JOIN students AS s ON st.student_id = s.id\n" + //
            "WHERE t.id = ?1 ORDER BY st.tries" , nativeQuery = true)
    List<StudentTestQueryProjection> findAllByTestId(Integer testId);

    @Query(value = "SELECT st.passed_flag FROM students_tests as st\n" + //
            "INNER JOIN tests AS t ON st.test_id = t.id\n" + //
            "INNER JOIN students AS s ON st.student_id = s.id\n" + //
            "WHERE t.id = ?2 and s.id = ?1 and st.passed_flag = true", nativeQuery = true)
    Boolean hasPassed(Integer studentId, Integer testId);

    @Query(value = "SELECT s.first_name AS firstName, s.last_name AS lastName, t.test_name AS testName, st.passed_flag AS passedFlag, st.tries AS tries FROM students as s\n" + //
            "INNER JOIN students_tests AS st ON s.id = st.student_id\n" + //
            "INNER JOIN tests AS t ON st.test_id = t.id\n" + //
            "WHERE st.tries = 3 AND st.passed_flag = false", nativeQuery = true)
    List<StudentTestQueryProjection> getAllFailedStudents();

}
