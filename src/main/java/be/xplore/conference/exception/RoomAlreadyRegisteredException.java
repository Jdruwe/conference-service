package be.xplore.conference.excpetion;

public class RoomAlreadyRegisteredException extends Exception {
    public RoomAlreadyRegisteredException(String message) {
        super(message);
    }
}
