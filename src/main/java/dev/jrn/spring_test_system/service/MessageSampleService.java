package dev.jrn.spring_test_system.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import dev.jrn.spring_test_system.entity.MessageSample;
import dev.jrn.spring_test_system.repository.MessageSampleRepository;

@Service
public class MessageSampleService {
    
    private final MessageSampleRepository messageSampleRepository;

    public MessageSampleService(MessageSampleRepository messageSampleRepository) {
        this.messageSampleRepository = messageSampleRepository;
    }

    public String getMessageTemplate(String key) {
        return getTemplate(key).getTextSample();
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
}
