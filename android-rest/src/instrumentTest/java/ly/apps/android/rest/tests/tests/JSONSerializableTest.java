package ly.apps.android.rest.tests.tests;

import junit.framework.TestCase;
import ly.apps.android.rest.converters.impl.JacksonBodyConverter;
import ly.apps.android.rest.converters.impl.JacksonDotNetDateDeserializer;
import ly.apps.android.rest.converters.impl.JacksonDotNetDateSerializer;
import ly.apps.android.rest.utils.FileUtils;
import ly.apps.android.rest.utils.HeaderUtils;
import ly.apps.android.rest.tests.models.request.TestEntity;
import ly.apps.android.rest.tests.models.request.TestNestedEntity;
import ly.apps.android.rest.tests.models.request.ThirdEntity;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.codehaus.jackson.util.TokenBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.util.*;


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
            setSomeDate(new Date());
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
        StringEntity serialized = (StringEntity) new JacksonBodyConverter().toRequestBody(testEntity, HeaderUtils.CONTENT_TYPE_JSON);
        String json = FileUtils.convertStreamToString(serialized.getContent());
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

//    @Test
//    public void testAnnotatedProperties() throws JSONException, IOException {
//        TestEntity testEntity = getTestEntity();
//        String id = testEntity.getObjectId();
//        BodyConverter converter = new JacksonBodyConverter();
//        StringEntity serialized = (StringEntity) converter.toRequestBody(testEntity, HeaderUtils.CONTENT_TYPE_JSON);
//        String json = FileUtils.convertStreamToString(serialized.getContent());
//        JSONObject jsonObject = new JSONObject(json);
//        assertNotNull(jsonObject);
//        assertNotNull(jsonObject.opt("_id"));
//        TestResponse deserialized = converter.fromResponseBody(TestResponse.class, HeaderUtils.CONTENT_TYPE_JSON, new StringEntity(json));
//        assertEquals("Deserialization based on @JsonProperty failed", id, deserialized.getObjectId());
//
//    }

    public void testDotNetSerializer() throws IOException, JSONException {

        ObjectMapper mapper = new ObjectMapper() {{
            registerModule(
                    new SimpleModule("DotNetDateSerializationModule",
                            new Version(1, 0, 0, null)){{
                        addSerializer(Date.class, new JacksonDotNetDateSerializer());
                        addDeserializer(Date.class, new JacksonDotNetDateDeserializer());
                    }}
            );
        }};
        TestEntity testEntity = getTestEntity();
        String json = mapper.writeValueAsString(testEntity);
        JSONObject jsonObject = new JSONObject(json);
        assertEquals(jsonObject.getString("someDate"), "/Date(" + testEntity.getSomeDate().getTime() + ")/");
        TestEntity testEntity2 = mapper.readValue(json, TestEntity.class);
        assertNotNull(testEntity2.getSomeDate());
        assertEquals(testEntity.getSomeDate(), testEntity2.getSomeDate());
    }

}
