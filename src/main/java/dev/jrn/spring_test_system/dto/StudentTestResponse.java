package dev.jrn.spring_test_system.dto;

import dev.jrn.spring_test_system.entity.StudentTest;

public record StudentTestResponse(
        Integer studentId,
        Integer testId,
        Boolean passedFlag,
        Integer tries
) {
    public static StudentTestResponse from(StudentTest studentTest) {
        return new StudentTestResponse(
                studentTest.getStudentTestId().getStudentId(),
                studentTest.getStudentTestId().getTestId(),
                studentTest.getPassedFlag(),
                studentTest.getTries()
        );
    }
}
