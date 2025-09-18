package com.devasee.product.exception;

import com.devasee.product.response.CustomResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {


    // Handle any unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse<Object>> handleAllExceptions(Exception ex){
        // Return a generic user-friendly response
        String message = "Something went wrong. Please try again later.";
        CustomResponse<Object> response = new CustomResponse<>(false, message, null);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // When a ProductNotFoundException is thrown anywhere in the app, this method will handle it.
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<CustomResponse<Object>> handleProductNotFound(ProductNotFoundException ex) {
        CustomResponse<Object> response = new CustomResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // 404
    }

//    // Similarly, this method handles any RuntimeException
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<CustomResponse<Object>> handleRuntimeException(RuntimeException ex) {
//        CustomResponse<Object> response = new CustomResponse<>(false, ex.getMessage(), null);
//        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    // Handle Product already exist error when saving
    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<CustomResponse<Object>> handleProductAlreadyExist(ProductAlreadyExistsException ex){
        CustomResponse<Object> response = new CustomResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT); // 409
    }

    // Handle service unavailable error
    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<CustomResponse<Object>> handleServiceUnavailableException(ServiceUnavailableException ex){
        CustomResponse<Object> response = new CustomResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE); // 503
    }

    // Handle Method mismatch error
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CustomResponse<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = "Invalid input. Please check your request parameters.";
        CustomResponse<Object> response = new CustomResponse<>(false, message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // 400
    }

    // Handling Database error (inbuilt)
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<CustomResponse<Object>> handleDatabaseException(DataAccessException ex){
        CustomResponse<Object> response = new CustomResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE); // 503
    }
}
