package com.instacart.shopper.exception;

/**
 * An user exception type to specify that a conflict was found while creating this exception.
 *
 * @author arun
 */
public class EntityConflictException extends Exception {

    private static final long serialVersionUID = -6846318909313103654L;

    public EntityConflictException(String message) {
        super(message);
    }

    public EntityConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
