package ly.apps.android.rest.tests.services;


import ly.apps.android.rest.client.Callback;
import ly.apps.android.rest.client.annotations.Body;
import ly.apps.android.rest.client.annotations.GET;
import ly.apps.android.rest.client.annotations.POST;
import ly.apps.android.rest.client.annotations.Path;
import ly.apps.android.rest.client.annotations.QueryParam;
import ly.apps.android.rest.client.annotations.RestService;
import ly.apps.android.rest.tests.models.response.TestResponse;

import java.util.List;

@RestService
public interface TestRestService {

    @GET("/list/{model}/other/{test}")
    void sampleGet(@Path("model") String model, @Path("test") String test, @QueryParam("size") String size, Callback<TestResponse> callback);

    @GET("/list/{model}/other/{test}/list")
    void sampleGetList(@Path("model") String model, @Path("test") String test, @QueryParam("size") String size, Callback<List<TestResponse>> callback);

    @POST("/list/{model}/other/{test}")
    void samplePost(@Body Object body, @Path("model") String model, @Path("test") String test, @QueryParam("size") String size, Callback<TestResponse> callback);

}
