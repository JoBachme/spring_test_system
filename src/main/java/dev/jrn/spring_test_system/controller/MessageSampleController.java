package dev.jrn.spring_test_system.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.jrn.spring_test_system.dto.MessagePreviewResponse;
import dev.jrn.spring_test_system.dto.MessageTemplateRequest;
import dev.jrn.spring_test_system.dto.MessageTemplateResponse;
import dev.jrn.spring_test_system.service.MessageSampleService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/message-templates")
public class MessageSampleController {
    
    private final MessageSampleService messageSampleService;

    public MessageSampleController(MessageSampleService messageSampleService) {
        this.messageSampleService = messageSampleService;
    }

    @GetMapping(path = "/{key}")
    @PreAuthorize("hasRole('ADMIN')")
    public MessageTemplateResponse getTemplateByString(@PathVariable("key") String key) {
        return new MessageTemplateResponse(messageSampleService.getMessageTemplate(key));
    }

    @GetMapping(path = "/{key}/preview")
    @PreAuthorize("hasRole('ADMIN')")
    public MessagePreviewResponse previewTemplate(@PathVariable("key") String key,
            @RequestParam Integer studentId,
            @RequestParam Integer testId) {
        return new MessagePreviewResponse(messageSampleService.previewMessage(key, studentId, testId));
    }

    @PutMapping(path = "/{key}")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateTemplate(@PathVariable("key") String key, @Valid @RequestBody MessageTemplateRequest request) {
        messageSampleService.updateTemplate(request.text(), key);
    }
}
