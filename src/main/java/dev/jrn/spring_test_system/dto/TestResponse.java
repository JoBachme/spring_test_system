package dev.jrn.spring_test_system.dto;

import dev.jrn.spring_test_system.entity.Test;

public record TestResponse(
        Integer id,
        String testName,
        Boolean graded
) {
    public static TestResponse from(Test test) {
        return new TestResponse(test.getId(), test.getTestName(), test.getGraded());
    }
}
