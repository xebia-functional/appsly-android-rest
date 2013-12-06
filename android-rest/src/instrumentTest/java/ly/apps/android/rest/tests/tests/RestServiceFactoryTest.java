package ly.apps.android.rest.tests.tests;


import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

import ly.apps.android.rest.client.RestMethodCache;
import ly.apps.android.rest.client.RestServiceFactory;
import ly.apps.android.rest.tests.models.response.TestResponse;
import ly.apps.android.rest.tests.services.TestRestService;

import static org.junit.Assert.assertEquals;


public class RestServiceFactoryTest {

    public static final String BASEURL = "http://awesome.io/api";

    private static TestRestService service() {
        return RestServiceFactory.getService(BASEURL, TestRestService.class);
    }

    private static RestMethodCache get(String methodName) {
        RestMethodCache cache = null;
        for (Method method : TestRestService.class.getMethods()) {
            if (method.getName().equals(methodName)) {
                cache = RestServiceFactory.getMethodCache(method);
                break;
            }
        }
        return cache;
    }

    @Test
    public void testProxyGeneration() {
        Assert.assertNotNull(service());
    }

    @Test
    public void testResponseTypeInstrospected() {
        RestMethodCache methodCache = get("sampleGet");
        assertEquals(TestResponse.class, methodCache.getTargetClass());
    }

    @Test
    public void testUrlParamParsing() throws UnsupportedEncodingException {
        RestMethodCache methodCache = get("sampleGet");
        Assert.assertNotNull(methodCache);
        String result = methodCache.parseArgs(BASEURL, new Object[]{"model", "test", 10, null});
        assertEquals(BASEURL + "/list/model/other/test?size=10", result);
    }

    @Test
    public void testUrlParamParsingWithParamNotInPath() throws UnsupportedEncodingException {
        RestMethodCache methodCache = get("samplePost");
        Assert.assertNotNull(methodCache);
        String result = methodCache.parseArgs(BASEURL, new Object[]{new File("."), "model", "test", 10, null});
        assertEquals(BASEURL + "/list/model/other/test?size=10", result);
    }


}
