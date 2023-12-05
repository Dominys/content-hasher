package io.github.dominys.content.hasher.api;

import io.github.dominys.content.hasher.api.config.HashConfiguration;

public interface ContentHasher {
    <T> String hash(T source, HashConfiguration hashConfiguration);

    <T> String hash(T source);
}
