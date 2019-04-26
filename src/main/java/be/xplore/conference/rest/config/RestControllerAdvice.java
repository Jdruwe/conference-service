package be.xplore.conference.rest.config;

import be.xplore.conference.exception.AbstractAlreadyExistException;
import be.xplore.conference.exception.AbstractAlreadyRegisteredException;
import be.xplore.conference.exception.AbstractNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {AbstractNotFoundException.class})
    public ResponseEntity<?> handleRoomScheduleNotFoundException(AbstractNotFoundException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {AbstractAlreadyExistException.class})
    public ResponseEntity<?> handleEmailAlreadyExistsException(AbstractAlreadyExistException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {AbstractAlreadyRegisteredException.class})
    public ResponseEntity<?> handleEmailAlreadyExistsException(AbstractAlreadyRegisteredException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}
