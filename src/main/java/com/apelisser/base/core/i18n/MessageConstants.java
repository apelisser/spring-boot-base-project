package com.apelisser.base.core.i18n;


/**
 * This class contains constants for message codes.
 */
public final class MessageConstants {

    public static final String APPLICATION_TEST = "application.test";

    // exception-handler :: problem type
    public static final String PROBLEM_SYSTEM_ERROR_TITLE = "problem.system-error.title";
    public static final String PROBLEM_SYSTEM_ERROR_PATH = "problem.system-error.path";
    public static final String PROBLEM_UNREADABLE_MESSAGE_TITLE = "problem.unreadable-message.title";
    public static final String PROBLEM_UNREADABLE_MESSAGE_PATH = "problem.unreadable-message.path";
    public static final String PROBLEM_INVALID_DATA_TITLE = "problem.invalid-data.title";
    public static final String PROBLEM_INVALID_DATA_PATH = "problem.invalid-data.path";
    public static final String PROBLEM_RESOURCE_NOT_FOUND_TITLE = "problem.resource-not-found.title";
    public static final String PROBLEM_RESOURCE_NOT_FOUND_PATH = "problem.resource-not-found.path";
    public static final String PROBLEM_POLICY_VIOLATION_TITLE = "problem.policy-violation.title";
    public static final String PROBLEM_POLICY_VIOLATION_PATH = "problem.policy-violation.path";
    public static final String PROBLEM_METHOD_NOT_ALLOWED_TITLE = "problem.method-not-allowed.title";
    public static final String PROBLEM_METHOD_NOT_ALLOWED_PATH = "problem.method-not-allowed.path";
    public static final String PROBLEM_HTTP_MEDIA_TYPE_NOT_SUPPORTED_TITLE = "problem.http-media-type-not-supported.title";
    public static final String PROBLEM_HTTP_MEDIA_TYPE_NOT_SUPPORTED_PATH = "problem.http-media-type-not-supported.path";
    public static final String PROBLEM_HTTP_MEDIA_TYPE_NOT_ACCEPTABLE_TITLE = "problem.http-media-type-not-acceptable.title";
    public static final String PROBLEM_HTTP_MEDIA_TYPE_NOT_ACCEPTABLE_PATH = "problem.http-media-type-not-acceptable.path";

    // exception-handler :: exceptions messages
    public static final String GENERIC_USER_MESSAGE = "exception.generic.message";
    public static final String EX_METHOD_ARGUMENT_NOT_VALID_MESSAGE = "exception.method-argument-not-valid.message";
    public static final String EX_HTTP_MESSAGE_NOT_READABLE_MESSAGE = "exception.http-message-not-readable.message";
    public static final String EX_INVALID_FORMAT_MESSAGE = "exception.invalid-format.message";
    public static final String EX_INVALID_FORMAT_DETAIL = "exception.invalid-format.detail";
    public static final String EX_PROPERTY_BINDING_MESSAGE = "exception.property-binding.message";
    public static final String EX_PROPERTY_BINDING_DETAIL = "exception.property-binding.detail";
    public static final String EX_NO_RESOURCE_FOUND_MESSAGE = "exception.no-resource-found.message";
    public static final String EX_NO_RESOURCE_FOUND_DETAIL = "exception.no-resource-found.detail";
    public static final String EX_HTTP_METHOD_NOT_SUPPORTED_MESSAGE = "exception.http-method-not-supported.message";
    public static final String EX_HTTP_METHOD_NOT_SUPPORTED_DETAIL = "exception.http-method-not-supported.detail";
    public static final String EX_HTTP_MEDIA_TYPE_NOT_SUPPORTED_MESSAGE = "exception.http-media-type-not-supported.message";
    public static final String EX_HTTP_MEDIA_TYPE_NOT_SUPPORTED_DETAIL = "exception.http-media-type-not-supported.detail";
    public static final String EX_HTTP_MEDIA_TYPE_NOT_ACCEPTABLE_MESSAGE = "exception.http-media-type-not-acceptable.message";
    public static final String EX_HTTP_MEDIA_TYPE_NOT_ACCEPTABLE_DETAIL = "exception.http-media-type-not-acceptable.detail";
    public static final String EX_MISSING_REQUEST_PARAMETER_MESSAGE = "exception.missing-servlet-request-parameter.message";
    public static final String EX_MISSING_REQUEST_PARAMETER_DETAIL = "exception.missing-servlet-request-parameter.detail";
    public static final String EX_METHOD_ARGUMENT_TYPE_MISMATCH_MESSAGE = "exception.method-argument-type-mismatch.message";
    public static final String EX_METHOD_ARGUMENT_TYPE_MISMATCH_DETAIL_WITH_TYPE = "exception.method-argument-type-mismatch.detail-with-type";
    public static final String EX_METHOD_ARGUMENT_TYPE_MISMATCH_DETAIL_WITHOUT_TYPE = "exception.method-argument-type-mismatch.detail-without-type";
    public static final String EX_MISSING_REQUEST_HEADER_MESSAGE = "exception.missing-servlet-request-header.message";
    public static final String EX_MISSING_REQUEST_HEADER_DETAIL = "exception.missing-servlet-request-header.detail";
    public static final String EX_MISSING_PATH_VARIABLE_MESSAGE = "exception.missing-path-variable.message";
    public static final String EX_MISSING_PATH_VARIABLE_DETAIL = "exception.missing-path-variable.detail";
    public static final String EX_NO_HANDLER_FOUND_MESSAGE = "exception.no-handler-found.message";
    public static final String EX_NO_HANDLER_FOUND_DETAIL = "exception.no-handler-found.detail";


    private MessageConstants() {
        throw new IllegalStateException("Utility class");
    }

}
