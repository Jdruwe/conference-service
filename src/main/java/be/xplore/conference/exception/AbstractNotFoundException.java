package be.xplore.conference.exception;

public abstract class AbstractNotFoundException extends RuntimeException {
    public AbstractNotFoundException() {
    }

    public AbstractNotFoundException(String message) {
        super(message);
    }
}
