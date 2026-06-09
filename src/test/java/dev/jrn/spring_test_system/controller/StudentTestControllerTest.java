package dev.jrn.spring_test_system.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import dev.jrn.spring_test_system.entity.StudentTest;
import dev.jrn.spring_test_system.entity.StudentTestId;
import dev.jrn.spring_test_system.service.StudentTestService;

@WebMvcTest(StudentTestController.class)
class StudentTestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentTestService studentTestService;

    @Test
    void getStudentTestById_ShouldUseResourcePath() throws Exception {
        StudentTestId id = new StudentTestId(1, 2);
        when(studentTestService.getStudentTestCombinationById(id)).thenReturn(new StudentTest(id, true, 2));

        mockMvc.perform(get("/api/v1/student-tests/1/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId", is(1)))
                .andExpect(jsonPath("$.testId", is(2)))
                .andExpect(jsonPath("$.passedFlag", is(true)))
                .andExpect(jsonPath("$.tries", is(2)));
    }

    @Test
    void getStudentTestById_WhenMissing_ShouldReturnProblemDetail() throws Exception {
        StudentTestId id = new StudentTestId(1, 2);
        when(studentTestService.getStudentTestCombinationById(id))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Student is not registered for this test"));

        mockMvc.perform(get("/api/v1/student-tests/1/2"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title", is("Not Found")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.detail", is("Student is not registered for this test")))
                .andExpect(jsonPath("$.path", is("/api/v1/student-tests/1/2")));
    }
}
