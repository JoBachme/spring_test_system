package dev.jrn.spring_test_system.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class StudentAccessEvaluator {

    private final String userUsername;
    private final Integer userStudentId;

    public StudentAccessEvaluator(
            @Value("${app.security.user.username:student1}") String userUsername,
            @Value("${app.security.user.student-id:1}") Integer userStudentId) {
        this.userUsername = userUsername;
        this.userStudentId = userStudentId;
    }

    public boolean canAccessStudent(Authentication authentication, Integer studentId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        if (hasRole(authentication, "ROLE_ADMIN")) {
            return true;
        }
        return authentication.getName().equals(userUsername) && userStudentId.equals(studentId);
    }

    private boolean hasRole(Authentication authentication, String role) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(role));
    }
}
