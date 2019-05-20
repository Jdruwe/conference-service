package be.xplore.conference.converter.model;

import be.xplore.conference.dto.SlotDto;
import be.xplore.conference.dto.TalkDto;
import be.xplore.conference.model.Speaker;
import be.xplore.conference.model.Talk;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class TalkConverter {
    public static Talk toTalk(SlotDto slotDto, List<Speaker> speakers) {
        TalkDto talkDto = slotDto.getTalk();
        LocalDateTime startTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(slotDto.getFromTimeMillis()), ZoneId.systemDefault());
        LocalDateTime endTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(slotDto.getToTimeMillis()), ZoneId.systemDefault());
        return Talk.builder()
                .id(talkDto.getId())
                .startTime(startTime)
                .endTime(endTime)
                .fromTime(slotDto.getFromTime())
                .toTime(slotDto.getToTime())
                .title(talkDto.getTitle())
                .type(talkDto.getTalkType())
                .summary(talkDto.getSummary())
                .speakers(speakers)
                .build();
    }
}
