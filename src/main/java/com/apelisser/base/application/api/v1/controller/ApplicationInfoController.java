package com.apelisser.base.application.api.v1.controller;

import com.apelisser.base.core.openapi.TagConstants;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apelisser.base.core.application.ApplicationInfoConfig;

@Tag(name = TagConstants.APPLICATION_INFO)
@RestController
@RequestMapping(
    path = "/api/v1/info",
    produces = MediaType.APPLICATION_JSON_VALUE)
public class ApplicationInfoController {

    private final ApplicationInfoConfig applicationInfo;

    public ApplicationInfoController(ApplicationInfoConfig applicationInfo) {
        this.applicationInfo = applicationInfo;
    }

    @GetMapping
    public ApplicationInfoConfig getInfo() {
        return applicationInfo;
    }

}
