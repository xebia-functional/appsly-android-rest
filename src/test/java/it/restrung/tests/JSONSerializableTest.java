package it.restrung.tests;

import it.restrung.tests.models.request.TestEntity;
import it.restrung.tests.models.request.TestNestedEntity;
import it.restrung.tests.models.request.ThirdEntity;
import it.restrung.tests.models.response.TestResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JSONSerializableTest {

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
            }});
        }};

    }


    @Test
    public void testJSONSerialization() throws JSONException {
        TestEntity testEntity = getTestEntity();
        String json = testEntity.toJSON();
        System.out.printf("Evaluating : %s%n", json);
        JSONObject jsonObject = new JSONObject(json);
        assertNotNull(jsonObject);
        assertNotNull(jsonObject.opt("string"));
        assertNotNull(jsonObject.opt("nestedEntity"));
        JSONObject nestedJSON = jsonObject.optJSONObject("nestedEntity");
        assertNotNull(nestedJSON.opt("nestedMap"));
        assertNotNull(nestedJSON.opt("thirdEntities"));
    }

    @Test
    public void testAnnotatedProperties() throws JSONException {
        TestEntity testEntity = getTestEntity();
        String id = testEntity.getObjectId();
        String json = testEntity.toJSON();
        JSONObject jsonObject = new JSONObject(json);
        assertNotNull(jsonObject);
        assertNotNull(jsonObject.opt("_id"));
        TestResponse deserialized = new TestResponse();
        deserialized.fromJSON(jsonObject);
        assertEquals("Deserialization based on @JsonProperty failed", deserialized.getObjectId(), id);

    }

}
