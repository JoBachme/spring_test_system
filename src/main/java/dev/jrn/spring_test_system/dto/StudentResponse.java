package dev.jrn.spring_test_system.dto;

import dev.jrn.spring_test_system.entity.Student;

public record StudentResponse(
        Integer id,
        String firstName,
        String lastName
) {
    public static StudentResponse from(Student student) {
        return new StudentResponse(student.getId(), student.getFirstName(), student.getLastName());
    }
}
