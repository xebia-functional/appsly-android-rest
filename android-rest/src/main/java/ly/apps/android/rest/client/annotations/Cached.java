package ly.apps.android.rest.client.annotations;

import ly.apps.android.rest.cache.CachePolicy;
import ly.apps.android.rest.utils.StringUtils;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface Cached {

    String key() default StringUtils.EMPTY;

    CachePolicy policy() default CachePolicy.ENABLED;

    long timeToLive() default 10 * 60 * 1000;

}
