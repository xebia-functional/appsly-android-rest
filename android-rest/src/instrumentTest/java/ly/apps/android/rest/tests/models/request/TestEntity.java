package ly.apps.android.rest.tests.models.request;


import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;

public class TestEntity {

    @JsonProperty("_id")
    private String objectId;

    private String string;

    private int anInt;

    private double aDouble;

    private boolean aBoolean;

    private TestNestedEntity nestedEntity;

    private Date someDate;

    public boolean isaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    public double getaDouble() {
        return aDouble;
    }

    public void setaDouble(double aDouble) {
        this.aDouble = aDouble;
    }

    public int getAnInt() {
        return anInt;
    }

    public void setAnInt(int anInt) {
        this.anInt = anInt;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public TestNestedEntity getNestedEntity() {
        return nestedEntity;
    }

    public void setNestedEntity(TestNestedEntity nestedEntity) {
        this.nestedEntity = nestedEntity;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Date getSomeDate() {
        return someDate;
    }

    public void setSomeDate(Date someDate) {
        this.someDate = someDate;
    }
}
