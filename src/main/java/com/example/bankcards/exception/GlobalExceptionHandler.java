package com.example.bankcards.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppError> handleAllExceptions(Exception ex) {
        AppError error = new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<String > handleAuthorizationException(AuthorizationException ex){
        return ResponseEntity.badRequest().body( ex.getMessage());

    }
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<String> handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException ex) {
        String message = "Requested media type is not acceptable: " + ex.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(message);
    }


    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<String> handleEmailAlreadyException(EmailAlreadyExistsException ex) {
        return ResponseEntity.badRequest().body("Такой Email уже существует " + ex.getMessage());
    }

    @ExceptionHandler(UserHasNoCardException.class)
    public ResponseEntity<String> handleUserHasNoCardException(UserHasNoCardException ex) {
        return ResponseEntity.badRequest().body("У пользователя нет доступа к этой карте" + ex.getMessage());
    }

    @ExceptionHandler(UserNoHasMoney.class)
    public ResponseEntity<String > handleUserNoHasMoney (UserNoHasMoney ex){
        return ResponseEntity.badRequest().body("Недостаточно средств на карте");
    }
    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<String> handleEmailNotFoundException(EmailNotFoundException ex) {
        return ResponseEntity.badRequest().body("Такой Email не найден  ");
    }

    @ExceptionHandler(BankCardNotFoundException.class)
    public ResponseEntity<String> handleBankCardNotFoundException(BankCardNotFoundException ex) {
        return ResponseEntity.badRequest().body("У пользователя нет банковских карт");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body("Invalid JSON format.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Entity not found: " + ex.getMessage());
    }

}
