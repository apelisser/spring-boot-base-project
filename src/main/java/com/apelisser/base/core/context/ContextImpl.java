package com.apelisser.base.core.context;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class ContextImpl implements Context {

    @Override
    public void add(ContextKey key, String value) {
        validateKey(key);
        MDC.put(key.getName(), value);
    }

    @Override
    public Optional<String> get(ContextKey key) {
        validateKey(key);
        return Optional.ofNullable(MDC.get(key.getName()));
    }

    @Override
    public Optional<Map<String, String>> getAll() {
        Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();

        return copyOfContextMap != null && !copyOfContextMap.isEmpty()
            ? Optional.of(copyOfContextMap)
            : Optional.empty();
    }

    @Override
    public boolean exists(ContextKey key) {
        validateKey(key);
        return MDC.get(key.getName()) != null;
    }

    @Override
    public void remove(ContextKey key) {
        if (key != null) {
            MDC.remove(key.getName());
        }
    }

    @Override
    public void clear() {
        MDC.clear();
    }

    private void validateKey(ContextKey key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null.");
        }
    }

}
