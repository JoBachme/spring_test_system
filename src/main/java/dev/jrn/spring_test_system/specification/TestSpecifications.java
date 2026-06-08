package dev.jrn.spring_test_system.specification;

import org.springframework.data.jpa.domain.Specification;

import dev.jrn.spring_test_system.entity.Test;

public final class TestSpecifications {

    private TestSpecifications() { }

    public static Specification<Test> testNameContains(String testName) {
        return (root, query, criteriaBuilder) -> testName == null || testName.isBlank()
                ? criteriaBuilder.conjunction()
                : criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("testName")),
                        "%" + testName.toLowerCase() + "%"
                );
    }

    public static Specification<Test> gradedEquals(Boolean graded) {
        return (root, query, criteriaBuilder) -> graded == null
                ? criteriaBuilder.conjunction()
                : criteriaBuilder.equal(root.get("graded"), graded);
    }
}
