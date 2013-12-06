package ly.apps.android.rest.client.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(ANNOTATION_TYPE)
@Retention(RUNTIME)
public @interface HttpMethod {

    String value();

    boolean body() default false;

}