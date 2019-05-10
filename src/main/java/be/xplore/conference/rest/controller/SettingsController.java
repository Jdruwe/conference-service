package be.xplore.conference.rest.controller;

import be.xplore.conference.exception.SettingNotFoundException;
import be.xplore.conference.model.Settings;
import be.xplore.conference.rest.dto.MainSettingsDto;
import be.xplore.conference.rest.dto.NotificationsDto;
import be.xplore.conference.rest.dto.SettingsDto;
import be.xplore.conference.service.SettingsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/settings")
public class SettingsController {
    private static final String MINUTES_BEFORE_NEXT_SESSION = "minutesBeforeNextSession";
    private static final String IS_ROOM_OCCUPANCY_ON = "isRoomOccupancyOn";
    private static final String SHOW_MESSAGE = "showMessage";
    private static final String MESSAGE = "message";

    private final SettingsService settingsService;

    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping
    public ResponseEntity<SettingsDto> getSettings() {
        SettingsDto settingsDto = buildDto();
        return new ResponseEntity<>(settingsDto, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<MainSettingsDto> changeSettings(@RequestBody MainSettingsDto dto) {
        Settings minutesBeforeNextSession = settingsService.update(
                MINUTES_BEFORE_NEXT_SESSION,
                String.valueOf(dto.getMinutesBeforeNextSession()));
        Settings roomOccupancy = settingsService.update(
                IS_ROOM_OCCUPANCY_ON,
                String.valueOf(dto.isRoomOccupancyOn()));

        dto.setMinutesBeforeNextSession(Integer.parseInt(minutesBeforeNextSession.getValue()));
        dto.setRoomOccupancyOn(Boolean.parseBoolean(roomOccupancy.getValue()));

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/notifications")
    public ResponseEntity<NotificationsDto> changeNotifications(@RequestBody NotificationsDto dto) {
        Settings message = settingsService.update(
                MESSAGE, dto.getMessage());
        Settings showMessage = settingsService.update(
                SHOW_MESSAGE,
                String.valueOf(dto.isShowMessage()));

        dto.setMessage(message.getValue());
        dto.setShowMessage(Boolean.parseBoolean(showMessage.getValue()));

        return new ResponseEntity<>(dto, HttpStatus.OK);
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
                .showMessage(Boolean.parseBoolean(settingsService.loadByKey(SHOW_MESSAGE)
                        .orElseThrow(SettingNotFoundException::new)
                        .getValue()))
                .message(settingsService.loadByKey(MESSAGE)
                        .orElseThrow(SettingNotFoundException::new)
                        .getValue())
                .build();
    }
}
