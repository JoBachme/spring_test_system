package dev.jrn.spring_test_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.jrn.spring_test_system.entity.Test;

@Repository
public interface TestRepository extends JpaRepository<Test, Integer> {

    Optional<Test> findByTestName(String testName);
}
