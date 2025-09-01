package jinjja.exception;

/**
 * Exception thrown when a required parameter is missing.
 */
public class MissingParameterException extends RuntimeException {
    public MissingParameterException(String message) {
        super(message);
    }
}
