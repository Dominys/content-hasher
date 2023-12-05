package io.github.dominys.content.hasher.impl.internal.generator;

import io.github.dominys.content.hasher.api.config.HashConfiguration;
import io.github.dominys.content.hasher.impl.config.Configuration;
import io.github.dominys.content.hasher.impl.internal.ValueHasher;
import io.github.dominys.content.hasher.impl.internal.WildcardHasher;

import java.lang.reflect.Type;

public class WildcardHasherProvider implements TargetHasherProvider {

    @Override
    public boolean match(Type type) {
        return type instanceof Class && Object.class.equals(type);
    }

    @Override
    public <T> ValueHasher<T> create(Type type, Configuration configuration, HashConfiguration hashConfiguration) {
        return new WildcardHasher<>();
    }
}

