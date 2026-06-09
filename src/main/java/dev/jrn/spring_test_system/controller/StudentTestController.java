package dev.jrn.spring_test_system.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jrn.spring_test_system.dto.StudentTestResponse;
import dev.jrn.spring_test_system.dto.TestAttemptRequest;
import dev.jrn.spring_test_system.dto.TestAttemptResponse;
import dev.jrn.spring_test_system.entity.StudentTestId;
import dev.jrn.spring_test_system.entity.StudentTestQueryProjection;
import dev.jrn.spring_test_system.service.StudentTestService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/student-tests")
public class StudentTestController {
    
    private final StudentTestService studentTestService;

    public StudentTestController(StudentTestService studentTestService) {
        this.studentTestService = studentTestService;
    }

    @GetMapping
    public List<StudentTestResponse> fetchAllStudentTestCombinations() {
        return studentTestService.getAllStudentTestCombinations().stream()
                .map(StudentTestResponse::from)
                .toList();
    }

    @GetMapping(path = "/{studentId}/{testId}")
    public StudentTestResponse fetchOneByStudentId(@PathVariable Integer studentId, @PathVariable Integer testId) {
        return StudentTestResponse.from(studentTestService.getStudentTestCombinationById(new StudentTestId(studentId, testId)));
    }

    @GetMapping(path = "/by-student/{studentId}")
    public List<StudentTestQueryProjection> fetchAllByStudentId(@PathVariable Integer studentId) {
        return studentTestService.findAllByStudentId(studentId);
    }

    @GetMapping(path = "/by-test/{testId}")
    public List<StudentTestQueryProjection> fetchAllByTestId(@PathVariable Integer testId) {
        return studentTestService.findAllByTestId(testId);
    }
    
    @GetMapping(path = "/{studentId}/{testId}/passed")
    public Boolean hasPassed(@PathVariable Integer studentId, @PathVariable Integer testId) {
        return studentTestService.hasPassed(studentId, testId);
    }

    @PostMapping(path = "/{studentId}/{testId}/attempts")
    public TestAttemptResponse submitAttempt(@PathVariable Integer studentId,
            @PathVariable Integer testId,
            @Valid @RequestBody TestAttemptRequest request) {
        return TestAttemptResponse.from(studentTestService.submitAttempt(studentId, testId, request.result()));
    }

    @GetMapping(path = "/{studentId}/{testId}/attempts")
    public List<TestAttemptResponse> getAttemptHistory(@PathVariable Integer studentId,
            @PathVariable Integer testId) {
        return studentTestService.getAttemptHistory(studentId, testId).stream()
                .map(TestAttemptResponse::from)
                .toList();
    }

    @GetMapping(path = "/failed")
    public List<StudentTestQueryProjection> getAllFailedStudents() {
        return studentTestService.getAllFailedStudents();
    }

}
