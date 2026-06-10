package dev.jrn.spring_test_system.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import dev.jrn.spring_test_system.entity.MessageSample;
import dev.jrn.spring_test_system.entity.Student;
import dev.jrn.spring_test_system.entity.StudentTest;
import dev.jrn.spring_test_system.repository.MessageSampleRepository;
import dev.jrn.spring_test_system.repository.StudentRepository;
import dev.jrn.spring_test_system.repository.StudentTestRepository;
import dev.jrn.spring_test_system.repository.TestRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@WithMockUser(roles = "ADMIN")
class MessageSampleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MessageSampleRepository messageSampleRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private StudentTestRepository studentTestRepository;

    private Student student;
    private dev.jrn.spring_test_system.entity.Test test;

    @BeforeEach
    void setUp() {
        messageSampleRepository.save(new MessageSample(
                "notification_sample",
                "Hello $firstName $lastName, your result for $testName is $result."
        ));
        student = studentRepository.save(new Student("Ada", "Lovelace"));
        test = testRepository.save(new dev.jrn.spring_test_system.entity.Test("Algorithms", true));
    }

    @Test
    void previewTemplate_WhenRegistrationExists_ShouldRenderMessage() throws Exception {
        studentTestRepository.save(new StudentTest(student, test, true, 1));

        mockMvc.perform(get("/api/v1/message-templates/notification_sample/preview")
                        .param("studentId", student.getId().toString())
                        .param("testId", test.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is("Hello Ada Lovelace, your result for Algorithms is passed.")));
    }

    @Test
    void previewTemplate_WhenRegistrationMissing_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/message-templates/notification_sample/preview")
                        .param("studentId", student.getId().toString())
                        .param("testId", test.getId().toString()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title", is("Not Found")))
                .andExpect(jsonPath("$.detail", is("Registration does not exist")));
    }
}
