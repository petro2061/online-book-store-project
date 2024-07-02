package project.onlinebookstore.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomGlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        List<String> errors = ex.getBindingResult()
                .getAllErrors().stream()
                .map(this::getMessageError)
                .toList();

        body.put("timestamp", LocalDateTime.now());
        body.put("code", HttpStatus.BAD_REQUEST.value());
        body.put("status", HttpStatus.BAD_REQUEST);
        body.put("errors", errors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataProcessingException.class)
    public ResponseEntity<Map<String, Object>> handleDataProcessingException(
            DataProcessingException ex) {
        return new ResponseEntity<>(getBody(ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFoundException(
            EntityNotFoundException ex) {
        return new ResponseEntity<>(getBody(ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex) {
        return new ResponseEntity<>(getBody(ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String getMessageError(ObjectError error) {
        if (error instanceof FieldError fieldError) {
            String field = fieldError.getField();
            String message = fieldError.getDefaultMessage();
            return "field: "
                    + field
                    + " -> "
                    + "message: "
                    + message;
        }
        return error.getDefaultMessage();
    }

    private Map<String, Object> getBody(Exception exception) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
        body.put("error message", exception.getMessage());
        return body;
    }
}
