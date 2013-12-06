package ly.apps.android.rest.tests.tests;

import junit.framework.TestCase;
import ly.apps.android.rest.converters.BodyConverter;
import ly.apps.android.rest.converters.JacksonConverter;
import ly.apps.android.rest.utils.HeaderUtils;
import ly.apps.android.rest.utils.IOUtils;
import ly.apps.android.rest.tests.models.request.TestEntity;
import ly.apps.android.rest.tests.models.request.TestNestedEntity;
import ly.apps.android.rest.tests.models.request.ThirdEntity;
import ly.apps.android.rest.tests.models.response.TestResponse;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;


public class JSONSerializableTest extends TestCase {

    private String getRandomString() {
        return UUID.randomUUID().toString();
    }

    private boolean getRandomBoolean() {
        return new Random().nextBoolean();
    }

    private double getRandomDouble() {
        return new Random().nextDouble();
    }

    private int getRandomInt() {
        return new Random().nextInt();
    }

    private TestEntity getTestEntity() {

        return new TestEntity() {{
            setObjectId(getRandomString());
            setString(getRandomString());
            setAnInt(getRandomInt());
            setaDouble(getRandomDouble());
            setaBoolean(getRandomBoolean());
            setNestedEntity(new TestNestedEntity() {{
                setString(getRandomString());
                setAnInt(getRandomInt());
                setaDouble(getRandomDouble());
                setaBoolean(getRandomBoolean());
                setNestedMap(new HashMap<String, Object>() {{
                    put("string", getRandomString());
                    put("boolean", getRandomBoolean());
                    put("int", getRandomInt());
                    put("double", getRandomDouble());
                    put("third", new ThirdEntity() {{
                        setString(getRandomString());
                    }});
                }});
                setThirdEntities(Arrays.<ThirdEntity>asList(
                        new ThirdEntity() {{
                            setString(getRandomString());
                        }}
                ));
                setDoubles(new Double[]{ 0.1, 0.2 });
            }});
        }};

    }

    @Test
    public void testJSONSerialization() throws JSONException, IOException {
        TestEntity testEntity = getTestEntity();
        StringEntity serialized = (StringEntity) new JacksonConverter().toRequestBody(testEntity, HeaderUtils.JSON_CONTENT_TYPE);
        String json = IOUtils.convertStreamToString(serialized.getContent());
        System.out.printf("Evaluating : %s%n", json);
        JSONObject jsonObject = new JSONObject(json);
        assertNotNull(jsonObject);
        assertNotNull(jsonObject.opt("string"));
        assertNotNull(jsonObject.opt("nestedEntity"));
        JSONObject nestedJSON = jsonObject.optJSONObject("nestedEntity");
        assertNotNull(nestedJSON.opt("nestedMap"));
        assertNotNull(nestedJSON.opt("thirdEntities"));
        assertEquals(nestedJSON.optJSONArray("doubles").toString(), new JSONArray(new Double[]{0.1, 0.2}).toString());
    }

    @Test
    public void testAnnotatedProperties() throws JSONException, IOException {
        TestEntity testEntity = getTestEntity();
        String id = testEntity.getObjectId();
        BodyConverter converter = new JacksonConverter();
        StringEntity serialized = (StringEntity) converter.toRequestBody(testEntity, HeaderUtils.JSON_CONTENT_TYPE);
        String json = IOUtils.convertStreamToString(serialized.getContent());
        JSONObject jsonObject = new JSONObject(json);
        assertNotNull(jsonObject);
        assertNotNull(jsonObject.opt("_id"));
        TestResponse deserialized = converter.fromResponseBody(TestResponse.class, HeaderUtils.JSON_CONTENT_TYPE, new StringEntity(json));
        assertEquals("Deserialization based on @JsonProperty failed", id, deserialized.getObjectId());

    }

}
