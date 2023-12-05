package io.github.dominys.content.hasher.impl.internal;

import io.github.dominys.content.hasher.impl.HasherFactory;
import io.github.dominys.content.hasher.impl.config.Configuration;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RequiredArgsConstructor
public class MapHasher implements FieldValueHasher<Object> {

    private final ValueHasher<Object> keyHasher;
    private final ValueHasher<Object> valueHasher;

    @Override
    public String getHash(Object value, Configuration config) {
        var map = (Map<?,?>) value;
        if (MapUtils.isEmpty(map)) {
            return null;
        }
        var hasher = HasherFactory.hasher();
        map.entrySet().stream()
                .map(es -> keyHasher.getHash(es.getKey(), config) + valueHasher.getHash(es.getValue(), config))
                .sorted(String::compareTo)
                .forEachOrdered(h -> hasher.putString(h, StandardCharsets.UTF_8));
        return hasher.hash().toString();
    }

    @Override
    public boolean match(Type sourceType) {
        return false;
    }

}
