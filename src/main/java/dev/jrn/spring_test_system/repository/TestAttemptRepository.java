package dev.jrn.spring_test_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.jrn.spring_test_system.entity.TestAttempt;

@Repository
public interface TestAttemptRepository extends JpaRepository<TestAttempt, Integer> {

    @EntityGraph(attributePaths = {"student", "test"})
    List<TestAttempt> findByStudentIdAndTestIdOrderByAttemptNumberAsc(Integer studentId, Integer testId);
}
