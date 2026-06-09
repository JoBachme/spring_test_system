package dev.jrn.spring_test_system.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import dev.jrn.spring_test_system.entity.AttemptResult;
import dev.jrn.spring_test_system.entity.StudentTest;
import dev.jrn.spring_test_system.entity.StudentTestId;
import dev.jrn.spring_test_system.entity.StudentTestQueryProjection;
import dev.jrn.spring_test_system.entity.Test;
import dev.jrn.spring_test_system.entity.TestAttempt;
import dev.jrn.spring_test_system.repository.StudentTestRepository;
import dev.jrn.spring_test_system.repository.TestAttemptRepository;
import dev.jrn.spring_test_system.repository.TestRepository;

@Service
public class StudentTestService {
    
    private final StudentTestRepository studentTestRepository;
    private final TestRepository testRepository;
    private final TestAttemptRepository testAttemptRepository;

    public StudentTestService(StudentTestRepository studentTestRepository,
            TestRepository testRepository,
            TestAttemptRepository testAttemptRepository) {
        this.studentTestRepository = studentTestRepository;
        this.testRepository = testRepository;
        this.testAttemptRepository = testAttemptRepository;
    }

    public List<StudentTest> getAllStudentTestCombinations() {
        return studentTestRepository.getAllCombinations();
    }

    public List<StudentTestQueryProjection> findAllByStudentId(Integer studentId) { 
        return studentTestRepository.findAllByStudentId(studentId);
    }

    public List<StudentTestQueryProjection> findAllByTestId(Integer testId) {
        return studentTestRepository.findAllByTestId(testId);
    }

    public StudentTest getStudentTestCombinationById(StudentTestId studentTestId) {
        return studentTestRepository.findByStudentTestId(studentTestId);
    }

    public Boolean hasPassed(Integer studentId, Integer testId) {
        return studentTestRepository.hasPassed(studentId, testId) != null;
    }

    @Transactional
    public TestAttempt submitAttempt(Integer studentId, Integer testId, AttemptResult result) {
        StudentTest st = getRegistration(studentId, testId);
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Test does not exist"));

        if (!Boolean.TRUE.equals(test.getGraded())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Test is not marked as graded");
        }
        if (Boolean.TRUE.equals(st.getPassedFlag())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student has already passed this test");
        }
        if (st.getTries() >= 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student already used all three attempts");
        }

        Integer nextAttemptNumber = st.getTries() + 1;
        TestAttempt attempt = testAttemptRepository.save(
                new TestAttempt(st.getStudent(), test, nextAttemptNumber, result));

        st.setTries(nextAttemptNumber);
        if (result == AttemptResult.PASSED) {
            st.setPassedFlag(true);
        }

        return attempt;
    }

    @Transactional(readOnly = true)
    public List<TestAttempt> getAttemptHistory(Integer studentId, Integer testId) {
        getRegistration(studentId, testId);
        return testAttemptRepository.findByStudentIdAndTestIdOrderByAttemptNumberAsc(studentId, testId);
    }

    public List<StudentTestQueryProjection> getAllFailedStudents() {
        return studentTestRepository.getAllFailedStudents();
    }

    private StudentTest getRegistration(Integer studentId, Integer testId) {
        StudentTest studentTest = studentTestRepository.getCombination(studentId, testId);
        if (studentTest == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student is not registered for this test");
        }
        return studentTest;
    }
}
