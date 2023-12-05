package io.github.dominys.content.hasher.api.exceptions;

public class HasherCreationException extends RuntimeException {

    public HasherCreationException(String message) {
        super(message);
    }

    public HasherCreationException(String message, Throwable cause) {
        super(message, cause);
    }

}
