package io.github.dominys.content.hasher.impl.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dominys.content.hasher.api.exceptions.HashProcessingException;
import io.github.dominys.content.hasher.impl.HasherFactory;
import io.github.dominys.content.hasher.impl.config.Configuration;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;


@Slf4j
public class WildcardHasher<T> implements ValueHasher<T> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public String getHash(T value, Configuration config) {
        if (value == null) {
            return null;
        }

        var stringValue = toJson(value);

        var hasher = HasherFactory.hasher();
        hasher.putString(stringValue, StandardCharsets.UTF_8);
        return hasher.hash().toString();
    }

    private static String toJson(Object value) {
        try {
            return OBJECT_MAPPER.writeValueAsString(value);
        } catch (Exception e) {
            log.error("Error getting value for hash ", e);
            throw new HashProcessingException("Error getting value for hash", e);
        }
    }
}
