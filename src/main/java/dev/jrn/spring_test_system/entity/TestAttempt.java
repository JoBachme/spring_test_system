package dev.jrn.spring_test_system.entity;

import java.time.Instant;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "test_attempts",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_test_attempts_student_test_attempt",
                columnNames = {"student_id", "test_id", "attempt_number"}
        )
)
public class TestAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attempt_id")
    private Integer attemptId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;

    @Column(name = "attempt_number", nullable = false)
    private Integer attemptNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "result", nullable = false, length = 20)
    private AttemptResult result;

    @Column(name = "submitted_at", nullable = false)
    private Instant submittedAt;

    public TestAttempt() {
    }

    public TestAttempt(Student student, Test test, Integer attemptNumber, AttemptResult result) {
        this.student = student;
        this.test = test;
        this.attemptNumber = attemptNumber;
        this.result = result;
    }

    @PrePersist
    void setSubmittedAtIfMissing() {
        if (submittedAt == null) {
            submittedAt = Instant.now();
        }
    }

    public Integer getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(Integer attemptId) {
        this.attemptId = attemptId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public Integer getAttemptNumber() {
        return attemptNumber;
    }

    public void setAttemptNumber(Integer attemptNumber) {
        this.attemptNumber = attemptNumber;
    }

    public AttemptResult getResult() {
        return result;
    }

    public void setResult(AttemptResult result) {
        this.result = result;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
    }

    @Override
    public int hashCode() {
        return Objects.hash(attemptId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof TestAttempt other))
            return false;
        return Objects.equals(attemptId, other.attemptId);
    }
}
