package be.xplore.conference.rest.controller;

import be.xplore.conference.model.Settings;
import be.xplore.conference.rest.dto.SettingsDto;
import be.xplore.conference.service.SettingsService;
import org.modelmapper.ModelMapper;
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

    @PatchMapping("{key}/{value}")
    public ResponseEntity<SettingsDto> changeSettings(@PathVariable String key, @PathVariable String value) {
        Settings settings = settingsService.loadByKey(key);
        settings.setValue(value);
        settingsService.save(settings);
        return new ResponseEntity<>(modelMapper.map(settings, SettingsDto.class), HttpStatus.OK);
    }
}
