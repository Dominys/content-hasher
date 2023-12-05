package io.github.dominys.content.hasher.impl.internal.generator;

import io.github.dominys.content.hasher.api.config.HashConfiguration;
import io.github.dominys.content.hasher.impl.config.Configuration;
import io.github.dominys.content.hasher.impl.internal.MapHasher;
import io.github.dominys.content.hasher.impl.internal.ValueHasher;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

@SuppressWarnings("unchecked")
public class MapHasherProvider implements TargetHasherProvider {

    @Override
    public <T> ValueHasher<T> create(Type type, Configuration configuration, HashConfiguration hashConfiguration) {
        var parameterizedType = ((ParameterizedType) type);
        var keyType = parameterizedType.getActualTypeArguments()[0];
        var valueType = parameterizedType.getActualTypeArguments()[1];
        var provider = configuration.hasherProvider();
        return (ValueHasher<T>) new MapHasher(provider.getHasher(keyType, configuration, hashConfiguration),
                provider.getHasher(valueType, configuration, hashConfiguration));
    }

    @Override
    public boolean match(Type type) {
        if (!(type instanceof ParameterizedType)) {
            return false;
        }
        if (!(((ParameterizedType) type).getRawType() instanceof Class)) {
            return false;
        }
        Class<?> baseClass = (Class<?>) (((ParameterizedType) type).getRawType());
        return Map.class.isAssignableFrom(baseClass);
    }
}
