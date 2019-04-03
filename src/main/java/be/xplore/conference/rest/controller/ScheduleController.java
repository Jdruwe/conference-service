package be.xplore.conference.rest.controller;

import be.xplore.conference.excpetion.ScheduleNotFoundException;
import be.xplore.conference.model.Schedule;
import be.xplore.conference.rest.dto.ScheduleDto;
import be.xplore.conference.service.ScheduleService;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("api/schedule")
public class ScheduleController {

    private final ScheduleService service;
    private final ModelMapper modelMapper;

    public ScheduleController(ScheduleService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{date}/{roomId}")
    public ResponseEntity<ScheduleDto> getScheduleForRoom(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @PathVariable String roomId) throws ScheduleNotFoundException {
        Schedule schedule = service.loadByDateAndRoom(date, roomId).orElseThrow(ScheduleNotFoundException::new);
        return ResponseEntity.ok(modelMapper.map(schedule, ScheduleDto.class));
    }
}
