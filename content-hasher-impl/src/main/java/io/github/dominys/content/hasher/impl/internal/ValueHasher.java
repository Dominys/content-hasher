package io.github.dominys.content.hasher.impl.internal;

import io.github.dominys.content.hasher.impl.config.Configuration;

public interface ValueHasher<T> {

    String getHash(T value, Configuration config);

}
