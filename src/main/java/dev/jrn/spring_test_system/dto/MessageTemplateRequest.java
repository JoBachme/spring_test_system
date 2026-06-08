package dev.jrn.spring_test_system.dto;

import jakarta.validation.constraints.NotBlank;

public record MessageTemplateRequest(
        @NotBlank String text
) { }
