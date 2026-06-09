package dev.jrn.spring_test_system.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.jrn.spring_test_system.dto.StudentTestResponse;
import dev.jrn.spring_test_system.entity.StudentTestId;
import dev.jrn.spring_test_system.entity.StudentTestQueryProjection;
import dev.jrn.spring_test_system.service.StudentTestService;

@RestController
@RequestMapping(path = {"/api/v1/student-tests", "/api/v1/st_combination"})
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

    @Deprecated(since = "0.0.1", forRemoval = true)
    @GetMapping(path = "/all")
    public List<StudentTestResponse> fetchAllStudentTestCombinationsLegacy() {
        return fetchAllStudentTestCombinations();
    }

    @Deprecated(since = "0.0.1", forRemoval = true)
    @GetMapping(path = "/id")
    public StudentTestResponse fetchOneByStudentIdQuery(@RequestParam Integer studentId, @RequestParam Integer testId) {
        return fetchOneByStudentId(studentId, testId);
    }

    @Deprecated(since = "0.0.1", forRemoval = true)
    @GetMapping(path = "/tests")
    public List<StudentTestQueryProjection> fetchAllByStudentIdQuery(@RequestParam Integer studentId) {
        return fetchAllByStudentId(studentId);
    }

    @Deprecated(since = "0.0.1", forRemoval = true)
    @GetMapping(path = "/students")
    public List<StudentTestQueryProjection> fetchAllByTestIdQuery(@RequestParam Integer testId) {
        return fetchAllByTestId(testId);
    }

    @Deprecated(since = "0.0.1", forRemoval = true)
    @GetMapping(path = "/hasPassed")
    public Boolean hasPassedQuery(@RequestParam Integer studentId, @RequestParam Integer testId) {
        return hasPassed(studentId, testId);
    }

    @Deprecated(since = "0.0.1", forRemoval = true)
    @PutMapping(path = "/addTry")
    public void addTryQuery(@RequestParam Integer studentId, @RequestParam Integer testId) {
        addTry(studentId, testId);
    }

    @Deprecated(since = "0.0.1", forRemoval = true)
    @PutMapping(path = "/passing")
    public void markPassedQuery(@RequestParam Integer studentId, @RequestParam Integer testId) {
        markPassed(studentId, testId);
    }

}
