package com.spring.jwt.Attandance;

public class ResourceAlreadyExistsException extends RuntimeException {
    /**
     * Creates a new exception to indicate that a resource already exists.
     *
     * @param message the detail message describing the exception
     */
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
