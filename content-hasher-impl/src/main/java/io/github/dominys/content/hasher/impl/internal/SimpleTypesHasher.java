package io.github.dominys.content.hasher.impl.internal;

import io.github.dominys.content.hasher.impl.config.Configuration;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Set;

public class SimpleTypesHasher implements FieldValueHasher<Object> {

    private static final Set<Class<?>> SIMPLE_TYPES = Set.of(String.class, Enum.class, Long.class, Integer.class,
            Boolean.class, Float.class);

    @Override
    public String getHash(Object value, Configuration config) {
        return value != null ? Objects.toString(value) : null;
    }

    @Override
    @SuppressWarnings("java:S3740")
    public boolean match(Type sourceType) {
        if (!(sourceType instanceof Class typeClass)) {
            return false;
        }
        return typeClass.isPrimitive() || SIMPLE_TYPES.stream().anyMatch(f -> f.isAssignableFrom(typeClass));
    }
}
