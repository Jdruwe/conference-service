package be.xplore.conference.rest.controller;

import be.xplore.conference.model.DayOfWeek;
import be.xplore.conference.model.Room;
import be.xplore.conference.model.RoomSchedule;
import be.xplore.conference.model.RoomScheduleId;
import be.xplore.conference.model.Schedule;
import be.xplore.conference.model.Talk;
import be.xplore.conference.service.RoomScheduleService;
import be.xplore.conference.service.RoomService;
import be.xplore.conference.service.ScheduleService;
import be.xplore.conference.service.TalkService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class RoomScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoomScheduleService roomScheduleService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private TalkService talkService;

    @Autowired
    private RoomService roomService;

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
    public void testGetScheduleForRoom() throws Exception {
        mockMvc.perform(get("/api/schedule/2018-11-12/Room10")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(room.getId())))
                .andExpect(content().string(containsString("2018")))
                .andExpect(content().string(containsString("11")))
                .andExpect(content().string(containsString("12")));
    }

    @Test
    public void testGetNonExistingScheduleForRoom() throws Exception {
        mockMvc.perform(get("/api/schedule/2012-11-12/Room87")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
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
        LocalDate date = LocalDate.of(2018, 11, 12);
        Schedule schedule = new Schedule(date, DayOfWeek.TUESDAY);
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
