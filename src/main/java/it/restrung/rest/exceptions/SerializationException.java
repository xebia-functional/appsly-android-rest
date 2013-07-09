package it.restrung.rest.exceptions;


public class SerializationException extends RuntimeException {


    public SerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializationException(Throwable cause) {
        super(cause);
    }

}
