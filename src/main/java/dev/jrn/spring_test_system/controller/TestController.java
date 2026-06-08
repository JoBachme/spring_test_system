package dev.jrn.spring_test_system.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.jrn.spring_test_system.entity.Test;
import dev.jrn.spring_test_system.service.TestService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "api/v1/test")
public class TestController {
    
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping(path = "/all")
    public List<Test> fetchAllTests() { return testService.getAllTests(); }

    @GetMapping(path = "/id")
    public Optional<Test> getTestById(@RequestParam Integer testId) {
        return testService.getTestById(testId);
    }

    @DeleteMapping(path = "{testId}")
    public void deleteTestById(@PathVariable("testId") Integer testId) {
        testService.deleteTestById(testId);
    }

    @PostMapping
    public void addNewTest(@Valid @RequestBody Test test) { testService.addNewTest(test); }


    @PutMapping(path = "{testId}")
    public void updateTest(@PathVariable("testId") Integer testId, @RequestParam(required = false) String testName) {
        testService.updateTest(testId, testName);
    }

    @PutMapping(path = "/graded-status")
    public void toggleGradedStatus(@RequestParam Integer testId) {
        testService.toggleGradedStatus(testId);
    }
}
