package io.github.dominys.content.hasher.impl.internal;

import  io.github.dominys.content.hasher.annotation.IgnoreHashing;
import  io.github.dominys.content.hasher.annotation.IgnoreHashings;
import io.github.dominys.content.hasher.api.config.HashConfiguration;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class TypeUtils {

    public static Map<String, Method> getAccessors(Type type, HashConfiguration hashConfiguration) {
        var superType = ((Class) type).getSuperclass();
        Map<String, Method> result = new HashMap<>();
        if (superType != null && superType != Object.class && superType != Enum.class) {
            result = getAccessors(superType, hashConfiguration);
        }
        result.putAll(Stream.of(((Class) type).getDeclaredMethods())
                .filter(f -> !isSystemMethod(f))
                .filter(TypeUtils::isAccessorMethod)
                .filter(TypeUtils::isValidAccessorName)
                .filter(f -> !isIgnored(f, hashConfiguration))
                .collect(Collectors.toMap(Method::getName, f -> f)));
        return result;
    }

    private static boolean isIgnored(AccessibleObject accessibleObject, HashConfiguration hashConfiguration) {
        var annotations = collectIgnoreAnnotations(accessibleObject);
        if (CollectionUtils.isEmpty(annotations)) {
            return false;
        }
        return isIgnoredByDefault(annotations, hashConfiguration)
                || isIgnoredByTags(annotations, hashConfiguration);
    }

    private static Collection<IgnoreHashing> collectIgnoreAnnotations(AccessibleObject accessibleObject) {
        Set<IgnoreHashing> result = new HashSet<>();
        if (accessibleObject.isAnnotationPresent(IgnoreHashing.class)) {
            result.addAll(Arrays.asList(accessibleObject.getAnnotationsByType(IgnoreHashing.class)));
        }
        if (accessibleObject.isAnnotationPresent(IgnoreHashings.class)) {
            result.addAll(Arrays.stream(accessibleObject.getAnnotationsByType(IgnoreHashings.class))
                    .map(IgnoreHashings::value)
                    .flatMap(Arrays::stream)
                    .collect(Collectors.toSet()));
        }
        return result;
    }

    private static boolean isIgnoredByTags(Collection<IgnoreHashing> annotations, HashConfiguration hashConfiguration) {
        if (CollectionUtils.isEmpty(hashConfiguration.tags())) {
            return false;
        }
        return annotations.stream()
                .anyMatch(f -> ArrayUtils.isNotEmpty(f.value()) &&
                        CollectionUtils.containsAny(hashConfiguration.tags(), f.value()));
    }

    private static boolean isIgnoredByDefault(Collection<IgnoreHashing> annotations, HashConfiguration hashConfiguration) {
        return !hashConfiguration.excludeDefault() &&
                annotations.stream()
                .anyMatch(f -> ArrayUtils.isEmpty(f.value()));
    }

    private static boolean isAccessorMethod(Method method) {
        return Modifier.isPublic(method.getModifiers())
                && method.getParameterCount() == 0
                && !method.getReturnType().equals(void.class);
    }

    private static boolean isSystemMethod(Method method) {
        return method.isBridge() || method.isSynthetic();
    }

    public static boolean isValidAccessorName(Method method) {
        var methodName = method.getName();
        return (methodName.startsWith("get") && methodName.length() > 3)
                || (methodName.startsWith("is") && methodName.length() > 2);
    }

    private TypeUtils() {
        throw new IllegalAccessError("Utility class");
    }
}
