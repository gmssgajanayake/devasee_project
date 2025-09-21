package com.devasee.users.exceptions;

import com.devasee.users.response.CustomResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse<Object>> handleAllExceptions(Exception ex) {
        String message = "global Something went wrong. Please try again later. : " +ex.getMessage();
        CustomResponse<Object> response = new CustomResponse<>(false, message, null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); // 500
    }

    @ExceptionHandler({
            UserNotFoundException.class,
            RoleNotFoundException.class
    })
    public ResponseEntity<CustomResponse<Object>> handleCustomerNotFound(RuntimeException  ex) {
        CustomResponse<Object> response = new CustomResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // 404
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<CustomResponse<Object>> handleCustomerAlreadyExists(UserAlreadyExistsException ex) {
        CustomResponse<Object> response = new CustomResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT); // 409
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<CustomResponse<Object>> handleServiceUnavailable(ServiceUnavailableException ex) {
        CustomResponse<Object> response = new CustomResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE); // 503
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CustomResponse<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = "Invalid input. Please check your request parameters.";
        CustomResponse<Object> response = new CustomResponse<>(false, message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // 400
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<CustomResponse<Object>> handleDatabaseException(DataAccessException ex) {
        CustomResponse<Object> response = new CustomResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE); // 503
    }

    @ExceptionHandler(InvalidAuthHeaderException.class)
    public ResponseEntity<CustomResponse<Object>> handleInvalidAuthHeaderException(InvalidAuthHeaderException ex){
        CustomResponse<Object> response = new CustomResponse<>(false, ex.getMessage(),null);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED); // 401
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<CustomResponse<Object>> handleInvalidOperationException(InvalidOperationException ex){
        CustomResponse<Object> response = new CustomResponse<>(false, ex.getMessage(),null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // 400
    }

    @ExceptionHandler(UnauthorizedOperationException.class)
    public ResponseEntity<CustomResponse<Object>> handleUnauthorizedOperationException(UnauthorizedOperationException ex){
        CustomResponse<Object> response = new CustomResponse<>(false, ex.getMessage(),null);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN); // 403
    }
}
