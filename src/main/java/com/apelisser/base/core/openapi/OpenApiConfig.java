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

    private final ApplicationInfoConfig appInfo;
    private final String openApiVersion;

    public OpenApiConfig(ApplicationInfoConfig appInfo, SpringDocConfigProperties springDocConfig) {
        this.appInfo = appInfo;
        this.openApiVersion = springDocConfig.getApiDocs().getVersion().getVersion();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI openApi = new OpenAPI();
        openApi.setOpenapi(openApiVersion);
        openApi.setInfo(this.getOpenApiInfo());
        openApi.setTags(this.getTags());
        return openApi;
    }

    private Info getOpenApiInfo() {
        return new Info()
            .title(appInfo.getName())
            .description(appInfo.getDescription())
            .version(appInfo.getAppVersion());
    }

    private List<Tag> getTags() {
        return List.of(
            new Tag().name(TagConstants.APPLICATION_INFO).description(TagConstants.APPLICATION_INFO_DESCRIPTION),
            new Tag().name(TagConstants.I18N_TEST).description(TagConstants.I18N_TEST_DESCRIPTION)
        );
    }

}
