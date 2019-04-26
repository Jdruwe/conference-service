package be.xplore.conference.exception;

public class RoomAlreadyRegisteredException extends AbstractAlreadyRegisteredException {
    public RoomAlreadyRegisteredException() {
    }

    public RoomAlreadyRegisteredException(String message) {
        super(message);
    }
}
