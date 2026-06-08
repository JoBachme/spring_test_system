package dev.jrn.spring_test_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.jrn.spring_test_system.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    Optional<Student> findByFirstNameAndLastName(String firstName, String lastName);

}
