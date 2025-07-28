package com.spring.jwt.Attandance;

public class ResourceAlreadyExistsException extends RuntimeException {
    /**
     * Constructs a new exception indicating that a resource already exists.
     *
     * @param message the detail message explaining the exception
     */
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
