package dev.jrn.spring_test_system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jrn.spring_test_system.dto.MessageTemplateRequest;
import dev.jrn.spring_test_system.dto.MessageTemplateResponse;
import dev.jrn.spring_test_system.service.MessageSampleService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/messagetemplate")
public class MessageSampleController {
    
    private final MessageSampleService messageSampleService;

    public MessageSampleController(MessageSampleService messageSampleService) {
        this.messageSampleService = messageSampleService;
    }

    @GetMapping(path = "{key}")
    public MessageTemplateResponse getTemplateByString(@PathVariable("key") String key) {
        return new MessageTemplateResponse(messageSampleService.getMessageTemplate(key));
    }

    @PutMapping(path = "{key}")
    public void updateTemplate(@PathVariable("key") String key, @Valid @RequestBody MessageTemplateRequest request) {
        messageSampleService.updateTemplate(request.text(), key);
    }
}
