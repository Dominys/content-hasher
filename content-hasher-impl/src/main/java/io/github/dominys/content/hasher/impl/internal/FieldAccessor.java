package io.github.dominys.content.hasher.impl.internal;

import java.lang.reflect.Method;

public record FieldAccessor(
        String fieldName,
        Method method) {

}
