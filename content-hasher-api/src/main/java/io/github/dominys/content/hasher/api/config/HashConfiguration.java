package io.github.dominys.content.hasher.api.config;

import lombok.Builder;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

@Builder(toBuilder = true)
public record HashConfiguration(
    Type sourceType,
    Collection<String> tags,
    boolean excludeDefault) {

    public static class HashConfigurationBuilder {
        private Type sourceType;
        private Collection<String> tags = List.of();
        boolean excludeDefault;
    }
}
