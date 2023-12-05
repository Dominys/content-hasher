package io.github.dominys.content.hasher.api.exceptions;

public class HashProcessingException extends RuntimeException {

    public HashProcessingException(String message) {
        super(message);
    }

    public HashProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
