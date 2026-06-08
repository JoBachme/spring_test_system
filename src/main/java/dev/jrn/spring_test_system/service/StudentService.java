package dev.jrn.spring_test_system.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import dev.jrn.spring_test_system.entity.Student;
import dev.jrn.spring_test_system.repository.StudentRepository;

@Service
public class StudentService {
    
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() { return studentRepository.findAll(); }

    public Optional<Student> getStudentById(Integer studentId) { return studentRepository.findById(studentId); }

    public void deleteStudentById(Integer studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student does not exist");
        }
        studentRepository.deleteById(studentId);
    }

    public void addNewStudent(Student student) {
        Optional<Student> studentOptional = studentRepository.findByFirstNameAndLastName(
                student.getFirstName(),
                student.getLastName()
        );

        if (studentOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Name already taken");
        }
        studentRepository.save(student);
    }

    @Transactional
    public void updateStudent(Integer studentId, String firstName, String lastName) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student does not exist"));

        String newFirstName = firstName != null && !firstName.isBlank() ? firstName : student.getFirstName();
        String newLastName = lastName != null && !lastName.isBlank() ? lastName : student.getLastName();

        if (!Objects.equals(student.getFirstName(), newFirstName)
                || !Objects.equals(student.getLastName(), newLastName)) {
            Optional<Student> studentOptional = studentRepository.findByFirstNameAndLastName(newFirstName, newLastName);
            if (studentOptional.isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Name already taken");
            }
            student.setFirstName(newFirstName);
            student.setLastName(newLastName);
        }
    }
}
