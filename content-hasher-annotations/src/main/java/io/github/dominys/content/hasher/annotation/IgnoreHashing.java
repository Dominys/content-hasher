package io.github.dominys.content.hasher.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(IgnoreHashings.class)
public @interface IgnoreHashing {
    String[] value() default {};
}
