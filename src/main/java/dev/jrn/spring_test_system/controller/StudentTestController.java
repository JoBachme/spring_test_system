package dev.jrn.spring_test_system.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.jrn.spring_test_system.dto.StudentTestResponse;
import dev.jrn.spring_test_system.entity.StudentTestId;
import dev.jrn.spring_test_system.entity.StudentTestQueryProjection;
import dev.jrn.spring_test_system.service.StudentTestService;

@RestController
@RequestMapping(path = "api/v1/st_combination")
public class StudentTestController {
    
    private final StudentTestService studentTestService;

    public StudentTestController(StudentTestService studentTestService) {
        this.studentTestService = studentTestService;
    }

    @GetMapping(path = "/all")
    public List<StudentTestResponse> fetchAllStudentTestCombination() {
        return studentTestService.getAllStudentTestCombinations().stream()
                .map(StudentTestResponse::from)
                .toList();
    }

    @GetMapping(path = "/id")
    public StudentTestResponse fatchOneByStudentId(@RequestParam(required = true) Integer studentId,
            @RequestParam(required = true) Integer testId) {
        return StudentTestResponse.from(studentTestService.getStudentTestCombinationById(new StudentTestId(studentId, testId)));
    }

    @GetMapping(path = "/tests")
    public List<StudentTestQueryProjection> fatchOneByTestId(@RequestParam(required = true) Integer studentId) {
        return studentTestService.findAllByStudentId(studentId);
    }

    @GetMapping(path = "/students")
    public List<StudentTestQueryProjection> fatchOneStudentTestCombination(@RequestParam(required = true) Integer testId) {
        return studentTestService.findAllByTestId(testId);
    }
    
    @GetMapping(path = "/hasPassed")
    public Boolean hasPassed(@RequestParam Integer studentId, @RequestParam Integer testId) {
        return studentTestService.hasPassed(studentId, testId);
    }

    @PutMapping(path = "/addTry")
    public void addTry(@RequestParam Integer studentId, @RequestParam Integer testId) {
        studentTestService.addTry(studentId, testId);
    }

    @PutMapping(path = "/passing")
    public void markPassed(@RequestParam Integer studentId, @RequestParam Integer testId) {
        studentTestService.markPassed(studentId, testId);
    }

    @GetMapping(path = "/failed")
    public List<StudentTestQueryProjection> getAllFailedStudents() {
        return studentTestService.getAllFailedStudents();
    }

}
