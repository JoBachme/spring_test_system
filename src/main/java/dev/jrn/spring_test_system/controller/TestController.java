package dev.jrn.spring_test_system.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import dev.jrn.spring_test_system.dto.TestRequest;
import dev.jrn.spring_test_system.dto.TestResponse;
import dev.jrn.spring_test_system.dto.PageResponse;
import dev.jrn.spring_test_system.service.TestService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/tests")
public class TestController {
    
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping
    public PageResponse<TestResponse> fetchAllTests(
            @RequestParam(required = false) String testName,
            @RequestParam(required = false) Boolean graded,
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return PageResponse.from(testService.searchTests(testName, graded, pageable)
                .map(TestResponse::from));
    }

    @GetMapping(path = "/list")
    public List<TestResponse> fetchAllTestsAsList() {
        return testService.getAllTests().stream()
                .map(TestResponse::from)
                .toList();
    }

    @GetMapping(path = "/{testId}")
    public TestResponse getTestById(@PathVariable("testId") Integer testId) {
        return testService.getTestById(testId)
                .map(TestResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Test does not exist"));
    }

    @DeleteMapping(path = "/{testId}")
    public void deleteTestById(@PathVariable("testId") Integer testId) {
        testService.deleteTestById(testId);
    }

    @PostMapping
    public void addNewTest(@Valid @RequestBody TestRequest test) { testService.addNewTest(test.toEntity()); }


    @PutMapping(path = "/{testId}")
    public void updateTest(@PathVariable("testId") Integer testId, @RequestParam(required = false) String testName) {
        testService.updateTest(testId, testName);
    }

    @PutMapping(path = "/{testId}/graded-status")
    public void toggleGradedStatus(@PathVariable("testId") Integer testId) {
        testService.toggleGradedStatus(testId);
    }

}
