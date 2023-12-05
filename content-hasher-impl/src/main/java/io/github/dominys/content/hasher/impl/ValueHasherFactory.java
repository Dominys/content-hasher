package io.github.dominys.content.hasher.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.github.dominys.content.hasher.api.config.HashConfiguration;
import io.github.dominys.content.hasher.api.exceptions.HasherCreationException;
import io.github.dominys.content.hasher.impl.config.Configuration;
import io.github.dominys.content.hasher.impl.internal.BigDecimalHasher;
import io.github.dominys.content.hasher.impl.internal.DateHasher;
import io.github.dominys.content.hasher.impl.internal.FieldValueHasher;
import io.github.dominys.content.hasher.impl.internal.SimpleTypesHasher;
import io.github.dominys.content.hasher.impl.internal.ValueHasher;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.Set;
import java.util.function.Function;

@Slf4j
public class ValueHasherFactory implements HasherProvider {

    private static final Set<FieldValueHasher<?>> DEFAULT_HASHERS = Set.of(
            new SimpleTypesHasher(), new BigDecimalHasher(), new DateHasher());

    private static final Cache<HashConfiguration, ValueHasher<?>> CACHE = CacheBuilder.newBuilder().build();

    @Override
    @SuppressWarnings("unchecked")
    public <T> ValueHasher<T> getHasher(Type type, Configuration config, HashConfiguration hashConfiguration) {
        try {
            return (ValueHasher<T>) CACHE.get(hashConfiguration.toBuilder()
                    .sourceType(type)
                    .build(), () -> computeHasher(type, config, hashConfiguration));
        } catch (Exception e) {
            log.error("Can't get hasher for type : {}", type, e);
            throw new HasherCreationException("Failed to get hasher for type " + type, e);
        }
    }

    private ValueHasher<?> computeHasher(Type type, Configuration config, HashConfiguration hashConfiguration) {
        return DEFAULT_HASHERS.stream()
                .filter(f -> f.match(type))
                .findFirst()
                .<ValueHasher<?>>map(Function.identity())
                .orElseGet(() -> config.engine().create(type, config, hashConfiguration));
    }

}
