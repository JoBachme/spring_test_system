package dev.jrn.spring_test_system.controller;

import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.jrn.spring_test_system.dto.StudentRequest;
import dev.jrn.spring_test_system.entity.Student;
import dev.jrn.spring_test_system.service.StudentService;


@WebMvcTest(StudentController.class)
@WithMockUser(roles = "ADMIN")
public class StudentControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StudentService studentService;

    @Test
    public void fetchAllStudents_ShouldReturnAllStudents() throws Exception {
        List<Student> students = Arrays.asList(
            new Student(1, "John", "Doe"),
            new Student(2, "Alice", "Smith")
        );
        when(studentService.searchStudents(isNull(), isNull(), any(Pageable.class))).thenReturn(new PageImpl<>(students));

        mockMvc.perform(get("/api/v1/students"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.content", hasSize(2)))
               .andExpect(jsonPath("$.content[0].firstName", is("John")))
               .andExpect(jsonPath("$.content[1].lastName", is("Smith")))
               .andExpect(jsonPath("$.totalElements", is(2)));
    }

    @Test
    public void addNewStudent_WhenRequestInvalid_ShouldReturnValidationProblem() throws Exception {
        StudentRequest request = new StudentRequest("", "");

        mockMvc.perform(post("/api/v1/students")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title", is("Validation failed")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("Request validation failed")))
                .andExpect(jsonPath("$.path", is("/api/v1/students")))
                .andExpect(jsonPath("$.errors", hasSize(2)));
    }
}
