package org.devasee.promo.exception;

/**
 * Thrown when an Advertisement is not found in DB
 */
public class AdsNotFoundException extends RuntimeException {
  public AdsNotFoundException(String message) {
    super(message);
  }
}
