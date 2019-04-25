package be.xplore.conference.excpetion;

public class RoomNotFoundException extends Exception {
    public RoomNotFoundException(String message){
        super(message);
    }
}
