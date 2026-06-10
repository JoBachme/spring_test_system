package dev.jrn.spring_test_system.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import dev.jrn.spring_test_system.dto.StudentRequest;
import dev.jrn.spring_test_system.dto.StudentResponse;
import dev.jrn.spring_test_system.dto.PageResponse;
import dev.jrn.spring_test_system.service.StudentService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<StudentResponse> fetchAllStudents(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return PageResponse.from(studentService.searchStudents(firstName, lastName, pageable)
                .map(StudentResponse::from));
    }

    @GetMapping(path = "/list")
    @PreAuthorize("hasRole('ADMIN')")
    public List<StudentResponse> fetchAllStudentsAsList() {
        return studentService.getAllStudents().stream()
                .map(StudentResponse::from)
                .toList();
    }

    @GetMapping(path = "/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public StudentResponse getStudentById(@PathVariable("studentId") Integer studentId) {
        return studentService.getStudentById(studentId)
                .map(StudentResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student does not exist"));
    }

    @DeleteMapping(path = "/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteStudentById(@PathVariable("studentId") Integer studentId) {
        studentService.deleteStudentById(studentId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void addNewStudent(@Valid @RequestBody StudentRequest student) {
        studentService.addNewStudent(student.toEntity());
    }

    @PutMapping(path = "/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateStudent(@PathVariable("studentId") Integer studentId,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName) {
        studentService.updateStudent(studentId, firstName, lastName);
    }
    
}
