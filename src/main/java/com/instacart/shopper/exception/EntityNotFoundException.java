package com.instacart.shopper.exception;

/**
 * An user exception type to specify the requested entity is not found in the system.
 *
 * @author arun
 */
public class EntityNotFoundException extends Exception {

    private static final long serialVersionUID = -6846318909313103654L;

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
