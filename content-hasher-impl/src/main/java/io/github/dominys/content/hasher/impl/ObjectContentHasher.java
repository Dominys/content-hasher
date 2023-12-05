package io.github.dominys.content.hasher.impl;

import io.github.dominys.content.hasher.api.ContentHasher;
import io.github.dominys.content.hasher.api.config.HashConfiguration;
import io.github.dominys.content.hasher.impl.config.Configuration;
import io.github.dominys.content.hasher.impl.internal.generator.HasherProviderEngine;
import lombok.NonNull;

import java.lang.reflect.Type;
import java.util.LinkedList;

import static java.util.Objects.isNull;

public class ObjectContentHasher implements ContentHasher {

    private final ValueHasherFactory hasherFactory;
    private final Configuration configuration;


    public ObjectContentHasher() {
        this(false);
    }

    public ObjectContentHasher(boolean devMode) {
        hasherFactory = new ValueHasherFactory();
        configuration = new Configuration(hasherFactory, new HasherProviderEngine(), null, devMode);
    }

    public <T> String hash(@NonNull T source, HashConfiguration hashConfiguration) {
        return hasherFactory
                .getHasher(getSourceType(hashConfiguration, source),
                        new Configuration(configuration.hasherProvider(), configuration.engine(),
                        new LinkedList<>(), configuration.devMode()), hashConfiguration)
                .getHash(source, configuration);
    }

    @Override
    public <T> String hash(T source) {
        return hash(source, HashConfiguration.builder().build());
    }

    private <T> Type getSourceType(HashConfiguration hashConfiguration, T source) {
        return isNull(hashConfiguration.sourceType())
                ? source.getClass()
                : hashConfiguration.sourceType();
    }
}
