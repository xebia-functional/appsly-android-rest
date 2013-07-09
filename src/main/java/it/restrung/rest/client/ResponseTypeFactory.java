package it.restrung.rest.client;

import it.restrung.rest.marshalling.response.JSONResponse;


public interface ResponseTypeFactory {

    <T extends JSONResponse> T newInstance(Class<T> targetClass);

}
