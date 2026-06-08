package dev.jrn.spring_test_system.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.jrn.spring_test_system.service.TestService;

@WebMvcTest(TestController.class)
class TestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TestService testService;

    @Test
    void fetchAllTests_ShouldApplyFilterParameters() throws Exception {
        List<dev.jrn.spring_test_system.entity.Test> tests = List.of(
                new dev.jrn.spring_test_system.entity.Test(1, "Algorithms", true)
        );
        when(testService.searchTests(org.mockito.ArgumentMatchers.eq("algo"),
                org.mockito.ArgumentMatchers.eq(true),
                any(Pageable.class))).thenReturn(new PageImpl<>(tests));

        mockMvc.perform(get("/api/v1/test/all")
                        .param("testName", "algo")
                        .param("graded", "true")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "testName,asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].testName", is("Algorithms")))
                .andExpect(jsonPath("$.content[0].graded", is(true)));
    }
}
