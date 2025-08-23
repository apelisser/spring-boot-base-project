package com.apelisser.base.core.openapi;

import com.apelisser.base.core.application.ApplicationInfoConfig;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private final SpringDocConfigProperties springDocConfig;
    private final ApplicationInfoConfig appInfo;

    public OpenApiConfig(SpringDocConfigProperties springDocConfig, ApplicationInfoConfig appInfo) {
        this.springDocConfig = springDocConfig;
        this.appInfo = appInfo;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI openAPI = new OpenAPI();
        openAPI.setInfo(this.getOpenApiInfo());
        openAPI.setOpenapi(this.getOpenApiVersion());
        return openAPI;
    }

    private Info getOpenApiInfo() {
        return new Info()
            .title(appInfo.getName())
            .description(appInfo.getDescription())
            .version(appInfo.getAppVersion());
    }

    private String getOpenApiVersion() {
        return springDocConfig.getApiDocs().getVersion().getVersion();
    }

}
