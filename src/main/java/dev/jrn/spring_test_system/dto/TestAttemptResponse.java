package dev.jrn.spring_test_system.dto;

import java.time.Instant;

import dev.jrn.spring_test_system.entity.AttemptResult;
import dev.jrn.spring_test_system.entity.TestAttempt;

public record TestAttemptResponse(
        Integer attemptId,
        Integer studentId,
        Integer testId,
        Integer attemptNumber,
        AttemptResult result,
        Instant submittedAt
) {
    public static TestAttemptResponse from(TestAttempt attempt) {
        return new TestAttemptResponse(
                attempt.getAttemptId(),
                attempt.getStudent().getId(),
                attempt.getTest().getId(),
                attempt.getAttemptNumber(),
                attempt.getResult(),
                attempt.getSubmittedAt()
        );
    }
}
