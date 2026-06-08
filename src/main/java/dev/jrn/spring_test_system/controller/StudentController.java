package dev.jrn.spring_test_system.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.jrn.spring_test_system.entity.Student;
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
    public List<Student> fetchAllStudents() { return studentService.getAllStudents(); }

    @GetMapping(path = "/id")
    public Optional<Student> getStudentById(@RequestParam Integer studentId) {
        return studentService.getStudentById(studentId);
    }

    @DeleteMapping(path = "{studentId}")
    public void deleteStudentById(@PathVariable("studentId") Integer studentId) {
        studentService.deleteStudentById(studentId);
    }

    @PostMapping
    public void addNewStudent(@Valid @RequestBody Student student) {
        studentService.addNewStudent(student);
    }

    @PutMapping(path = "{studentId}")
    public void updateStudent(@PathVariable("studentId") Integer studentId,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName) {
        studentService.updateStudent(studentId, firstName, lastName);
    }
    
}
