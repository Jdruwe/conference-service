package be.xplore.conference;

import be.xplore.conference.api.ApiCaller;
import be.xplore.conference.api.dto.RoomsResponse;
import be.xplore.conference.dto.RoomsDto;
import be.xplore.conference.model.Room;
import be.xplore.conference.model.Settings;
import be.xplore.conference.processor.RoomProcessor;
import be.xplore.conference.processor.ScheduleProcessor;
import be.xplore.conference.property.SettingsProperties;
import be.xplore.conference.service.RoomService;
import be.xplore.conference.service.SettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class DevoxxConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DevoxxConsumer.class);

    private final SettingsProperties settingsProperties;

    private final RoomProcessor roomProcessor;
    private final ScheduleProcessor scheduleProcessor;

    private final RoomService roomService;
    private final SettingsService settingsService;

    private final ApiCaller apiCaller;

    public DevoxxConsumer(RoomService roomService,
                          SettingsService settingsService,
                          SettingsProperties settingsProperties,
                          RoomProcessor roomProcessor,
                          ScheduleProcessor scheduleProcessor,
                          ApiCaller apiCaller) {
        this.roomService = roomService;
        this.settingsService = settingsService;
        this.settingsProperties = settingsProperties;
        this.roomProcessor = roomProcessor;
        this.scheduleProcessor = scheduleProcessor;
        this.apiCaller = apiCaller;
    }

    @Scheduled(fixedRateString = "${settings.queryRefreshInterval}")
    public void consumeApi() {
        String etag = getRoomsEtag();
        RoomsDto dto = getRoomsFromApi(etag);

        List<Room> rooms = fillRooms(dto);
        scheduleProcessor.process(rooms);

        LOGGER.info("Done");
    }

    @PostConstruct
    public void fillSettings() {
        settingsService.save(new Settings("minutesBeforeNextSession", String.valueOf(settingsProperties.getMinutesBeforeNextSession())));
        settingsService.save(new Settings("isRoomOccupancyOn", String.valueOf(settingsProperties.getIsRoomOccupancyOn())));
        settingsService.save(new Settings("message", ""));
        settingsService.save(new Settings("showMessage", String.valueOf(false)));
        settingsService.save(new Settings("mailDelayForConnectionIssues", String.valueOf(settingsProperties.getMailDelayForConnectionIssues())));
    }

    private RoomsDto getRoomsFromApi(String etag) {
        RoomsResponse response = apiCaller.getRooms(etag);
        saveRoomsEtag(response.getEtag());
        return response.getRooms();
    }

    private List<Room> fillRooms(RoomsDto dto) {
        return Objects.nonNull(dto) ? roomProcessor.process(dto) : roomService.loadAll();
    }

    private String getRoomsEtag() {
        Optional<Settings> etagSetting = settingsService.loadByKey("roomsEtag");

        if (etagSetting.isPresent()) {
            return etagSetting.get().getValue();
        }
        return "";
    }

    private void saveRoomsEtag(String etag) {
        settingsService.save(new Settings("roomsEtag", etag));
    }
}
