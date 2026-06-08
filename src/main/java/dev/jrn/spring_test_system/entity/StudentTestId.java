package dev.jrn.spring_test_system.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class StudentTestId implements Serializable {
    
    @Column(nullable = false, name = "student_id")
    private Integer studentId;

    @Column(nullable = false, name = "test_id")
    private Integer testId;
    
    public StudentTestId(Integer studentId, Integer testId) {
        this.studentId = studentId;
        this.testId = testId;
    }

    public StudentTestId() { }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getTestId() {
        return testId;
    }

    public void setTestId(Integer testId) {
        this.testId = testId;
    }

    @Override
    public String toString() {
        return "StudentTestId [studentId=" + studentId + ", testId=" + testId + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, testId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof StudentTestId other))
            return false;
        return Objects.equals(studentId, other.studentId)
                && Objects.equals(testId, other.testId);
    }
}
