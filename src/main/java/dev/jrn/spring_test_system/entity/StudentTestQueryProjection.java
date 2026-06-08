package dev.jrn.spring_test_system.entity;

public interface StudentTestQueryProjection {

    String getFirstName();

    String getLastName();

    String getTestName();

    Boolean getPassedFlag();

    Integer getTries();
}
