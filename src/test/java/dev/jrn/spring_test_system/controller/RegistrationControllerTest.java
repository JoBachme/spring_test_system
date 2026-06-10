package dev.jrn.spring_test_system.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.jrn.spring_test_system.dto.RegistrationRequest;
import dev.jrn.spring_test_system.entity.Student;
import dev.jrn.spring_test_system.entity.StudentTest;
import dev.jrn.spring_test_system.repository.StudentRepository;
import dev.jrn.spring_test_system.repository.StudentTestRepository;
import dev.jrn.spring_test_system.repository.TestRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@WithMockUser(roles = "ADMIN")
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
        student = studentRepository.save(new Student("Ada", "Lovelace"));
        test = testRepository.save(new dev.jrn.spring_test_system.entity.Test("Algorithms", true));
    }

    @Test
    void register_ShouldCreateRegistration() throws Exception {
        RegistrationRequest request = new RegistrationRequest(student.getId(), test.getId());

        mockMvc.perform(post("/api/v1/registrations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(student.getId()))
                .andExpect(jsonPath("$.testId").value(test.getId()))
                .andExpect(jsonPath("$.passedFlag").value(false))
                .andExpect(jsonPath("$.tries").value(0));
    }

    @Test
    void register_WhenRegistrationAlreadyExists_ShouldReturnConflict() throws Exception {
        studentTestRepository.save(new StudentTest(student, test, false, 0));
        RegistrationRequest request = new RegistrationRequest(student.getId(), test.getId());

        mockMvc.perform(post("/api/v1/registrations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void unregister_ShouldDeleteRegistration() throws Exception {
        studentTestRepository.save(new StudentTest(student, test, false, 0));

        mockMvc.perform(delete("/api/v1/registrations/{studentId}/{testId}", student.getId(), test.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/v1/registrations/{studentId}/{testId}", student.getId(), test.getId()))
                .andExpect(status().isNotFound());
    }
}
