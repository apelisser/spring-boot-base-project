package com.apelisser.base.application.api.exceptionhandler.model;

import static com.apelisser.base.core.i18n.MessageConstants.*;

public enum ProblemType {

    SYSTEM_ERROR(
            PROBLEM_SYSTEM_ERROR_TITLE,
            PROBLEM_SYSTEM_ERROR_PATH),

    UNREADABLE_MESSAGE(
            PROBLEM_UNREADABLE_MESSAGE_TITLE,
            PROBLEM_UNREADABLE_MESSAGE_PATH),

    INVALID_DATA(
            PROBLEM_INVALID_DATA_TITLE,
            PROBLEM_INVALID_DATA_PATH),

    RESOURCE_NOT_FOUND(
            PROBLEM_RESOURCE_NOT_FOUND_TITLE,
            PROBLEM_RESOURCE_NOT_FOUND_PATH),

    POLICY_VIOLATION(
            PROBLEM_POLICY_VIOLATION_TITLE,
            PROBLEM_POLICY_VIOLATION_PATH),

    METHOD_NOT_SUPPORTED(
            PROBLEM_METHOD_NOT_ALLOWED_TITLE,
            PROBLEM_METHOD_NOT_ALLOWED_PATH),

    HTTP_MEDIA_TYPE_NOT_SUPPORTED(
            PROBLEM_HTTP_MEDIA_TYPE_NOT_SUPPORTED_TITLE,
            PROBLEM_HTTP_MEDIA_TYPE_NOT_SUPPORTED_PATH),

    HTTP_MEDIA_TYPE_NOT_ACCEPTABLE(
            PROBLEM_HTTP_MEDIA_TYPE_NOT_ACCEPTABLE_TITLE,
            PROBLEM_HTTP_MEDIA_TYPE_NOT_ACCEPTABLE_PATH);

    private final String title;
    private final String path;

    ProblemType(String title, String path) {
        this.title = title;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }

}
