package com.apelisser.base.core.openapi;

import com.apelisser.base.core.application.ApplicationInfoConfig;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

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
        openAPI.setOpenapi(this.getOpenApiVersion());
        openAPI.setInfo(this.getOpenApiInfo());
        openAPI.setTags(this.loadTags());
        return openAPI;
    }

    private Info getOpenApiInfo() {
        return new Info()
            .title(appInfo.getName())
            .description(appInfo.getDescription())
            .version(appInfo.getAppVersion());
    }

    private List<Tag> loadTags() {
        return List.of(
            new Tag().name(TagConstants.APPLICATION_INFO).description(TagConstants.APPLICATION_INFO_DESCRIPTION),
            new Tag().name(TagConstants.I18N_TEST).description(TagConstants.I18N_TEST_DESCRIPTION)
        );
    }

    private String getOpenApiVersion() {
        return springDocConfig.getApiDocs().getVersion().getVersion();
    }

}
