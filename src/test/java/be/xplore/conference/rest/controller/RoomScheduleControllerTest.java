package be.xplore.conference.rest.controller;

import be.xplore.conference.model.*;
import be.xplore.conference.service.RoomScheduleService;
import be.xplore.conference.service.RoomService;
import be.xplore.conference.service.ScheduleService;
import be.xplore.conference.service.TalkService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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

    private RoomSchedule roomSchedule;

    @Before
    public void init() {
        Room room1 = Room.builder()
                .id("room10")
                .name("room 10")
                .capacity(105)
                .setup("conference").build();
        roomService.save(room1);

        LocalDate date = LocalDate.of(2018, 11, 12);
        Schedule schedule = new Schedule(date, DayOfWeek.TUESDAY);
        scheduleService.save(schedule);

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

        roomSchedule = new RoomSchedule(new RoomScheduleId(schedule, room1), "123-A", talks);
        roomScheduleService.save(roomSchedule);
    }

    @Test
    public void testGetScheduleForRoom() throws Exception {
        RoomSchedule save = roomScheduleService.save(roomSchedule);
        log.error(save.toString());
        log.error("-----------------------------------------------");
        mockMvc.perform(get("/api/schedule/2018-11-12/Room10")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Room10")))
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
}
