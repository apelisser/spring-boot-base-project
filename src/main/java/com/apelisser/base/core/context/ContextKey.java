package com.apelisser.base.core.context;

public enum ContextKey {

    REQUEST_ID("requestId");

    private final String key;

    ContextKey(String key) {
        this.key = key;
    }

    public String getName() {
        return key;
    }

}
