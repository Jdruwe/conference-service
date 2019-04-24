package be.xplore.conference.rest.config;

import be.xplore.conference.excpetion.AdminNameAlreadyExistsException;
import be.xplore.conference.excpetion.EmailAlreadyExistsException;
import be.xplore.conference.excpetion.RoomAlreadyRegisteredException;
import be.xplore.conference.excpetion.RoomScheduleNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {RoomScheduleNotFoundException.class})
    public ResponseEntity<?> handleRoomScheduleNotFoundException(RoomScheduleNotFoundException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {EmailAlreadyExistsException.class})
    public ResponseEntity<?> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class})
    public ResponseEntity<?> handleAdminNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {AdminNameAlreadyExistsException.class})
    public ResponseEntity<?> handleAdminNameAlreadyExistsException(AdminNameAlreadyExistsException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {RoomAlreadyRegisteredException.class})
    public ResponseEntity<?> RoomAlreadyRegisteredException(RoomAlreadyRegisteredException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}
