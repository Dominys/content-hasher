package io.github.dominys.content.hasher.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface IgnoreHashings {
    IgnoreHashing[] value();
}
