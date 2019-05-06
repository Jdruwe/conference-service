package be.xplore.conference.consumer.api;

import be.xplore.conference.consumer.api.dto.ApiResponse;
import be.xplore.conference.consumer.api.dto.RoomScheduleResponse;
import be.xplore.conference.consumer.api.dto.RoomsResponse;
import be.xplore.conference.consumer.api.dto.SpeakerResponse;
import be.xplore.conference.consumer.api.util.ApiCallHelper;
import be.xplore.conference.consumer.dto.RoomsDto;
import be.xplore.conference.consumer.dto.ScheduleDto;
import be.xplore.conference.consumer.dto.SpeakerInformationDto;
import be.xplore.conference.consumer.property.DevoxxApiProperties;
import be.xplore.conference.model.DayOfWeek;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApiCaller {

    private static final Logger log = LoggerFactory.getLogger(ApiCaller.class);

    private final DevoxxApiProperties apiProperties;
    private final ApiCallHelper apiHelper;

    public ApiCaller(DevoxxApiProperties apiProperties, ApiCallHelper apiHelper) {
        this.apiProperties = apiProperties;
        this.apiHelper = apiHelper;
    }

    public RoomsResponse getRooms(String etag) {
        String url = apiProperties.getBaseUrl() + apiProperties.getRooms();
        try {
            ApiResponse response = apiHelper.queryApi(url, etag, RoomsDto.class);
            return new RoomsResponse(response.getETag(), (RoomsDto) response.getBody());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public RoomScheduleResponse getRoomSchedule(String roomId, String etag, DayOfWeek day) {
        String url = apiProperties.getBaseUrl() + apiProperties.getRooms() + roomId + "/" + day.name().toLowerCase();
        try {
            ApiResponse response = apiHelper.queryApi(url, etag, ScheduleDto.class);
            return new RoomScheduleResponse(response.getETag(), (ScheduleDto) response.getBody());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SpeakerResponse getSpeaker(String uuid, String etag) {
        String url = apiProperties.getBaseUrl() + apiProperties.getSpeaker() + uuid;
        try {
            ApiResponse response = apiHelper.queryApi(url, etag, SpeakerInformationDto.class);
            return new SpeakerResponse(response.getETag(), (SpeakerInformationDto) response.getBody());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
