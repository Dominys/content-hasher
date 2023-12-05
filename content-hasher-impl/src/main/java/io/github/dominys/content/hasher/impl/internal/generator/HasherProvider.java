package io.github.dominys.content.hasher.impl.internal.generator;

import io.github.dominys.content.hasher.api.config.HashConfiguration;
import io.github.dominys.content.hasher.impl.config.Configuration;
import io.github.dominys.content.hasher.impl.internal.ValueHasher;

import java.lang.reflect.Type;

public interface HasherProvider {

    <T> ValueHasher<T> create(Type type, Configuration configuration, HashConfiguration hashConfiguration);

}
