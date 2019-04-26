package be.xplore.conference.exception;

public class EmailAlreadyExistsException extends AbstractAlreadyExistException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
