package dev.jrn.spring_test_system.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
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
import dev.jrn.spring_test_system.service.StudentService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "api/v1/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping(path = "/all")
    public List<StudentResponse> fetchAllStudents() {
        return studentService.getAllStudents().stream()
                .map(StudentResponse::from)
                .toList();
    }

    @GetMapping(path = "/id")
    public StudentResponse getStudentById(@RequestParam Integer studentId) {
        return studentService.getStudentById(studentId)
                .map(StudentResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student does not exist"));
    }

    @DeleteMapping(path = "{studentId}")
    public void deleteStudentById(@PathVariable("studentId") Integer studentId) {
        studentService.deleteStudentById(studentId);
    }

    @PostMapping
    public void addNewStudent(@Valid @RequestBody StudentRequest student) {
        studentService.addNewStudent(student.toEntity());
    }

    @PutMapping(path = "{studentId}")
    public void updateStudent(@PathVariable("studentId") Integer studentId,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName) {
        studentService.updateStudent(studentId, firstName, lastName);
    }
    
}
