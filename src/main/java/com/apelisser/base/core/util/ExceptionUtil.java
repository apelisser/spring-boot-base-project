package com.apelisser.base.core.util;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Optional;

public final class ExceptionUtil {

    private ExceptionUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Finds the root cause of a given {@link Throwable} or returns an empty
     * {@link Optional} if the given exception is null.
     *
     * @param ex the given exception
     * @return an optional containing the root cause of the given exception
     */
    public static Optional<Throwable> findRootCause(Throwable ex) {
        if (ex == null) {
            return Optional.empty();
        }

        Throwable rootCause = ex;
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }
        return Optional.of(rootCause);
    }

    /**
     * Converts the stack trace of a given {@link Throwable} into a string.
     * Returns an empty {@link Optional} if the provided throwable is null.
     *
     * @param throwable the throwable whose stack trace is to be converted
     * @return an optional containing the stack trace as a string, or empty if
     *         the throwable is null or an error occurs during conversion
     */
    public static Optional<String> getStackTraceAsString(Throwable throwable) {
        if (throwable == null) {
            return Optional.empty();
        }

        try (OutputStream out = new ByteArrayOutputStream();
                PrintStream printStream = new PrintStream(out)) {
            throwable.printStackTrace(printStream);
            printStream.flush();
            return Optional.ofNullable(out.toString());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}