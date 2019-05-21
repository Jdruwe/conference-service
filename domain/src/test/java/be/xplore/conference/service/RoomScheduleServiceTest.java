package be.xplore.conference.service;

import be.xplore.conference.exception.RoomScheduleNotFoundException;
import be.xplore.conference.model.DayOfWeek;
import be.xplore.conference.model.Room;
import be.xplore.conference.model.Talk;
import be.xplore.conference.model.schedule.RoomSchedule;
import be.xplore.conference.model.schedule.RoomScheduleId;
import be.xplore.conference.model.schedule.Schedule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class RoomScheduleServiceTest {

    @Autowired
    private RoomScheduleService service;

    @Autowired
    private RoomScheduleService roomScheduleService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private TalkService talkService;

    @Autowired
    private RoomService roomService;

    private final LocalDate date = LocalDate.of(2018, 11, 14);
    private Room room;

    @Before
    public void init() {
        Room room1 = initRoom();
        Schedule schedule = initSchedule();
        List<Talk> talks = initTalkList();
        RoomSchedule roomSchedule = new RoomSchedule(new RoomScheduleId(schedule, room1), "123-A", talks);
        roomScheduleService.save(roomSchedule);
    }

    @Test
    public void testLoadByDateAndRoom() throws RoomScheduleNotFoundException {
        RoomSchedule roomSchedule = service.loadByDateAndRoomId(date, room.getId())
                .orElseThrow(RoomScheduleNotFoundException::new);
        Assert.assertNotNull(roomSchedule);
        Assert.assertEquals(date, roomSchedule.getId().getSchedule().getDate());
    }

    @Test(expected = RoomScheduleNotFoundException.class)
    public void testLoadByDateAndRoomWithWrongRoom() throws RoomScheduleNotFoundException {
        RoomSchedule roomSchedule = service.loadByDateAndRoomId(date, "Room8000")
                .orElseThrow(RoomScheduleNotFoundException::new);
        Assert.assertNotNull(roomSchedule);
        Assert.assertEquals(date, roomSchedule.getId().getSchedule().getDate());
    }

    @Test
    public void testLoadByDayAndRoom() throws RoomScheduleNotFoundException {
        RoomSchedule roomSchedule = service.loadByDayAndRoomId(DayOfWeek.valueOf(date.getDayOfWeek().name()), room.getId())
                .orElseThrow(RoomScheduleNotFoundException::new);
        Assert.assertNotNull(roomSchedule);
        Assert.assertEquals(java.time.DayOfWeek.valueOf(date.getDayOfWeek().name()), roomSchedule.getId().getSchedule().getDate().getDayOfWeek());
    }


    @Test
    public void testSaveRoomSchedule() throws RoomScheduleNotFoundException {
        RoomSchedule roomSchedule = service.loadByDateAndRoomId(date, room.getId()).orElseThrow(RoomScheduleNotFoundException::new);
        roomSchedule.setEtag("testing");
        RoomSchedule savedRoomSchedule = service.save(roomSchedule);
        Assert.assertNotNull(savedRoomSchedule);
        Assert.assertEquals(roomSchedule.getEtag(), "testing");
    }

    private Room initRoom() {
        room = Room.builder()
                .id("Room10")
                .name("room 10")
                .capacity(105)
                .setup("conference").build();
        return roomService.save(room);
    }

    private Schedule initSchedule() {
        Schedule schedule = new Schedule(date, DayOfWeek.valueOf(String.valueOf(date.getDayOfWeek())));
        return scheduleService.save(schedule);
    }

    private List<Talk> initTalkList() {
        LocalDateTime localDateTime = LocalDateTime.now();
        Talk talkToSafe = Talk.builder()
                .id("DDD-7665")
                .startTime(localDateTime.minusDays(15))
                .endTime(localDateTime.minusDays(13))
                .fromTime("fromtime")
                .toTime("totime")
                .title("title")
                .type("type")
                .summary("summary")
                .speakers(null).build();
        Talk savedTalk = talkService.save(talkToSafe);
        List<Talk> talks = new ArrayList<>();
        talks.add(savedTalk);
        return talks;
    }
}

