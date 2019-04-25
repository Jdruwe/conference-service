package be.xplore.conference.excpetion;

public class AdminNameAlreadyExistsException extends RuntimeException {
    public AdminNameAlreadyExistsException(String message){
        super(message);
    }
}
