package be.xplore.conference.exception;

public abstract class AbstractAlreadyExistException extends RuntimeException {
    public AbstractAlreadyExistException() {
    }

    public AbstractAlreadyExistException(String message) {
        super(message);
    }
}
