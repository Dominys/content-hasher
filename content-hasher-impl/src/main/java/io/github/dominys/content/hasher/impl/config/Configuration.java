package io.github.dominys.content.hasher.impl.config;

import io.github.dominys.content.hasher.impl.internal.generator.HasherProvider;

import java.lang.reflect.Type;
import java.util.Deque;

public record Configuration(
        io.github.dominys.content.hasher.impl.HasherProvider hasherProvider,
        HasherProvider engine,
        Deque<Type> typeStackTrace,
        boolean devMode
) {

}
