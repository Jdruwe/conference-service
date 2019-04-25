package be.xplore.conference.exception;

public class AdminNameAlreadyExistsException extends RuntimeException {
    public AdminNameAlreadyExistsException(String message){
        super(message);
    }
}
