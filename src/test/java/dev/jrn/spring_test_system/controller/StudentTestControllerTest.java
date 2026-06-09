package dev.jrn.spring_test_system.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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
}
