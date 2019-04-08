package be.xplore.conference.rest.controller;

import be.xplore.conference.model.Settings;
import be.xplore.conference.rest.dto.ChangeSettingsDto;
import be.xplore.conference.rest.dto.SettingsDto;
import be.xplore.conference.service.SettingsService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/settings")
public class SettingsController {
    private final ModelMapper modelMapper;
    private SettingsService settingsService;

    public SettingsController(ModelMapper modelMapper, SettingsService settingsService) {
        this.modelMapper = modelMapper;
        this.settingsService = settingsService;
    }

    @GetMapping
    public ResponseEntity<List<SettingsDto>> getSettings() {
        List<SettingsDto> settingsDtos = settingsService.loadAll()
                .stream()
                .map(setting -> modelMapper.map(setting, SettingsDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(settingsDtos, HttpStatus.OK);
    }

    private static final Logger log = LoggerFactory.getLogger(SettingsController.class);

    @PutMapping
    public ResponseEntity<ChangeSettingsDto> changeSettings(@RequestBody ChangeSettingsDto changeSettingsDto) {
        log.info("==================================");
        log.info(String.valueOf(changeSettingsDto.getMinutesBeforeNextSession()));
        log.info(String.valueOf(changeSettingsDto.isRoomOccupancyOn()));
        log.info("==================================");
        return null;
        //return new ResponseEntity<>(modelMapper.map(settings, SettingsDto.class), HttpStatus.OK);
    }
}
