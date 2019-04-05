package be.xplore.conference.parsing;

import be.xplore.conference.consumer.dto.RoomsDto;
import be.xplore.conference.consumer.dto.SlotDto;
import be.xplore.conference.consumer.dto.SpeakerInformationDto;
import be.xplore.conference.consumer.dto.TalkDto;
import be.xplore.conference.model.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ModelConverter {

    public List<Room> convertToRooms(RoomsDto roomsDto) {
        return roomsDto.getRooms()
                .stream()
                .map(dto -> new Room(dto.getId(), dto.getName(), dto.getCapacity(), dto.getSetup()))
                .collect(Collectors.toList());
    }

    public Schedule convertSchedule(LocalDate date, DayOfWeek dayOfWeek) {
        return new Schedule(date, dayOfWeek);
    }

    public Talk convertTalk(SlotDto slotDto, List<Speaker> speakers) {
        if (slotDto.getTalk() == null) {
            return null;
        }
        TalkDto talkDto = slotDto.getTalk();
        Date startTime = new Date(slotDto.getFromTimeMillis());
        Date endTime = new Date(slotDto.getToTimeMillis());
        return new Talk(
                talkDto.getId(),
                startTime,
                endTime,
                slotDto.getFromTime(),
                slotDto.getToTime(),
                talkDto.getTitle(),
                talkDto.getTalkType(),
                talkDto.getSummary(),
                speakers);
    }

    public List<Speaker> convertSpeakersDto(List<SpeakerInformationDto> speakerInformationDtos) {
        return speakerInformationDtos
                .stream()
                .map(dto -> new Speaker(
                        dto.getUuid(),
                        dto.getFirstName(),
                        dto.getLastName(),
                        dto.getAvatarURL(),
                        dto.getTwitter()))
                .collect(Collectors.toList());
    }
}
