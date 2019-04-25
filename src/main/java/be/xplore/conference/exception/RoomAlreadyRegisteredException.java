package be.xplore.conference.exception;

// todo wotofok only usage in tests?
public class RoomAlreadyRegisteredException extends Exception {
    public RoomAlreadyRegisteredException(String message) {
        super(message);
    }
}
