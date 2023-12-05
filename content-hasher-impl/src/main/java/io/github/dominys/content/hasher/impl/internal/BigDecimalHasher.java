package io.github.dominys.content.hasher.impl.internal;

import io.github.dominys.content.hasher.impl.config.Configuration;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class BigDecimalHasher implements FieldValueHasher<BigDecimal> {

    @Override
    public String getHash(BigDecimal value, Configuration config) {
        return new DecimalFormat("###.00").format(value);
    }

    @Override
    public boolean match(Type sourceType) {
        return sourceType == BigDecimal.class;
    }
}
