package com.apelisser.base.application.api.exceptionhandler.model;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ProblemDetail;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "Problem")
public class Problem implements ConvertibleProblemDetail {

    private static final String TYPE_URI_TEMPLATE = "https://httpstatuses.com/%d";
    private static final String TIMESTAMP_PROPERTY_NAME = "timestamp";
    private static final String USER_MESSAGE_PROPERTY_NAME = "userMessage";
    private static final String REQUEST_ID_PROPERTY_NAME = "requestId";
    private static final String OBJECTS_PROPERTY_NAME = "objects";

    @Schema(
        description = "A URI reference that identifies the problem type.",
        examples = "https://httpstatuses.com/400",
        requiredMode = REQUIRED)
    private URI type;

    @Schema(
        description = "A short, human readable summary of the problem type.",
        examples = "Invalid data",
        requiredMode = REQUIRED)
    private String title;

    @Schema(
        description = "The HTTP status code for this occurrence of the problem.",
        examples = "400",
        requiredMode = REQUIRED)
    private Integer status;

    @Schema(
        description = "A human readable explanation specific to this occurrence of the problem.",
        examples = "One or more fields are invalid. Check and try again.",
        requiredMode = REQUIRED)
    private String detail;

    @Schema(
        description = "The URI used that caused the problem occur.",
        examples = "/base-app/api/v1/resources",
        requiredMode = REQUIRED)
    private URI instance;

    @Schema(
        description = "The date and time the problem occurred.",
        examples = "2025-08-23T18:10:59.1234567Z",
        requiredMode = REQUIRED)
    private OffsetDateTime timestamp;

    @Schema(
        description = "The identifier of the request that caused the problem.",
        examples = "dd169a04-25b0-4a52-bc99-2d729bb81483",
        requiredMode = REQUIRED)
    private String requestId;

    @Schema(
        description = "A human readable explanation to this occurrence of the problem.",
        examples = "One or more fields as invalid.",
        requiredMode = REQUIRED)
    private String userMessage;

    @Schema(
        description = "Object list of the fields that generated the error.",
        requiredMode = NOT_REQUIRED)
    private List<Object> objects;

    public Problem(URI type, String title, Integer status, String detail, URI instance, OffsetDateTime timestamp,
            String requestId, String userMessage, List<Object> objects) {
        this.type = type;
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.instance = instance;
        this.timestamp = timestamp;
        this.requestId = requestId;
        this.userMessage = userMessage;
        this.objects = objects;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle(title);
        problemDetail.setDetail(detail);
        problemDetail.setInstance(instance);

        problemDetail.setType(
            Objects.requireNonNullElseGet(type, () -> URI.create(String.format(TYPE_URI_TEMPLATE, status))));

        if (timestamp != null) {
            problemDetail.setProperty(TIMESTAMP_PROPERTY_NAME, timestamp);
        }

        if (requestId != null) {
            problemDetail.setProperty(REQUEST_ID_PROPERTY_NAME, requestId);
        }

        if (userMessage != null) {
            problemDetail.setProperty(USER_MESSAGE_PROPERTY_NAME, userMessage);
        }

        if (!CollectionUtils.isEmpty(objects)) {
            problemDetail.setProperty(OBJECTS_PROPERTY_NAME, objects);
        }

        return problemDetail;
    }

    public static ProblemBuilder builder() {
        return new ProblemBuilder();
    }

    public URI getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDetail() {
        return detail;
    }

    public URI getInstance() {
        return instance;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public List<Object> getObjects() {
        return objects;
    }

    public boolean hasErrorObjects() {
        return objects != null && !objects.isEmpty();
    }

    @JsonIgnore
    public String getFormattedErrorObjects() {
        if (!hasErrorObjects()) {
            return "[]";
        }

        String prefix = "[";
        String suffix = "]";
        String delimiter = ", ";
        return objects.stream()
            .filter(Objects::nonNull)
            .map(obj -> String.format("(%s: '%s')", obj.getName(), obj.getUserMessage()))
            .collect(Collectors.joining(delimiter, prefix, suffix));
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, title, status, detail, instance, timestamp, requestId, userMessage, objects);
    }

    @Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Problem))
            return false;
        Problem other = (Problem) obj;
        return Objects.equals(type, other.type)
            && Objects.equals(title, other.title)
            && Objects.equals(status, other.status)
            && Objects.equals(detail, other.detail)
            && Objects.equals(instance, other.instance)
            && Objects.equals(timestamp, other.timestamp)
            && Objects.equals(requestId, other.requestId)
            && Objects.equals(userMessage, other.userMessage)
            && Objects.equals(objects, other.objects);
    }

    @Override
    public String toString() {
        return "Problem [" +
            "type=" + type +
            ", title=" + title +
            ", status=" + status +
            ", detail=" + detail +
            ", instance=" + instance +
            ", timestamp=" + timestamp +
            ", requestId=" + requestId +
            ", userMessage=" + userMessage +
            ", objects=" + objects +
            "]";
    }

    @Schema(
        name = "ObjectProblem",
        description = "Object or field that generated the error.")
    public static class Object {

        @Schema(
            description = "Name of the field or object that generated the error.",
            examples = "username",
            requiredMode = REQUIRED)
        private String name;

        @Schema(
            description = "A human readable explanation to this occurrence of the problem.",
            examples = "Must not be null or blank.",
            requiredMode = REQUIRED)
        private String userMessage;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUserMessage() {
            return userMessage;
        }

        public void setUserMessage(String userMessage) {
            this.userMessage = userMessage;
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, userMessage);
        }

        @Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj)
                return true;
            if (!(obj instanceof Object))
                return false;
            Object other = (Object) obj;
            return Objects.equals(name, other.name) && Objects.equals(userMessage, other.userMessage);
        }

        @Override
        public String toString() {
            return "Object [name=" + name + ", userMessage=" + userMessage + "]";
        }

    }

    public static class ProblemBuilder {

        private URI type;
        private String title;
        private Integer status;
        private String detail;
        private URI instance;
        private OffsetDateTime timestamp;
        private String requestId;
        private String userMessage;
        private List<Object> objects;

        ProblemBuilder() {
        }

        public ProblemBuilder type(final URI type) {
            this.type = type;
            return this;
        }

        public ProblemBuilder type(final String type) {
            this.type = URI.create(type);
            return this;
        }


        public ProblemBuilder title(final String title) {
            this.title = title;
            return this;
        }

        public ProblemBuilder status(final Integer status) {
            this.status = status;
            return this;
        }

        public ProblemBuilder detail(final String detail) {
            this.detail = detail;
            return this;
        }

        public ProblemBuilder instance(final URI instance) {
            this.instance = instance;
            return this;
        }

        public ProblemBuilder timestamp(final OffsetDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ProblemBuilder requestId(final String requestId) {
            this.requestId = requestId;
            return this;
        }

        public ProblemBuilder userMessage(final String userMessage) {
            this.userMessage = userMessage;
            return this;
        }

        public ProblemBuilder objects(final List<Object> objects) {
            this.objects = objects;
            return this;
        }

        public Problem build() {
            return new Problem(
                this.type,
                this.title,
                this.status,
                this.detail,
                this.instance,
                this.timestamp,
                this.requestId,
                this.userMessage,
                this.objects);
        }

    }

}
