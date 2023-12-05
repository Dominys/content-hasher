package io.github.dominys.content.hasher.impl.internal;

import java.lang.reflect.Type;

public interface FieldValueHasher<T> extends ValueHasher<T>{

    boolean match(Type sourceType);

}
