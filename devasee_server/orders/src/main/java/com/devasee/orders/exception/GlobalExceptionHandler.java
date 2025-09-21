package com.devasee.orders.exception;

import com.devasee.orders.response.CustomResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle any unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse<Object>> handleAllExceptions(Exception ex){
        String message = "Something went wrong. Please try again later.";
        CustomResponse<Object> response = new CustomResponse<>(false, message, null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle OrderNotFoundException
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<CustomResponse<Object>> handleOrderNotFound(OrderNotFoundException ex) {
        CustomResponse<Object> response = new CustomResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // 404
    }

    // Handle RuntimeException
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CustomResponse<Object>> handleRuntimeException(RuntimeException ex) {
        CustomResponse<Object> response = new CustomResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    // Handle InsufficientStockException
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<CustomResponse<Object>> handleInsufficientStock(InsufficientStockException ex) {
        CustomResponse<Object> response = new CustomResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // 400
    }

    // Handle OrderAlreadyExistsException
    @ExceptionHandler(OrderAlreadyExistsException.class)
    public ResponseEntity<CustomResponse<Object>> handleOrderAlreadyExists(OrderAlreadyExistsException ex){
        CustomResponse<Object> response = new CustomResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT); // 409
    }

    // Handle ServiceUnavailableException
    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<CustomResponse<Object>> handleServiceUnavailableException(ServiceUnavailableException ex){
        CustomResponse<Object> response = new CustomResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE); // 503
    }

    // Handle MethodArgumentTypeMismatchException
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CustomResponse<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = "Invalid input. Please check your request parameters.";
        CustomResponse<Object> response = new CustomResponse<>(false, message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handle DataAccessException (database errors)
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<CustomResponse<Object>> handleDatabaseException(DataAccessException ex){
        CustomResponse<Object> response = new CustomResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
