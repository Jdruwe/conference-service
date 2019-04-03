package be.xplore.conference.consumer;

import be.xplore.conference.consumer.dto.RoomsDto;
import be.xplore.conference.consumer.dto.ScheduleDto;
import be.xplore.conference.converter.ModelConverter;
import be.xplore.conference.model.DaysOfTheWeek;
import be.xplore.conference.model.Room;
import be.xplore.conference.model.Schedule;
import be.xplore.conference.service.RoomService;
import be.xplore.conference.service.ScheduleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Component
public class DevoxxConsumer {
    @Value("${devoxx.api.url}")
    private String apiUrl;
    @Value("${devoxx.schedule.api.url}")
    private String scheduleUrl;
    @Value("${devoxx.rooms.api.url}")
    private String roomsUrl;
    @Value("${devoxx.scheduleForRoomForDay.api.url}")
    private String scheduleForDayForRoom;

    private ModelConverter modelConverter;
    private final ObjectMapper objectMapper;
    private RoomService roomService;
    private ScheduleService scheduleService;

    public DevoxxConsumer(ModelConverter modelConverter,
                          ObjectMapper objectMapper,
                          RoomService roomService,
                          ScheduleService scheduleService) {
        this.modelConverter = modelConverter;
        this.objectMapper = objectMapper;
        this.roomService = roomService;
        this.scheduleService = scheduleService;
    }

    @PostConstruct
    // TODO fix postConstruct or scheduler
    private void getRooms() throws IOException {
        String url = apiUrl + roomsUrl;
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        RoomsDto roomsDto = objectMapper.readValue(result, RoomsDto.class);
        List<Room> rooms = modelConverter.convertRooms(roomsDto);
        for (Room room : rooms) {
            roomService.save(room);
        }
    }

    private static final Logger log = LoggerFactory.getLogger(DevoxxConsumer.class);

    @PostConstruct
    private void getScheduleForRoomForDay() throws IOException {
        List<Room> rooms = roomService.loadAll();
        for (Room room : rooms) {
            for (DaysOfTheWeek day : DaysOfTheWeek.values()) {
                String url = apiUrl + scheduleForDayForRoom + room.getId() + "/" + day.name().toLowerCase();
                RestTemplate restTemplate = new RestTemplate();
                String result = restTemplate.getForObject(url, String.class);
                ScheduleDto scheduleDto = objectMapper.readValue(result, ScheduleDto.class);
                Schedule schedule = modelConverter.convertSchedule(scheduleDto, day);
                log.warn(schedule.toString());
                scheduleService.save(schedule);
                // TODO complete or remove this method
            }
        }
    }
}
