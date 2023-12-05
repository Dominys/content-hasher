package io.github.dominys.content.hasher.impl.internal.generator;

import io.github.dominys.content.hasher.api.config.HashConfiguration;
import io.github.dominys.content.hasher.impl.config.Configuration;
import io.github.dominys.content.hasher.impl.internal.CollectionHasher;
import io.github.dominys.content.hasher.impl.internal.ValueHasher;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

public class CollectionHasherProvider implements TargetHasherProvider {

    @Override
    public <T> ValueHasher<T> create(Type type, Configuration configuration, HashConfiguration hashConfiguration) {
        var parameterizedType = ((ParameterizedType) type);
        var genericType = parameterizedType.getActualTypeArguments()[0];

        return (ValueHasher<T>) new CollectionHasher(configuration.hasherProvider().getHasher(genericType, configuration, hashConfiguration));
    }

    @Override
    public boolean match(Type type) {
        if (!(type instanceof ParameterizedType)) {
            return false;
        }
        if (!(((ParameterizedType) type).getRawType() instanceof Class)) {
            return false;
        }
        var baseClass = (Class<?>) (((ParameterizedType) type).getRawType());
        return Collection.class.isAssignableFrom(baseClass);
    }
}
