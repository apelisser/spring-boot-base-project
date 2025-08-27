package com.apelisser.base.application.api.exceptionhandler;

import static com.apelisser.base.core.i18n.MessageConstants.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.apelisser.base.application.api.exceptionhandler.model.ConvertibleProblemDetail;
import com.apelisser.base.application.api.exceptionhandler.model.Problem;
import com.apelisser.base.application.api.exceptionhandler.model.ProblemType;
import com.apelisser.base.core.util.ExceptionUtil;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

public abstract class FrameworkExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles the case when a resource is not found.
     *
     * @param ex      the exception thrown
     * @param headers the HTTP headers of the request
     * @param status  the HTTP status code
     * @param request the WebRequest object representing the request
     * @return the ResponseEntity object containing the response to the request
     */
    @Override
    @Nullable
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        ProblemType problemType = ProblemType.RESOURCE_NOT_FOUND;
        String userMessage = getMessage(EX_NO_RESOURCE_FOUND_MESSAGE);
        String detailMessage = getMessage(EX_NO_RESOURCE_FOUND_DETAIL, ex.getResourcePath());

        Problem problem = createProblemBuilder(status, problemType, detailMessage)
                .userMessage(userMessage)
                .build();

        createValidationLog(problem);

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    /**
     * Handles the case when a request is made with an unsupported HTTP method
     *
     * @param ex      the exception thrown
     * @param headers the HTTP headers of the request
     * @param status  the HTTP status code
     * @param request the WebRequest object representing the request
     * @return the ResponseEntity object containing the response to the request
     */
    @Override
    @Nullable
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemType problemType = ProblemType.METHOD_NOT_SUPPORTED;
        String userMessage = getMessage(EX_HTTP_METHOD_NOT_SUPPORTED_MESSAGE);

        String detailMessage;
        Set<HttpMethod> supportedMethods = ex.getSupportedHttpMethods();
        if (!CollectionUtils.isEmpty(supportedMethods)) {
            headers.setAllow(supportedMethods);
            detailMessage = getMessage(EX_HTTP_METHOD_NOT_SUPPORTED_DETAIL, ex.getMethod(), supportedMethods);
        } else {
            detailMessage = userMessage;
        }

        Problem problem = createProblemBuilder(status, problemType, detailMessage)
                .userMessage(userMessage)
                .build();

        createValidationLog(problem);

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    /**
     * Handles the case when a resource is called expecting a media type that
     * it does not produce.
     *
     * @param ex      the exception thrown
     * @param headers the HTTP headers of the request
     * @param status  the HTTP status code
     * @param request the WebRequest object representing the request
     * @return the ResponseEntity object containing the response to the request
     */
    @Override
    @Nullable
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemType problemType = ProblemType.HTTP_MEDIA_TYPE_NOT_ACCEPTABLE;
        String userMessage = getMessage(EX_HTTP_MEDIA_TYPE_NOT_ACCEPTABLE_MESSAGE);

        String detailMessage;
        List<MediaType> supportedMediaTypes = ex.getSupportedMediaTypes();
        if (!CollectionUtils.isEmpty(supportedMediaTypes)) {
            detailMessage = getMessage(EX_HTTP_MEDIA_TYPE_NOT_ACCEPTABLE_DETAIL, supportedMediaTypes);
        } else {
            detailMessage = userMessage;
        }

        Problem problem = createProblemBuilder(status, problemType, detailMessage)
                .userMessage(userMessage)
                .build();

        createValidationLog(problem);

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    /**
     * Handles the case when a parameter value (query or path) violates the
     * validation policies.
     *
     * @param ex      the exception thrown
     * @param headers the HTTP headers of the request
     * @param status  the HTTP status code
     * @param request the WebRequest object representing the request
     * @return the ResponseEntity object containing the response to the request
     */
    @Override
    @Nullable
    protected ResponseEntity<Object> handleHandlerMethodValidationException(HandlerMethodValidationException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemType problemType = ProblemType.INVALID_DATA;
        String genericMessage = getMessage(EX_METHOD_ARGUMENT_NOT_VALID_MESSAGE);

        List<Problem.Object> problemObjects = ex.getValueResults().stream()
                .flatMap(parameter -> {
                    String name = parameter.getMethodParameter().getParameterName();
                    return parameter.getResolvableErrors().stream()
                            .map(resolvableError -> {
                                var object = new Problem.Object();
                                object.setName(name);
                                object.setUserMessage(getMessage(resolvableError));
                                return object;
                            });
                })
                .toList();

        Problem problem = createProblemBuilder(status, problemType, genericMessage)
                .userMessage(genericMessage)
                .objects(problemObjects)
                .build();

        createValidationLog(problem);

        return handleExceptionInternal(ex, problem, headers, status, request);

    }

    /**
     * Handles the case when a request's body could not be read due to a
     * HttpMessageNotReadableException.
     * This exception is thrown when the body of the request is invalid, either
     * because it does not match the
     * expected format or because the application is not configured to handle the
     * format.
     * The possible causes are:
     * <ul>
     * <li>Invalid format: the request body does not match the expected format</li>
     * <li>Property binding error: some property of the request body could not be
     * bound to the corresponding parameter of the controller method</li>
     * </ul>
     *
     * @param ex      the exception thrown
     * @param headers the HTTP headers of the request
     * @param status  the HTTP status code
     * @param request the WebRequest object representing the request
     * @return the ResponseEntity object containing the response to the request
     */
    @Override
    @Nullable
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Throwable rootCause = ExceptionUtil.findRootCause(ex).orElse(null);
        if (rootCause instanceof InvalidFormatException invalidFormatException) {
            return handleInvalidFormatException(invalidFormatException, headers, status, request);
        } else if (rootCause instanceof PropertyBindingException propertyBindingException) {
            return handlePropertyBinding(propertyBindingException, headers, status, request);
        }

        ProblemType problemType = ProblemType.UNREADABLE_MESSAGE;
        String userMessage = getMessage(GENERIC_USER_MESSAGE);
        String detailMessage = getMessage(EX_HTTP_MESSAGE_NOT_READABLE_MESSAGE);
        Problem problem = createProblemBuilder(status, problemType, detailMessage)
                .userMessage(userMessage)
                .build();

        createValidationLog(problem);

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    /**
     * Handles the case when a parameter value violates the validation policies.
     *
     * @param ex      the exception thrown
     * @param headers the HTTP headers of the request
     * @param status  the HTTP status code
     * @param request the WebRequest object representing the request
     * @return the ResponseEntity object containing the response to the request
     */
    @Override
    @Nullable
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();

        ProblemType problemType = ProblemType.INVALID_DATA;
        String detailMessage = getMessage(EX_METHOD_ARGUMENT_NOT_VALID_MESSAGE);
        List<Problem.Object> problemObjects = bindingResult.getAllErrors().stream()
                .map(error -> {
                    String message = getMessage(error);
                    String name = error instanceof FieldError fieldError ? fieldError.getField()
                            : error.getObjectName();

                    var problemObject = new Problem.Object();
                    problemObject.setName(name);
                    problemObject.setUserMessage(message);
                    return problemObject;
                })
                .toList();
        Problem problem = createProblemBuilder(status, problemType, detailMessage)
                .userMessage(detailMessage)
                .objects(problemObjects)
                .build();

        createValidationLog(problem);

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    /**
     * Handles the case when a resource is called with an unsupported media type.
     *
     * @param ex      the exception thrown
     * @param headers the HTTP headers of the request
     * @param status  the HTTP status code
     * @param request the WebRequest object representing the request
     * @return the ResponseEntity object containing the response to the request
     */
    @Override
    @Nullable
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemType problemType = ProblemType.HTTP_MEDIA_TYPE_NOT_SUPPORTED;
        String userMessage = getMessage(EX_HTTP_MEDIA_TYPE_NOT_SUPPORTED_MESSAGE);
        String detailMessage;
        List<MediaType> supportedMediaTypes = ex.getSupportedMediaTypes();
        if (!CollectionUtils.isEmpty(supportedMediaTypes)) {
            detailMessage = getMessage(EX_HTTP_MEDIA_TYPE_NOT_SUPPORTED_DETAIL, ex.getContentType(),
                    supportedMediaTypes);
        } else {
            detailMessage = userMessage;
        }

        Problem problem = createProblemBuilder(status, problemType, detailMessage)
                .userMessage(userMessage)
                .build();

        createValidationLog(problem);

        return handleExceptionInternal(ex, problem, headers, status, request);

    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        ProblemType problemType = ProblemType.INVALID_DATA;
        String userMessage = getMessage(EX_NO_HANDLER_FOUND_MESSAGE);
        String detailMessage = getMessage(EX_NO_HANDLER_FOUND_DETAIL, ex.getRequestURL());

        Problem problem = createProblemBuilder(status, problemType, detailMessage)
            .userMessage(userMessage)
            .build();

        createValidationLog(problem);

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    @Nullable
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemType problemType = ProblemType.INVALID_DATA;
        String userMessage = getMessage(EX_MISSING_REQUEST_PARAMETER_MESSAGE);
        String detailMessage = getMessage(EX_MISSING_REQUEST_PARAMETER_DETAIL, ex.getParameterName(),
                ex.getParameterType());

        Problem problem = createProblemBuilder(status, problemType, detailMessage)
                .userMessage(userMessage)
                .build();

        createValidationLog(problem);

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    @Nullable
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        if (ex instanceof MethodArgumentTypeMismatchException methodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatch(methodArgumentTypeMismatchException, headers, status, request);
        }

        return super.handleTypeMismatch(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        ProblemType problemType = ProblemType.INVALID_DATA;
        String userMessage = getMessage(EX_MISSING_PATH_VARIABLE_MESSAGE);
        String detailMessage = getMessage(EX_MISSING_PATH_VARIABLE_DETAIL, ex.getVariableName());

        Problem problem = createProblemBuilder(status, problemType, detailMessage)
            .userMessage(userMessage)
            .build();

        createValidationLog(problem);

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    @Nullable
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        if (ex instanceof MissingRequestHeaderException missingRequestHeaderException) {
            return handleMissingRequestHeaderException(missingRequestHeaderException, headers, status, request);
        }

        return super.handleServletRequestBindingException(ex, headers, status, request);
    }


    /**
     * This method is called when an exception is thrown during the execution of the
     * controller method. It is also called when the controller method returns a
     * {@link ResponseEntity} with a status code that is not 2xx.
     *
     * @param ex         the exception thrown
     * @param body       the body of the response to the request
     * @param headers    the headers of the response to the request
     * @param statusCode the status code of the response to the request
     * @param request    the WebRequest object representing the request
     * @return a ResponseEntity object containing the response to the request
     */
    @Override
    @Nullable
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
            HttpStatusCode statusCode, WebRequest request) {
        if (body == null) {
            String genericMessage = getMessage(GENERIC_USER_MESSAGE);
            body = Problem.builder()
                    .timestamp(OffsetDateTime.now())
                    .title(HttpStatus.valueOf(statusCode.value()).getReasonPhrase())
                    .status(statusCode.value())
                    .userMessage(genericMessage)
                    .detail(genericMessage)
                    .requestId(getRequestId())
                    .build();
        } else if (body instanceof String contentBody) {
            body = Problem.builder()
                    .timestamp(OffsetDateTime.now())
                    .title(contentBody)
                    .status(statusCode.value())
                    .userMessage(getMessage(GENERIC_USER_MESSAGE))
                    .detail(contentBody)
                    .requestId(getRequestId())
                    .build();
        }

        if (body instanceof ConvertibleProblemDetail customProblem) {
            body = customProblem.toProblemDetail();
        }

        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    /**
     * Handles the case when a request's body could not be read due to a
     * InvalidFormatException.
     * This exception is thrown when the body of the request is invalid, either
     * because it does not match the expected format or because the application is
     * not configured to handle the format.
     * The possible causes are:
     * <ul>
     * <li>Invalid format: the request body does not match the expected format</li>
     * <li>Property binding error: some property of the request body could not be
     * bound to the corresponding parameter of the controller method</li>
     * </ul>
     *
     * @param ex      the exception thrown
     * @param headers the HTTP headers of the request
     * @param status  the HTTP status code
     * @param request the WebRequest object representing the request
     * @return the ResponseEntity object containing the response to the request
     */
    protected ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        ProblemType problemType = ProblemType.UNREADABLE_MESSAGE;
        String userMessage = getMessage(EX_INVALID_FORMAT_MESSAGE);
        String detailMessage = getMessage(EX_INVALID_FORMAT_DETAIL,
                joinPath(ex.getPath()),
                ex.getValue(),
                ex.getTargetType().getSimpleName());

        Problem problem = createProblemBuilder(status, problemType, detailMessage)
                .userMessage(userMessage)
                .build();

        createValidationLog(problem);

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    /**
     * Handles the case when a request's body could not be read due to a
     * PropertyBindingException.
     * This exception is thrown when the body of the request is invalid, either
     * because it does not match the expected format or because the application is
     * not configured to handle the format.
     * The possible causes are:
     * <ul>
     * <li>Invalid format: the request body does not match the expected format</li>
     * <li>Property binding error: some property of the request body could not be
     * bound to the corresponding parameter of the controller method</li>
     * </ul>
     *
     * @param ex      the exception thrown
     * @param headers the HTTP headers of the request
     * @param status  the HTTP status code
     * @param request the WebRequest object representing the request
     * @return the ResponseEntity object containing the response to the request
     */
    protected ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        String path = joinPath(ex.getPath());
        ProblemType problemType = ProblemType.UNREADABLE_MESSAGE;
        String userMessage = getMessage(EX_PROPERTY_BINDING_MESSAGE);
        String detailMessage = getMessage(EX_PROPERTY_BINDING_DETAIL, path);

        Problem problem = createProblemBuilder(status, problemType, detailMessage)
                .userMessage(userMessage)
                .build();

        createValidationLog(problem);

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    /**
     * Handles the case when a parameter value has an invalid type.
     *
     * @param ex      the exception thrown
     * @param headers the HTTP headers of the request
     * @param status  the HTTP status code
     * @param request the WebRequest object representing the request
     * @return the ResponseEntity object containing the response to the request
     */
    private ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemType problemType = ProblemType.INVALID_DATA;
        String userMessage = getMessage(EX_METHOD_ARGUMENT_TYPE_MISMATCH_MESSAGE);
        String detailMessage;

        Class<?> requiredType = ex.getRequiredType();
        if (requiredType != null) {
            detailMessage = getMessage(EX_METHOD_ARGUMENT_TYPE_MISMATCH_DETAIL_WITH_TYPE, ex.getName(), ex.getValue(),
                requiredType.getSimpleName());
        } else {
            detailMessage = getMessage(EX_METHOD_ARGUMENT_TYPE_MISMATCH_DETAIL_WITHOUT_TYPE, ex.getName(),
                    ex.getValue());
        }

        Problem problem = createProblemBuilder(status, problemType, detailMessage)
                .userMessage(userMessage)
                .build();

        createValidationLog(problem);

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    /**
     * Handles the case when a request is missing a required header.
     *
     * @param ex      the exception thrown
     * @param headers the HTTP headers of the request
     * @param status  the HTTP status code
     * @param request the WebRequest object representing the request
     * @return the ResponseEntity object containing the response to the request
     */
    private ResponseEntity<Object> handleMissingRequestHeaderException(MissingRequestHeaderException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemType problemType = ProblemType.INVALID_DATA;
        String userMessage = getMessage(EX_MISSING_REQUEST_HEADER_MESSAGE);
        String detailMessage = getMessage(EX_MISSING_REQUEST_HEADER_DETAIL, ex.getHeaderName());

        Problem problem = createProblemBuilder(status, problemType, detailMessage)
                .userMessage(userMessage)
                .build();

        createValidationLog(problem);

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    /**
     * Joins the given references into a single string, separating each element
     * with a dot (.).
     *
     * @param references the list of references to join
     * @return the joined string
     */
    private String joinPath(List<JsonMappingException.Reference> references) {
        return references.stream()
                .map(JsonMappingException.Reference::getFieldName)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("."));
    }

    /**
     * Retrieves the message associated with the given code.
     *
     * @param code the code of the message to retrieve
     * @return the message associated with the given code
     */
    protected abstract String getMessage(String code);

    /**
     * Retrieves the message associated with the given code.
     *
     * @param code the code of the message to retrieve
     * @param args optional arguments to be interpolated into the message
     * @return the message associated with the given code
     */
    protected abstract String getMessage(String code, Object... args);

    /**
     * Retrieves the message corresponding to the given MessageSourceResolvable in
     * the current context locale.
     *
     * @param resolvable the MessageSourceResolvable object representing the message
     *                   to retrieve
     * @return the message string corresponding to the given MessageSourceResolvable
     *         in the current context locale
     */
    protected abstract String getMessage(MessageSourceResolvable resolvable);

    /**
     * Get the request ID from the context to add it to the error object that will
     * be returned
     *
     * @return identification of the current process request
     */
    protected abstract String getRequestId();

    /**
     * When there is a field validation error, generated by spring-validation,
     * this method will be called so that a log, of type WARN, is recorded with
     * error information
     *
     * @param problem object that represents the validation error problem that was
     *                generated
     */
    protected abstract void createValidationLog(Problem problem);

    /**
     * Creates a new {@link Problem.ProblemBuilder} that is pre-filled with common
     * attributes.
     *
     * @param status      the HTTP status code
     * @param problemType the type of the problem
     * @param detail      the detail of the problem
     * @return a new {@link Problem.ProblemBuilder}
     */
    protected abstract Problem.ProblemBuilder createProblemBuilder(HttpStatusCode status, ProblemType problemType,
            String detail);

}
