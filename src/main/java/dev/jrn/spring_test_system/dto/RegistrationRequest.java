package dev.jrn.spring_test_system.dto;

import jakarta.validation.constraints.NotNull;

public record RegistrationRequest(
        @NotNull Integer studentId,
        @NotNull Integer testId
) { }
