package dev.jrn.spring_test_system.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jrn.spring_test_system.dto.StudentTestResponse;
import dev.jrn.spring_test_system.entity.StudentTestId;
import dev.jrn.spring_test_system.entity.StudentTestQueryProjection;
import dev.jrn.spring_test_system.service.StudentTestService;

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

    @PutMapping(path = "/{studentId}/{testId}/attempts")
    public void addTry(@PathVariable Integer studentId, @PathVariable Integer testId) {
        studentTestService.addTry(studentId, testId);
    }

    @PutMapping(path = "/{studentId}/{testId}/passing-status")
    public void markPassed(@PathVariable Integer studentId, @PathVariable Integer testId) {
        studentTestService.markPassed(studentId, testId);
    }

    @GetMapping(path = "/failed")
    public List<StudentTestQueryProjection> getAllFailedStudents() {
        return studentTestService.getAllFailedStudents();
    }

}
