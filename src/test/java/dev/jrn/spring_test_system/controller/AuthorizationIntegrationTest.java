package dev.jrn.spring_test_system.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthorizationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void requestWithoutCredentials_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/students"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void user_WhenAccessingAdminEndpoint_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/students")
                        .with(httpBasic("student1", "student-password")))
                .andExpect(status().isForbidden());
    }

    @Test
    void admin_WhenAccessingAdminEndpoint_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/v1/students")
                        .with(httpBasic("admin", "admin-password")))
                .andExpect(status().isOk());
    }

    @Test
    void user_WhenReadingOwnResults_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/v1/student-tests/by-student/1")
                        .with(httpBasic("student1", "student-password")))
                .andExpect(status().isOk());
    }

    @Test
    void user_WhenReadingOtherStudentResults_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/student-tests/by-student/2")
                        .with(httpBasic("student1", "student-password")))
                .andExpect(status().isForbidden());
    }
}
