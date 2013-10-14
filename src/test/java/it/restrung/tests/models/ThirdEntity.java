package it.restrung.tests.models;

import it.restrung.rest.marshalling.request.AbstractJSONRequest;

public class ThirdEntity extends AbstractJSONRequest {

    private String string;

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
