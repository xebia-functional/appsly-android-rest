package ly.apps.android.rest.client.annotations;

import ly.apps.android.rest.cache.CachePolicy;
import ly.apps.android.rest.utils.StringUtils;

public @interface Cached {

    String key() default StringUtils.EMPTY;

    CachePolicy policy() default CachePolicy.ENABLED;

    long timeToLive() default 10 * 60 * 1000;

}
