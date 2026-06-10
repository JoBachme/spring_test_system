package dev.jrn.spring_test_system.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    @PreAuthorize("hasRole('ADMIN')")
    public StudentTestResponse register(@Valid @RequestBody RegistrationRequest request) {
        return StudentTestResponse.from(registrationService.register(request.studentId(), request.testId()));
    }

    @DeleteMapping(path = "/{studentId}/{testId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void unregister(@PathVariable Integer studentId, @PathVariable Integer testId) {
        registrationService.unregister(studentId, testId);
    }
}
