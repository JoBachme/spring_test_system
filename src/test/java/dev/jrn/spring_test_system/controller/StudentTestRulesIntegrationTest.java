package dev.jrn.spring_test_system.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import dev.jrn.spring_test_system.entity.Student;
import dev.jrn.spring_test_system.entity.StudentTest;
import dev.jrn.spring_test_system.repository.StudentRepository;
import dev.jrn.spring_test_system.repository.StudentTestRepository;
import dev.jrn.spring_test_system.repository.TestRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class StudentTestRulesIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private StudentTestRepository studentTestRepository;

    private Student student;
    private dev.jrn.spring_test_system.entity.Test gradedTest;
    private dev.jrn.spring_test_system.entity.Test ungradedTest;

    @BeforeEach
    void setUp() {
        student = studentRepository.save(new Student("Grace", "Hopper"));
        gradedTest = testRepository.save(new dev.jrn.spring_test_system.entity.Test("Algorithms", true));
        ungradedTest = testRepository.save(new dev.jrn.spring_test_system.entity.Test("Databases", false));
    }

    @Test
    void addAttempt_WhenTestIsGraded_ShouldIncrementTries() throws Exception {
        studentTestRepository.save(new StudentTest(student, gradedTest, false, 0));

        mockMvc.perform(put("/api/v1/student-tests/{studentId}/{testId}/attempts", student.getId(), gradedTest.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/student-tests/{studentId}/{testId}", student.getId(), gradedTest.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tries", is(1)))
                .andExpect(jsonPath("$.passedFlag", is(false)));
    }

    @Test
    void addAttempt_WhenTestIsNotGraded_ShouldReturnBadRequest() throws Exception {
        studentTestRepository.save(new StudentTest(student, ungradedTest, false, 0));

        mockMvc.perform(put("/api/v1/student-tests/{studentId}/{testId}/attempts", student.getId(), ungradedTest.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.detail", is("Test is not marked as graded")));

        mockMvc.perform(get("/api/v1/student-tests/{studentId}/{testId}", student.getId(), ungradedTest.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tries", is(0)));
    }

    @Test
    void addAttempt_WhenAllAttemptsAreUsed_ShouldReturnBadRequest() throws Exception {
        studentTestRepository.save(new StudentTest(student, gradedTest, false, 3));

        mockMvc.perform(put("/api/v1/student-tests/{studentId}/{testId}/attempts", student.getId(), gradedTest.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.detail", is("Student already used all three attempts")));
    }

    @Test
    void markPassed_WhenStudentHasNoAttempt_ShouldReturnBadRequest() throws Exception {
        studentTestRepository.save(new StudentTest(student, gradedTest, false, 0));

        mockMvc.perform(put("/api/v1/student-tests/{studentId}/{testId}/passing-status", student.getId(), gradedTest.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.detail", is("Student can only pass after a valid graded attempt")));
    }

    @Test
    void markPassed_WhenStudentHasAttempt_ShouldSetPassedFlag() throws Exception {
        studentTestRepository.save(new StudentTest(student, gradedTest, false, 1));

        mockMvc.perform(put("/api/v1/student-tests/{studentId}/{testId}/passing-status", student.getId(), gradedTest.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/student-tests/{studentId}/{testId}", student.getId(), gradedTest.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.passedFlag", is(true)))
                .andExpect(jsonPath("$.tries", is(1)));

        mockMvc.perform(get("/api/v1/student-tests/{studentId}/{testId}/passed", student.getId(), gradedTest.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));
    }
}
