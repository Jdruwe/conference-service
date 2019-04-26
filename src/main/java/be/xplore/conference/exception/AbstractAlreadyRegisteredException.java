package be.xplore.conference.exception;

public abstract class AbstractAlreadyRegisteredException extends RuntimeException {
    public AbstractAlreadyRegisteredException() {
    }

    public AbstractAlreadyRegisteredException(String message) {
        super(message);
    }
}
