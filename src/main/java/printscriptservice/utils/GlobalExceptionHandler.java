package printscriptservice.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

  // Handle IllegalArgumentException globally
  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  // Handle HttpServerErrorException globally
  @ExceptionHandler(org.springframework.web.client.HttpServerErrorException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<String> handleHttpServerErrorException(
      org.springframework.web.client.HttpServerErrorException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  // Handle other exceptions globally
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<String> handleGeneralException(Exception ex) {
    return new ResponseEntity<>(
        "An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
