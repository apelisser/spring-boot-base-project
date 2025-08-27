package com.apelisser.base.application.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.Locale;

import com.apelisser.base.core.context.ContextKey;
import org.slf4j.Logger;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatusCode;

import com.apelisser.base.application.api.exceptionhandler.model.Problem;
import com.apelisser.base.application.api.exceptionhandler.model.ProblemType;
import com.apelisser.base.core.context.Context;
import com.apelisser.base.core.i18n.MessageManager;

public class ExceptionHandlingHelper extends FrameworkExceptionHandler {

    private final Logger log;
    private final Context context;
    private final MessageManager messageManager;

    public ExceptionHandlingHelper(Logger log, Context context, MessageManager messageManager) {
        this.log = log;
        this.context = context;
        this.messageManager = messageManager;
    }

    @Override
    protected String getMessage(String code) {
        return getMessage(code, (Object) null);
    }

    @Override
    protected String getMessage(String code, Object... args) {
        return messageManager.getMessage(code, args);
    }

    @Override
    protected String getMessage(MessageSourceResolvable resolvable) {
        return messageManager.getMessage(resolvable);
    }

    @Override
    protected String getRequestId() {
        return context.get(ContextKey.REQUEST_ID).orElse(null);
    }

    @Override
    protected void createValidationLog(Problem problem) {
        if (problem == null) {
            return;
        }

        Locale locale = messageManager.getContextLocale();
        if (problem.hasErrorObjects()) {
            log.warn("Validation error | lang '{}' | type: '{}' | message: '{}' | fields: {}",
                locale, problem.getTitle(), problem.getDetail(), problem.getFormattedErrorObjects());
        } else {
            log.warn("Validation error | lang '{}' | type: '{}' | message: '{}'",
                locale, problem.getTitle(), problem.getDetail());
        }
    }

    @Override
    protected Problem.ProblemBuilder createProblemBuilder(HttpStatusCode status, ProblemType problemType,
            String detail) {
        String title = getMessage(problemType.getTitle());
        String type = getMessage(problemType.getPath());
        return Problem.builder()
            .status(status.value())
            .type(type)
            .title(title)
            .detail(detail)
            .timestamp(OffsetDateTime.now())
            .requestId(getRequestId());
    }

}
