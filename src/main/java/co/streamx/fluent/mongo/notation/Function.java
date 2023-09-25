package co.streamx.fluent.mongo.notation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface Function {
    Class<?> factory();

    boolean isStatic() default true;

    boolean passThrough() default false;
}
