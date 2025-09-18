package org.devasee.promo.exception;

import org.devasee.promo.response.CustomResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Centralized Exception Handling for Promo module
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle Ads Not Found error
    @ExceptionHandler(AdsNotFoundException.class)
    public ResponseEntity<CustomResponse<Object>> handleAdsNotFound(AdsNotFoundException ex) {
        CustomResponse<Object> response = new CustomResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Handle Service Unavailable error
    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<CustomResponse<Object>> handleServiceUnavailable(ServiceUnavailableException ex) {
        CustomResponse<Object> response = new CustomResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

    // Handle Database errors
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<CustomResponse<Object>> handleDatabaseException(DataAccessException ex) {
        CustomResponse<Object> response = new CustomResponse<>(false, "Database error: " + ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

    // Handle generic errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse<Object>> handleGenericException(Exception ex) {
        CustomResponse<Object> response = new CustomResponse<>(false, "Something went wrong: " + ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
