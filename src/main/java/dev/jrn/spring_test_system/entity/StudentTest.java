package dev.jrn.spring_test_system.entity;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "students_tests")
public class StudentTest{

    @EmbeddedId
    private StudentTestId studentTestId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("studentId")
    private Student student;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("testId")
    private Test test;
    
    @Column(name = "passed_flag")
    private Boolean passedFlag = false;

    private Integer tries = 0;



    public StudentTest() { }

    public StudentTest(StudentTestId studentTestId, Boolean passedFlag, Integer tries) {
        this.studentTestId = studentTestId;
        this.passedFlag = passedFlag;
        this.tries = tries;
    }

    public StudentTest(StudentTestId studentTestId) {
        this.studentTestId = studentTestId;
    }

    public StudentTest(Student student, Test test, Boolean passedFlag, Integer tries) {
        this.student = student;
        this.test = test;
        this.passedFlag = passedFlag;
        this.tries = tries;
        this.studentTestId = new StudentTestId(student.getId(), test.getId());
    }



    public Boolean getPassedFlag() {
        return passedFlag;
    }

    public void setPassedFlag(Boolean passedFlag) {
        this.passedFlag = passedFlag;
    }

    public Integer getTries() {
        return tries;
    }

    public void setTries(Integer tries) {
        this.tries = tries;
    }

    public StudentTestId getStudentTestId() {
        return studentTestId;
    }


    public void setStudentTestId(StudentTestId studentTestId) {
        this.studentTestId = studentTestId;
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

    @Override
    public String toString() {
        return "StudentTest [studentTestId=" + studentTestId + ", student=" + student + ", test=" + test
                + ", passedFlag=" + passedFlag + ", tries=" + tries + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentTestId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof StudentTest other))
            return false;
        return Objects.equals(studentTestId, other.studentTestId);
    }

    
    
    

}
