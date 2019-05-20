package be.xplore.conference.api;

import be.xplore.conference.api.dto.ApiResponse;
import be.xplore.conference.api.dto.RoomScheduleResponse;
import be.xplore.conference.api.dto.RoomsResponse;
import be.xplore.conference.api.dto.SpeakerResponse;
import be.xplore.conference.api.util.ApiCallHelper;
import be.xplore.conference.dto.RoomsDto;
import be.xplore.conference.dto.ScheduleDto;
import be.xplore.conference.dto.SpeakerInformationDto;
import be.xplore.conference.model.DayOfWeek;
import be.xplore.conference.property.DevoxxApiProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApiCaller {

    private final DevoxxApiProperties apiProperties;
    private final ApiCallHelper apiHelper;

    public ApiCaller(DevoxxApiProperties apiProperties, ApiCallHelper apiHelper) {
        this.apiProperties = apiProperties;
        this.apiHelper = apiHelper;
    }

    public RoomsResponse getRooms(String etag) {
        try {
            String url = apiProperties.getBaseUrl() + apiProperties.getRooms();
            ApiResponse response = apiHelper.queryApi(url, etag, RoomsDto.class);
            return new RoomsResponse(response.getETag(), (RoomsDto) response.getBody());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public RoomScheduleResponse getRoomSchedule(String roomId, String etag, DayOfWeek day) {
        try {
            String url = apiProperties.getBaseUrl() + apiProperties.getRooms() + roomId + "/" + day.name().toLowerCase();
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
