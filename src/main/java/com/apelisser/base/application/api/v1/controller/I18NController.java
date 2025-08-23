package com.apelisser.base.application.api.v1.controller;

import static com.apelisser.base.core.i18n.MessageConstants.APPLICATION_TEST;

import java.util.HashMap;
import java.util.Map;

import com.apelisser.base.core.openapi.TagConstants;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apelisser.base.core.i18n.MessageManager;

/**
 * This controller was created only to test i18n.
 */
@Tag(name = TagConstants.I18N_TEST)
@RestController
@RequestMapping(
    path = "/api/v1/i18n",
    produces = MediaType.APPLICATION_JSON_VALUE)
public class I18NController {

    private final MessageManager message;

    public I18NController(MessageManager message) {
        this.message = message;
    }

    @GetMapping("/test")
    public Map<String, String> test() {
        String retrievedMessage = message.getMessage(APPLICATION_TEST);

        Map<String, String> response = new HashMap<>();
        response.put("message", retrievedMessage);
        return response;
    }

}
