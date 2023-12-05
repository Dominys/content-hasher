package io.github.dominys.content.hasher.impl.internal.generator;

import io.github.dominys.content.hasher.api.config.HashConfiguration;
import io.github.dominys.content.hasher.api.exceptions.HasherCreationException;
import io.github.dominys.content.hasher.impl.config.Configuration;
import io.github.dominys.content.hasher.impl.internal.ValueHasher;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Type;
import java.util.Set;

public class HasherProviderEngine implements HasherProvider {

    private final Set<TargetHasherProvider> hasherProviders = Set.of(new PojoHasherProvider(),
            new CollectionHasherProvider(), new MapHasherProvider(), new WildcardHasherProvider());

    @Override
    public <T> ValueHasher<T> create(Type type, Configuration configuration, HashConfiguration hashConfiguration) {
        var providers = hasherProviders.stream()
                .filter(f -> f.match(type))
                .toList();
        if (CollectionUtils.isEmpty(providers)) {
            throw new HasherCreationException("Hasher provider not found for type " + type);
        }
        if (providers.size() > 1) {
            throw new HasherCreationException("Found " + providers.size()
                    + " providers but expect only one for type " + type);
        }

        return providers.get(0).create(type, configuration, hashConfiguration);
    }
}
