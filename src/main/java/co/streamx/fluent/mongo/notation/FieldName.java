package co.streamx.fluent.mongo.notation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface FieldName {
    Class<?> type() default String.class;

    String factoryMethod() default "";
    //    Class<?>[] factoryMethodParams() default {}; //must be String at the moment
}
