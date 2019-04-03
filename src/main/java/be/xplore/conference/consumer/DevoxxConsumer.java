package be.xplore.conference.consumer;

import be.xplore.conference.consumer.dto.RoomsDto;
import be.xplore.conference.consumer.dto.ScheduleDto;
import be.xplore.conference.parsing.ModelConverter;
import be.xplore.conference.model.DaysOfTheWeek;
import be.xplore.conference.model.Room;
import be.xplore.conference.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public DevoxxConsumer(ModelConverter modelConverter,
                          ObjectMapper objectMapper,
                          RoomService roomService) {
        this.modelConverter = modelConverter;
        this.objectMapper = objectMapper;
        this.roomService = roomService;
    }

    @PostConstruct
    // TODO fix postConstruct or scheduler for testing
    private void getRooms() throws IOException {
        String url = apiUrl + roomsUrl;
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        RoomsDto roomsDto = objectMapper.readValue(result, RoomsDto.class);
        List<Room> rooms = modelConverter.convertRooms(roomsDto);
        for (Room room : rooms) {
            roomService.save(room);
        }
        this.getScheduleForRoomForDay();
    }

    private void getScheduleForRoomForDay() throws IOException {
        List<Room> rooms = roomService.loadAll();
        for (DaysOfTheWeek day : DaysOfTheWeek.values()) {
            for (Room room : rooms) {
                String url = apiUrl + scheduleForDayForRoom + room.getId() + "/" + day.name().toLowerCase();
                RestTemplate restTemplate = new RestTemplate();
                String result = restTemplate.getForObject(url, String.class);
                ScheduleDto scheduleDto = objectMapper.readValue(result, ScheduleDto.class);
                modelConverter.convertSchedule(scheduleDto, day);

            }
        }
    }
}
