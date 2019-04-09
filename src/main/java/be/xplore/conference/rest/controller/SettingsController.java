package be.xplore.conference.rest.controller;

import be.xplore.conference.model.Settings;
import be.xplore.conference.rest.dto.ChangeSettingsDto;
import be.xplore.conference.rest.dto.SettingsDto;
import be.xplore.conference.service.SettingsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/settings")
public class SettingsController {
    private SettingsService settingsService;

    private final String MINUTES_BEFORE_NEXT_SESSION = "minutesBeforeNextSession";
    private final String IS_ROOM_OCCUPANCY_ON = "isRoomOccupancyOn";

    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping
    public ResponseEntity<SettingsDto> getSettings() {
        SettingsDto settingsDto = SettingsDto.builder()
                .minutesBeforeNextSession(Integer.parseInt(settingsService.loadByKey(MINUTES_BEFORE_NEXT_SESSION).getValue()))
                .isRoomOccupancyOn(Boolean.parseBoolean(settingsService.loadByKey(IS_ROOM_OCCUPANCY_ON).getValue()))
                .build();
        return new ResponseEntity<>(settingsDto, HttpStatus.OK);
    }

    //TODO beautify
    @PutMapping
    public ResponseEntity<ChangeSettingsDto> changeSettings(@RequestBody ChangeSettingsDto changeSettingsDto) {
        Settings settings = settingsService.loadByKey(MINUTES_BEFORE_NEXT_SESSION);
        settings.setValue(String.valueOf(changeSettingsDto.getMinutesBeforeNextSession()));
        settingsService.save(settings);
        settings = settingsService.loadByKey(IS_ROOM_OCCUPANCY_ON);
        settings.setValue(String.valueOf(changeSettingsDto.isRoomOccupancyOn()));
        settingsService.save(settings);
        return new ResponseEntity<>(changeSettingsDto, HttpStatus.OK);
    }
}
