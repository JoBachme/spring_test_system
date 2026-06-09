package dev.jrn.spring_test_system.dto;

import dev.jrn.spring_test_system.entity.AttemptResult;
import jakarta.validation.constraints.NotNull;

public record TestAttemptRequest(
        @NotNull AttemptResult result
) {
}
