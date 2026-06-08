package dev.jrn.spring_test_system.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import dev.jrn.spring_test_system.entity.Test;
import dev.jrn.spring_test_system.repository.TestRepository;

@Service
public class TestService {
    
    private final TestRepository testRepository;

    public TestService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    public List<Test> getAllTests() { return testRepository.findAll(); }

    public Optional<Test> getTestById(Integer testId) { return testRepository.findById(testId); }

    public void deleteTestById(Integer testId) {
        if (!testRepository.existsById(testId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Test does not exist");
        }
        testRepository.deleteById(testId);
    }

    public void addNewTest(Test test) {
        Optional<Test> testOptional = testRepository.findByTestName(test.getTestName());

        if (testOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Name already taken");
        }
        testRepository.save(test);
    }

    @Transactional
    public void updateTest(Integer testId, String testName) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Test does not exist"));

        if (testName != null && !testName.isBlank()
                && !Objects.equals(test.getTestName(), testName)) { 
            Optional<Test> testOptional = testRepository.findByTestName(testName);
            if (testOptional.isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Name already taken");
            }
            test.setTestName(testName);
        }
    }

    @Transactional
    public void toggleGradedStatus(Integer testId) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Test does not exist"));
        test.setGraded(!Boolean.TRUE.equals(test.getGraded()));
    }
}
