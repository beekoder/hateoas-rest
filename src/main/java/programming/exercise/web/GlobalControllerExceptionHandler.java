package programming.exercise.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import programming.exercise.exception.ResourceNotFoundException;


@ControllerAdvice
public class GlobalControllerExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorContent> handle(ResourceNotFoundException e) {
        return new ResponseEntity<>(new ErrorContent(e), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorContent> handle(IllegalArgumentException e) {
        LOGGER.warn("{}", e.getMessage());
        return new ResponseEntity<>(new ErrorContent(e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {Throwable.class})
    public ResponseEntity<ErrorContent> handle(Throwable e) {
        LOGGER.error("Uncaught exception.", e);
        return new ResponseEntity<>(new ErrorContent(e), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static class ErrorContent {
        private String message;

        ErrorContent(Throwable x) {
            this(x.getMessage());
        }

        ErrorContent(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}