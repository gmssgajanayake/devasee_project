package com.devasee.orders.exception;

import com.devasee.orders.response.CustomResponse;
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

}
