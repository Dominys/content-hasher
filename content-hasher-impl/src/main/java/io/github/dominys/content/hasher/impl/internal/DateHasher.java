package io.github.dominys.content.hasher.impl.internal;

import io.github.dominys.content.hasher.impl.config.Configuration;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public class DateHasher implements FieldValueHasher<Date> {

    @Override
    public String getHash(Date value, Configuration config) {
        return Optional.ofNullable(value)
                .map(Date::getTime)
                .map(Objects::toString)
                .orElse(null);
    }

    @Override
    @SuppressWarnings("java:S3740")
    public boolean match(Type sourceType) {
        if (!(sourceType instanceof Class typeClass)) {
            return false;
        }
        return Date.class.isAssignableFrom(typeClass);
    }
}
