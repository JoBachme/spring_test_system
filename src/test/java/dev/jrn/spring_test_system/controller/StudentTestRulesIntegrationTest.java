package dev.jrn.spring_test_system.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
    void submitFailedAttempt_WhenTestIsGraded_ShouldIncrementTriesAndStoreHistory() throws Exception {
        studentTestRepository.save(new StudentTest(student, gradedTest, false, 0));

        mockMvc.perform(post("/api/v1/student-tests/{studentId}/{testId}/attempts", student.getId(), gradedTest.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"result":"FAILED"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId", is(student.getId())))
                .andExpect(jsonPath("$.testId", is(gradedTest.getId())))
                .andExpect(jsonPath("$.attemptNumber", is(1)))
                .andExpect(jsonPath("$.result", is("FAILED")));

        mockMvc.perform(get("/api/v1/student-tests/{studentId}/{testId}", student.getId(), gradedTest.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tries", is(1)))
                .andExpect(jsonPath("$.passedFlag", is(false)));

        mockMvc.perform(get("/api/v1/student-tests/{studentId}/{testId}/attempts", student.getId(), gradedTest.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].attemptNumber", is(1)))
                .andExpect(jsonPath("$[0].result", is("FAILED")));
    }

    @Test
    void submitAttempt_WhenTestIsNotGraded_ShouldReturnBadRequest() throws Exception {
        studentTestRepository.save(new StudentTest(student, ungradedTest, false, 0));

        mockMvc.perform(post("/api/v1/student-tests/{studentId}/{testId}/attempts", student.getId(), ungradedTest.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"result":"FAILED"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.detail", is("Test is not marked as graded")));

        mockMvc.perform(get("/api/v1/student-tests/{studentId}/{testId}", student.getId(), ungradedTest.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tries", is(0)));
    }

    @Test
    void submitAttempt_WhenAllAttemptsAreUsed_ShouldReturnBadRequest() throws Exception {
        studentTestRepository.save(new StudentTest(student, gradedTest, false, 3));

        mockMvc.perform(post("/api/v1/student-tests/{studentId}/{testId}/attempts", student.getId(), gradedTest.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"result":"FAILED"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.detail", is("Student already used all three attempts")));
    }

    @Test
    void submitAttempt_WhenStudentAlreadyPassed_ShouldReturnBadRequest() throws Exception {
        studentTestRepository.save(new StudentTest(student, gradedTest, true, 1));

        mockMvc.perform(post("/api/v1/student-tests/{studentId}/{testId}/attempts", student.getId(), gradedTest.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"result":"FAILED"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Bad Request")))
                .andExpect(jsonPath("$.detail", is("Student has already passed this test")));
    }

    @Test
    void submitPassedAttempt_ShouldSetPassedFlagAndStoreHistory() throws Exception {
        studentTestRepository.save(new StudentTest(student, gradedTest, false, 0));

        mockMvc.perform(post("/api/v1/student-tests/{studentId}/{testId}/attempts", student.getId(), gradedTest.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"result":"PASSED"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.attemptNumber", is(1)))
                .andExpect(jsonPath("$.result", is("PASSED")));

        mockMvc.perform(get("/api/v1/student-tests/{studentId}/{testId}", student.getId(), gradedTest.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.passedFlag", is(true)))
                .andExpect(jsonPath("$.tries", is(1)));

        mockMvc.perform(get("/api/v1/student-tests/{studentId}/{testId}/attempts", student.getId(), gradedTest.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].result", is("PASSED")));

        mockMvc.perform(get("/api/v1/student-tests/{studentId}/{testId}/passed", student.getId(), gradedTest.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));
    }
}
