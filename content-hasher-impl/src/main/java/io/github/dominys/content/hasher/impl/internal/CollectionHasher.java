package io.github.dominys.content.hasher.impl.internal;

import io.github.dominys.content.hasher.impl.HasherFactory;
import io.github.dominys.content.hasher.impl.config.Configuration;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

@RequiredArgsConstructor
public class CollectionHasher implements FieldValueHasher<Object> {
    private final ValueHasher<Object> valueHasher;

    @Override
    public String getHash(Object value, Configuration config) {
        var collection = (Collection<?>) value;
        if (CollectionUtils.isEmpty(collection)) {
            return null;
        }
        var hasher = HasherFactory.hasher();
        collection.stream()
                .map(value1 -> value1 == null ? "null" : valueHasher.getHash(value1, config))
                .sorted(String::compareTo)
                .forEachOrdered(h -> hasher.putString(h, StandardCharsets.UTF_8));
        return hasher.hash().toString();
    }

    @Override
    public boolean match(Type sourceType) {
        return false;
    }

}
