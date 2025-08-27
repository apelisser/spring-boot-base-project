package com.apelisser.base.core.openapi.customizers;

import com.apelisser.base.application.api.exceptionhandler.model.Problem;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springdoc.core.properties.SpringDocConfigProperties.ApiDocs.OpenApiVersion.OPENAPI_3_1;

@Component
public class ProblemCustomizer implements OpenApiCustomizer {

    private static final String BAD_REQUEST_RESPONSE = "BadRequestResponse";
    private static final String METHOD_NOT_ALLOWED_RESPONSE = "MethodNotAllowedResponse";
    private static final String NOT_FOUND_RESPONSE = "NotFoundResponse";
    private static final String NOT_ACCEPTABLE_RESPONSE = "NotAcceptableResponse";
    private static final String HTTP_MEDIA_TYPE_NOT_SUPPORTED = "MediaTypeNotSupportedResponse";
    private static final String INTERNAL_SERVER_ERROR_RESPONSE = "InternalServerErrorResponse";

    private final boolean isOpenApi31Version;

    public ProblemCustomizer(SpringDocConfigProperties springDocConfig) {
        this.isOpenApi31Version = springDocConfig.getApiDocs().getVersion() == OPENAPI_3_1;
    }

    @Override
    public void customise(OpenAPI openApi) {
        this.updateComponents(openApi.getComponents());

        openApi.getPaths()
            .values()
            .forEach(pathItem -> pathItem.readOperationsMap()
                .forEach((httpMethod, operation) -> {
                    ApiResponses responses = operation.getResponses();
                    switch (httpMethod) {
                        case PATCH, POST, PUT:
                            responses.addApiResponse("400", new ApiResponse().$ref(BAD_REQUEST_RESPONSE));
                            responses.addApiResponse("405", new ApiResponse().$ref(METHOD_NOT_ALLOWED_RESPONSE));
                            responses.addApiResponse("406", new ApiResponse().$ref(NOT_ACCEPTABLE_RESPONSE));
                            responses.addApiResponse("415", new ApiResponse().$ref(HTTP_MEDIA_TYPE_NOT_SUPPORTED));
                            responses.addApiResponse("500", new ApiResponse().$ref(INTERNAL_SERVER_ERROR_RESPONSE));
                            break;
                        default:
                            responses.addApiResponse("400", new ApiResponse().$ref(BAD_REQUEST_RESPONSE));
                            responses.addApiResponse("405", new ApiResponse().$ref(METHOD_NOT_ALLOWED_RESPONSE));
                            responses.addApiResponse("406", new ApiResponse().$ref(NOT_ACCEPTABLE_RESPONSE));
                            responses.addApiResponse("500", new ApiResponse().$ref(INTERNAL_SERVER_ERROR_RESPONSE));
                            break;
                    }
                })
            );
    }

    private void updateComponents(Components components) {
        this.loadSchemas().forEach(components::addSchemas);
        this.loadResponses().forEach(components::addResponses);
    }

    private Map<String, Schema> loadSchemas() {
        Map<String, Schema> schemas = new HashMap<>();

        Map<String, Schema> problemSchema = ModelConverters.getInstance(isOpenApi31Version).read(Problem.class);
        Map<String, Schema> problemObjectSchema = ModelConverters.getInstance(isOpenApi31Version).read(Problem.Object.class);

        schemas.putAll(problemSchema);
        schemas.putAll(problemObjectSchema);

        return schemas;
    }

    private Map<String, ApiResponse> loadResponses() {
        Map<String, ApiResponse> responses = new LinkedHashMap<>();

        var mediaType = new io.swagger.v3.oas.models.media.MediaType();
        mediaType.schema(new Schema<Problem>().$ref("#/components/schemas/Problem"));

        Content content = new Content()
            .addMediaType(MediaType.APPLICATION_JSON_VALUE, mediaType);

        responses.put(BAD_REQUEST_RESPONSE, new ApiResponse()
            .description("Bad request")
            .content(content));

        responses.put(METHOD_NOT_ALLOWED_RESPONSE, new ApiResponse()
            .description("Method not allowed")
            .content(content));

        responses.put(NOT_FOUND_RESPONSE, new ApiResponse()
            .description("Resource not found")
            .content(content));

        responses.put(NOT_ACCEPTABLE_RESPONSE, new ApiResponse()
            .description("Resource has no representation that would be acceptable to the client")
            .content(content));

        responses.put(HTTP_MEDIA_TYPE_NOT_SUPPORTED, new ApiResponse()
            .description("Unsupported media type")
            .content(content));

        responses.put(INTERNAL_SERVER_ERROR_RESPONSE, new ApiResponse()
            .description("Internal server error")
            .content(content));

        return responses;
    }

}
