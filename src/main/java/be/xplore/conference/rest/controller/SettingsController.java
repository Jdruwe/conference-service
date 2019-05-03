package be.xplore.conference.rest.controller;

import be.xplore.conference.exception.SettingNotFoundException;
import be.xplore.conference.model.Settings;
import be.xplore.conference.rest.dto.SettingsDto;
import be.xplore.conference.service.SettingsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/settings")
public class SettingsController {
    private final SettingsService settingsService;

    private static final String MINUTES_BEFORE_NEXT_SESSION = "minutesBeforeNextSession";
    private static final String IS_ROOM_OCCUPANCY_ON = "isRoomOccupancyOn";

    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping
    public ResponseEntity<SettingsDto> getSettings() {
        SettingsDto settingsDto = buildDto();
        return new ResponseEntity<>(settingsDto, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<SettingsDto> changeSettings(@RequestBody SettingsDto settingsDto) {
        Settings minutesBeforeNextSession = settingsService.update(
                MINUTES_BEFORE_NEXT_SESSION,
                String.valueOf(settingsDto.getMinutesBeforeNextSession()));
        Settings roomOccupancy = settingsService.update(
                IS_ROOM_OCCUPANCY_ON,
                String.valueOf(settingsDto.isRoomOccupancyOn()));

        settingsDto.setMinutesBeforeNextSession(Integer.parseInt(minutesBeforeNextSession.getValue()));
        settingsDto.setRoomOccupancyOn(Boolean.parseBoolean(roomOccupancy.getValue()));

        return new ResponseEntity<>(settingsDto, HttpStatus.OK);
    }

    private SettingsDto buildDto() {
        return SettingsDto.builder()
                .minutesBeforeNextSession(
                        Integer.parseInt(settingsService.loadByKey(MINUTES_BEFORE_NEXT_SESSION)
                                .orElseThrow(SettingNotFoundException::new)
                                .getValue()))
                .isRoomOccupancyOn(
                        Boolean.parseBoolean(settingsService.loadByKey(IS_ROOM_OCCUPANCY_ON)
                                .orElseThrow(SettingNotFoundException::new)
                                .getValue()))
                .build();
    }
}
