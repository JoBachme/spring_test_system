package dev.jrn.spring_test_system.dto;

import dev.jrn.spring_test_system.entity.Student;
import jakarta.validation.constraints.NotBlank;

public record StudentRequest(
        @NotBlank String firstName,
        @NotBlank String lastName
) {
    public Student toEntity() {
        return new Student(firstName, lastName);
    }
}
