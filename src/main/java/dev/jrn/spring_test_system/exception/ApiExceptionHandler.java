package dev.jrn.spring_test_system.exception;

import java.util.Comparator;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {
        ProblemDetail problem = createProblem(HttpStatus.BAD_REQUEST, "Validation failed",
                "Request validation failed", request);
        problem.setProperty("errors", exception.getBindingResult().getFieldErrors().stream()
                .sorted(Comparator.comparing(FieldError::getField))
                .map(error -> new ValidationError(error.getField(), error.getDefaultMessage()))
                .toList());

        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolationException(
            ConstraintViolationException exception,
            HttpServletRequest request) {
        ProblemDetail problem = createProblem(HttpStatus.BAD_REQUEST, "Validation failed",
                "Request validation failed", request);
        problem.setProperty("errors", exception.getConstraintViolations().stream()
                .map(violation -> new ValidationError(violation.getPropertyPath().toString(), violation.getMessage()))
                .toList());

        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ProblemDetail> handleMissingRequestParameter(
            MissingServletRequestParameterException exception,
            HttpServletRequest request) {
        ProblemDetail problem = createProblem(HttpStatus.BAD_REQUEST, "Missing request parameter",
                "Required parameter '" + exception.getParameterName() + "' is missing", request);
        problem.setProperty("errors", List.of(new ValidationError(exception.getParameterName(), exception.getMessage())));

        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleTypeMismatch(
            MethodArgumentTypeMismatchException exception,
            HttpServletRequest request) {
        ProblemDetail problem = createProblem(HttpStatus.BAD_REQUEST, "Invalid request parameter",
                "Parameter '" + exception.getName() + "' has an invalid value", request);
        problem.setProperty("errors", List.of(new ValidationError(exception.getName(), exception.getMessage())));

        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ProblemDetail> handleResponseStatusException(
            ResponseStatusException exception,
            HttpServletRequest request) {
        HttpStatusCode status = exception.getStatusCode();
        ProblemDetail problem = createProblem(status, resolveTitle(status), resolveDetail(exception, status), request);

        return ResponseEntity.status(status).body(problem);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolation(
            DataIntegrityViolationException exception,
            HttpServletRequest request) {
        ProblemDetail problem = createProblem(HttpStatus.CONFLICT, "Data conflict",
                "The request conflicts with existing data", request);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    private ProblemDetail createProblem(HttpStatusCode status, String title, String detail, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        problem.setProperty("path", request.getRequestURI());
        return problem;
    }

    private String resolveTitle(HttpStatusCode status) {
        HttpStatus httpStatus = HttpStatus.resolve(status.value());
        if (httpStatus == null) {
            return "HTTP " + status.value();
        }
        return httpStatus.getReasonPhrase();
    }

    private String resolveDetail(ResponseStatusException exception, HttpStatusCode status) {
        if (exception.getReason() != null) {
            return exception.getReason();
        }
        return resolveTitle(status);
    }

    private record ValidationError(String field, String message) { }
}
