package be.xplore.conference.excpetion;

public class AdminNameAlreadyExistsException extends Exception {
    public AdminNameAlreadyExistsException(String message){
        super(message);
    }
}
