package dev.jrn.spring_test_system.specification;

import org.springframework.data.jpa.domain.Specification;

import dev.jrn.spring_test_system.entity.Student;

public final class StudentSpecifications {

    private StudentSpecifications() { }

    public static Specification<Student> firstNameContains(String firstName) {
        return (root, query, criteriaBuilder) -> firstName == null || firstName.isBlank()
                ? criteriaBuilder.conjunction()
                : criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("firstName")),
                        "%" + firstName.toLowerCase() + "%"
                );
    }

    public static Specification<Student> lastNameContains(String lastName) {
        return (root, query, criteriaBuilder) -> lastName == null || lastName.isBlank()
                ? criteriaBuilder.conjunction()
                : criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("lastName")),
                        "%" + lastName.toLowerCase() + "%"
                );
    }
}
