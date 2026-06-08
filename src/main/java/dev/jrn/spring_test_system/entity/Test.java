package dev.jrn.spring_test_system.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "tests")
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "test_name", nullable = false)
    @NotBlank
    private String testName;

    @Column(name = "graded")
    private Boolean graded = false;

    @JsonIgnore
    @OneToMany(
        mappedBy = "test",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<StudentTest> students = new ArrayList<>();

    public Test() { }

    public Test(String testName) { this.testName = testName; }

    public Test(Integer id, String testName, Boolean graded) {
        this.id = id;
        this.testName = testName;
        this.graded = graded;
    }

    public Test(String testName, Boolean graded) {
        this.testName = testName;
        this.graded = graded;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public Boolean getGraded() {
        return graded;
    }

    public void setGraded(Boolean graded) {
        this.graded = graded;
    }

    @Override
    public String toString() {
        return "Test [id=" + id + ", testName=" + testName + ", graded=" + graded + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, testName, graded);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Test other))
            return false;
        return Objects.equals(id, other.id)
                && Objects.equals(testName, other.testName)
                && Objects.equals(graded, other.graded);
    }

    

    
}
