package io.github.dominys.content.hasher.impl.internal;

import io.github.dominys.content.hasher.api.exceptions.HashProcessingException;
import io.github.dominys.content.hasher.impl.HasherFactory;
import io.github.dominys.content.hasher.impl.config.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
public class POJOHasher<T> implements ValueHasher<T> {

    private final Type type;
    private final SortedMap<FieldAccessor, ValueHasher<Object>> fields;

    public POJOHasher(Type type, Map<FieldAccessor, ValueHasher<Object>> fields) {
        this.type = type;
        this.fields = new TreeMap<>(Comparator.comparing(FieldAccessor::fieldName));
        this.fields.putAll(fields);
    }

    @Override
    public String getHash(T value, Configuration config) {
        var hasher = HasherFactory.hasher();

        var fieldHashes = fields.entrySet().stream()
                .map(es -> Pair.of(es.getKey().fieldName(), getFieldHash(es, value, config)))
                .filter(es -> es.getRight() != null)
                .toList();

        if (config.devMode()){
            fieldHashes.forEach(p -> log.info("Type {} field {} hash value {{}}", ((Class)type).getSimpleName(), p.getLeft(), p.getRight()));
        }
        fieldHashes.stream()
                .map(p -> p.getLeft() + p.getRight())
                .forEach(h -> hasher.putString(h, StandardCharsets.UTF_8));
        return hasher.hash().toString();
    }

    private String getFieldHash(Map.Entry<FieldAccessor, ValueHasher<Object>> entry, T value, Configuration config) {
        try {
            var fieldValue = entry.getKey().method().invoke(value);
            if (fieldValue == null) {
                return null;
            }
            return entry.getValue().getHash(fieldValue, config);
        } catch (Exception e) {
            log.error("Error getting value for hash method " + entry.getKey(), e);
            throw new HashProcessingException("Error getting value for hash", e);
        }
    }
}
