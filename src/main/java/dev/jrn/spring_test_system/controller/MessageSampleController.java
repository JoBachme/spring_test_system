package dev.jrn.spring_test_system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jrn.spring_test_system.service.MessageSampleService;

@RestController
@RequestMapping(path = "/api/v1/messagetemplate")
public class MessageSampleController {
    
    private final MessageSampleService messageSampleService;

    public MessageSampleController(MessageSampleService messageSampleService) {
        this.messageSampleService = messageSampleService;
    }

    @GetMapping(path = "{key}")
    public String getTemplateByString(@PathVariable("key") String key) {
        return messageSampleService.getMessageTemplate(key);
    }

    @PutMapping(path = "{key}")
    public void updateTemplate(@PathVariable("key") String key, @RequestBody String text) {
        messageSampleService.updateTemplate(text, key);
    }
}
