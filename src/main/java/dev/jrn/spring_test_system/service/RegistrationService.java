package dev.jrn.spring_test_system.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import dev.jrn.spring_test_system.entity.Student;
import dev.jrn.spring_test_system.entity.StudentTest;
import dev.jrn.spring_test_system.entity.StudentTestId;
import dev.jrn.spring_test_system.entity.Test;
import dev.jrn.spring_test_system.repository.StudentRepository;
import dev.jrn.spring_test_system.repository.StudentTestRepository;
import dev.jrn.spring_test_system.repository.TestRepository;

@Service
public class RegistrationService {

    private final StudentRepository studentRepository;
    private final TestRepository testRepository;
    private final StudentTestRepository studentTestRepository;

    public RegistrationService(StudentRepository studentRepository,
            TestRepository testRepository,
            StudentTestRepository studentTestRepository) {
        this.studentRepository = studentRepository;
        this.testRepository = testRepository;
        this.studentTestRepository = studentTestRepository;
    }

    @Transactional
    public StudentTest register(Integer studentId, Integer testId) {
        StudentTestId registrationId = new StudentTestId(studentId, testId);
        if (studentTestRepository.existsById(registrationId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Student is already registered for this test");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student does not exist"));
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Test does not exist"));

        return studentTestRepository.save(new StudentTest(student, test, false, 0));
    }

    @Transactional
    public void unregister(Integer studentId, Integer testId) {
        StudentTestId registrationId = new StudentTestId(studentId, testId);
        if (!studentTestRepository.existsById(registrationId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Registration does not exist");
        }
        studentTestRepository.deleteById(registrationId);
    }
}
