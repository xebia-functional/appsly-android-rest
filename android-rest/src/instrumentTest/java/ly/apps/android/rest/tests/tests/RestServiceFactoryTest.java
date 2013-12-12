package ly.apps.android.rest.tests.tests;


import ly.apps.android.rest.client.RestMethodCache;
import ly.apps.android.rest.client.RestServiceFactory;
import ly.apps.android.rest.tests.models.response.TestResponse;
import ly.apps.android.rest.tests.services.TestRestService;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;


public class RestServiceFactoryTest {

//    public static final String BASEURL = "http://awesome.io/api";
//
//    private static TestRestService service() {
//        return RestServiceFactory.getService(BASEURL, TestRestService.class);
//    }
//
//    private static RestMethodCache get(String methodName) {
//        RestMethodCache cache = null;
//        for (Method method : TestRestService.class.getMethods()) {
//            if (method.getName().equals(methodName)) {
//                cache = RestServiceFactory.getMethodCache(method);
//                break;
//            }
//        }
//        return cache;
//    }
//
//    @Test
//    public void testProxyGeneration() {
//        Assert.assertNotNull(service());
//    }
//
//    @Test
//    public void testResponseTypeInstrospected() {
//        RestMethodCache methodCache = get("sampleGet");
//        assertEquals(TestResponse.class, methodCache.getTargetType());
//    }



}
