package be.xplore.conference.exception;

public class RoomScheduleNotFoundException extends AbstractNotFoundException {
    public RoomScheduleNotFoundException() {
    }

    public RoomScheduleNotFoundException(String message) {
        super(message);
    }
}
