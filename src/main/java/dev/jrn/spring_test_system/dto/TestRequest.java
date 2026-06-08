package dev.jrn.spring_test_system.dto;

import dev.jrn.spring_test_system.entity.Test;
import jakarta.validation.constraints.NotBlank;

public record TestRequest(
        @NotBlank String testName,
        Boolean graded
) {
    public Test toEntity() {
        return new Test(testName, Boolean.TRUE.equals(graded));
    }
}
