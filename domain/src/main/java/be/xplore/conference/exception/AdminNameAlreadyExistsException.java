package be.xplore.conference.exception;

public class AdminNameAlreadyExistsException extends AbstractAlreadyExistException {
    public AdminNameAlreadyExistsException(String message) {
        super(message);
    }
}
