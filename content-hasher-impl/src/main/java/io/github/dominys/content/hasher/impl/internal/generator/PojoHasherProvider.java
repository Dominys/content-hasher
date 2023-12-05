package io.github.dominys.content.hasher.impl.internal.generator;

import io.github.dominys.content.hasher.api.config.HashConfiguration;
import io.github.dominys.content.hasher.api.exceptions.HasherCreationException;
import io.github.dominys.content.hasher.impl.config.Configuration;
import io.github.dominys.content.hasher.impl.internal.FieldAccessor;
import io.github.dominys.content.hasher.impl.internal.POJOHasher;
import io.github.dominys.content.hasher.impl.internal.TypeUtils;
import io.github.dominys.content.hasher.impl.internal.ValueHasher;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class PojoHasherProvider implements TargetHasherProvider {

    @Override
    public <T> ValueHasher<T> create(Type type, Configuration configuration, HashConfiguration hashConfiguration) {
        if (configuration.typeStackTrace().contains(type)) {
            log.error("Found circular reference for type {} trace {}", type, configuration.typeStackTrace().stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(" -> ")));
            throw new HasherCreationException("Circular reference found for type " + type);
        }
        configuration.typeStackTrace().push(type);

        var accessors = TypeUtils.getAccessors(type, hashConfiguration);

        var accessorHashers = accessors.entrySet().stream()
                .map(es -> Map.entry(
                        new FieldAccessor(es.getKey(), es.getValue()),
                        configuration.hasherProvider().getHasher(es.getValue().getGenericReturnType(), configuration, hashConfiguration)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        configuration.typeStackTrace().pop();
        return new POJOHasher<>(type, accessorHashers);
    }

    @Override
    public boolean match(Type type) {
        return type instanceof Class && !Object.class.equals(type);
    }
}
