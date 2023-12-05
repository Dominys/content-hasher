package io.github.dominys.content.hasher.impl.internal.generator;

import java.lang.reflect.Type;

public interface TargetHasherProvider extends HasherProvider {

    boolean match(Type type);

}
