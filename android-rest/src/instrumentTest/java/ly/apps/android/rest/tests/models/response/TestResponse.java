package ly.apps.android.rest.tests.models.response;


import org.codehaus.jackson.annotate.JsonProperty;

public class TestResponse {

    @JsonProperty("_id")
    private String objectId;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
