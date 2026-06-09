package dev.jrn.spring_test_system.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.jrn.spring_test_system.dto.RegistrationRequest;
import dev.jrn.spring_test_system.dto.StudentTestResponse;
import dev.jrn.spring_test_system.service.RegistrationService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/registrations")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public StudentTestResponse register(@Valid @RequestBody RegistrationRequest request) {
        return StudentTestResponse.from(registrationService.register(request.studentId(), request.testId()));
    }

    @DeleteMapping(path = "/{studentId}/{testId}")
    public void unregister(@PathVariable Integer studentId, @PathVariable Integer testId) {
        registrationService.unregister(studentId, testId);
    }

    @Deprecated(since = "0.0.1", forRemoval = true)
    @DeleteMapping
    public void unregisterQuery(@RequestParam Integer studentId, @RequestParam Integer testId) {
        unregister(studentId, testId);
    }
}
