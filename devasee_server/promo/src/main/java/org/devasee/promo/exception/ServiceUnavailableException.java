package org.devasee.promo.exception;

/**
 * Thrown when service is temporarily unavailable
 */
public class ServiceUnavailableException extends RuntimeException {
    public ServiceUnavailableException(String message) {
        super(message);
    }
}
