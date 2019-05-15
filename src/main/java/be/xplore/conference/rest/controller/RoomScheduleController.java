package be.xplore.conference.rest.controller;

import be.xplore.conference.exception.RoomScheduleNotFoundException;
import be.xplore.conference.model.Room;
import be.xplore.conference.model.RoomSchedule;
import be.xplore.conference.model.Schedule;
import be.xplore.conference.rest.dto.RoomDto;
import be.xplore.conference.rest.dto.RoomScheduleDto;
import be.xplore.conference.rest.dto.TalkDto;
import be.xplore.conference.service.RoomScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/schedule")
@Slf4j
public class RoomScheduleController {
    private final RoomScheduleService service;
    private final ModelMapper modelMapper;

    public RoomScheduleController(RoomScheduleService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{date}/{roomId}")
    public ResponseEntity<RoomScheduleDto> getScheduleForRoom(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @PathVariable String roomId) throws RoomScheduleNotFoundException {

        RoomSchedule roomSchedule = service.loadByDateAndRoomId(date, roomId).orElseThrow(RoomScheduleNotFoundException::new);
        Schedule schedule = roomSchedule.getId().getSchedule();
        Room room = roomSchedule.getId().getRoom();
        RoomScheduleDto dto = new RoomScheduleDto(
                schedule.getDate(),
                schedule.getDay(),
                modelMapper.map(room, RoomDto.class),
                roomSchedule.getTalks()
                        .stream()
                        .map(t -> modelMapper.map(t, TalkDto.class))
                        .collect(Collectors.toList()));
        return ResponseEntity.ok(dto);
    }
}
