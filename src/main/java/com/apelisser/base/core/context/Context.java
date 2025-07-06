package com.apelisser.base.core.context;

import java.util.Map;
import java.util.Optional;

public interface Context {

    /**
     * Adds a new key-value pair to the context.
     *
     * @param key   the key to added
     * @param value the value associated with the key
     */
    void add(ContextKey key, String value);

    /**
     * Retrieves the value associated with the given key from the context.
     *
     * @param key the key used to retrieve the value from the context
     * @return an {@link Optional} containing the value associated with the given key.
     * If the context does not contain the key, an empty
     * {@link Optional} is returned
     */
    Optional<String> get(ContextKey key);

    /**
     * Retrieves all the key-value pairs stored in the context.
     *
     * @return an {@link Optional} containing a map with all the key-value pairs
     * stored in the context. If the context is empty, an empty
     * {@link Optional} is returned.
     */
    Optional<Map<String, String>> getAll();

    /**
     * Checks if the context contains the specified key.
     *
     * @param key the key to check for existence in the context
     * @return true if the context contains the specified key, false otherwise
     */
    boolean exists(ContextKey key);

    /**
     * Removes the key-value pair associated with the given key from the context.
     *
     * @param key the key to remove from the context
     */
    void remove(ContextKey key);

    /**
     * Clears the context, removing all the key-value pairs from it.
     */
    void clear();

}
