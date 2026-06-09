package dev.jrn.spring_test_system.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import dev.jrn.spring_test_system.entity.MessageSample;
import dev.jrn.spring_test_system.entity.StudentTest;
import dev.jrn.spring_test_system.entity.StudentTestId;
import dev.jrn.spring_test_system.repository.MessageSampleRepository;
import dev.jrn.spring_test_system.repository.StudentTestRepository;

@Service
public class MessageSampleService {
    
    private final MessageSampleRepository messageSampleRepository;
    private final StudentTestRepository studentTestRepository;

    public MessageSampleService(MessageSampleRepository messageSampleRepository,
            StudentTestRepository studentTestRepository) {
        this.messageSampleRepository = messageSampleRepository;
        this.studentTestRepository = studentTestRepository;
    }

    public String getMessageTemplate(String key) {
        return getTemplate(key).getTextSample();
    }

    @Transactional(readOnly = true)
    public String previewMessage(String key, Integer studentId, Integer testId) {
        String template = getTemplate(key).getTextSample();
        StudentTest registration = studentTestRepository.findById(new StudentTestId(studentId, testId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registration does not exist"));

        return renderTemplate(template, registration);
    }

    @Transactional
    public void updateTemplate(String text, String key) {
        MessageSample ms = getTemplate(key);
        ms.setTextSample(text);
    }

    private MessageSample getTemplate(String key) {
        return messageSampleRepository.findById(key)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Message template does not exist"));
    }

    private String renderTemplate(String template, StudentTest registration) {
        if (template == null) {
            return "";
        }

        return template
                .replace("$firstName", registration.getStudent().getFirstName())
                .replace("$lastName", registration.getStudent().getLastName())
                .replace("$testName", registration.getTest().getTestName())
                .replace("$result", Boolean.TRUE.equals(registration.getPassedFlag()) ? "passed" : "failed");
    }
}
