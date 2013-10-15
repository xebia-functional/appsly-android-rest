package it.restrung.tests.models.request;

import it.restrung.rest.marshalling.request.AbstractJSONRequest;

import java.util.List;
import java.util.Map;

public class TestNestedEntity extends AbstractJSONRequest {

    private String string;

    private int anInt;

    private double aDouble;

    private boolean aBoolean;

    private Map<String, Object> nestedMap;

    private List<ThirdEntity> thirdEntities;

    private Double[] doubles;

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

    public Map<String, Object> getNestedMap() {
        return nestedMap;
    }

    public void setNestedMap(Map<String, Object> nestedMap) {
        this.nestedMap = nestedMap;
    }

    public List<ThirdEntity> getThirdEntities() {
        return thirdEntities;
    }

    public void setThirdEntities(List<ThirdEntity> thirdEntities) {
        this.thirdEntities = thirdEntities;
    }

    public Double[] getDoubles() {
        return doubles;
    }

    public void setDoubles(Double[] doubles) {
        this.doubles = doubles;
    }
}
