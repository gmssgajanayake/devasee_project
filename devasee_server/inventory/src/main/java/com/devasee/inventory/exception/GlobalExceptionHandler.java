package com.devasee.inventory.exception;

import com.devasee.inventory.response.CustomResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle any unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse<Object>> handleAllExceptions(Exception ex) {
        String message = "Something went wrong. Please try again later.";
        CustomResponse<Object> response = new CustomResponse<>(false, message, null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle InventoryNotFoundException
    @ExceptionHandler(InventoryNotFoundException.class)
    public ResponseEntity<CustomResponse<Object>> handleInventoryNotFound(InventoryNotFoundException ex) {
        CustomResponse<Object> response = new CustomResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // 404
    }

    // Handle RuntimeException
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CustomResponse<Object>> handleRuntimeException(RuntimeException ex) {
        CustomResponse<Object> response = new CustomResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    // Handle DataAccessException (database errors)
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<CustomResponse<Object>> handleDatabaseException(DataAccessException ex) {
        CustomResponse<Object> response = new CustomResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
