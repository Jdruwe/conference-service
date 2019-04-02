package be.xplore.conference.consumer;

import be.xplore.conference.consumer.dto.RoomsDto;
import be.xplore.conference.consumer.dto.ScheduleDto;
import be.xplore.conference.converter.ModelConverter;
import be.xplore.conference.model.DaysOfTheWeek;
import be.xplore.conference.model.Room;
import be.xplore.conference.service.RoomService;
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

    private ModelConverter modelConverter;
    private RoomService roomService;
    private final ObjectMapper objectMapper;

    public DevoxxConsumer(ModelConverter modelConverter, ObjectMapper objectMapper, RoomService roomService) {
        this.modelConverter = modelConverter;
        this.objectMapper = objectMapper;
        this.roomService = roomService;
    }

    @PostConstruct
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

    @PostConstruct
    private void getSchedule() throws IOException {
        for (DaysOfTheWeek day : DaysOfTheWeek.values()) {
            String url = apiUrl + scheduleUrl + "/" + day.name().toLowerCase();

            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(url, String.class);
            ScheduleDto schedule = objectMapper.readValue(result, ScheduleDto.class);
            // TODO complete or remove this
        }
    }
}
