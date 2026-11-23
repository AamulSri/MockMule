package com.mockmule.profile_service.exception;

import com.mockmule.profile_service.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(ResourceNotFoundException ex){
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(),null);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Void>> duplicateResourceFound(DuplicateResourceException ex){
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(),null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidException(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(
                error->errors.put(error.getField(), error.getDefaultMessage())
                );
        Map<String, Object> response = new HashMap<>();

      return buildResponse(   HttpStatus.BAD_REQUEST,
              "Validation failed",
              errors
      );
    }

    private ResponseEntity<ApiResponse<Void>> buildResponse(HttpStatus httpStatus, String message, Map<String,String> errors) {

        ApiResponse<Void> apiResponse = new ApiResponse<>(
                LocalDateTime.now(),
                httpStatus.value(),
                message,
                null,
                errors
        );

        return ResponseEntity.status(httpStatus).body(apiResponse);
    }


}
