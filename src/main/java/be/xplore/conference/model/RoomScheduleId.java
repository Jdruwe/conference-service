package be.xplore.conference.model;

import javax.persistence.Embeddable;

@Embeddable
public class RoomScheduleId {
    public Schedule schedule;
    public Room room;
}
