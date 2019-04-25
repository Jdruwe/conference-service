package be.xplore.conference.parsing.converter.model;

import be.xplore.conference.consumer.dto.SlotDto;
import be.xplore.conference.consumer.dto.TalkDto;
import be.xplore.conference.model.Speaker;
import be.xplore.conference.model.Talk;

import java.util.Date;
import java.util.List;

public class TalkConverter {
    public static Talk toTalk(SlotDto slotDto, List<Speaker> speakers) {
        TalkDto talkDto = slotDto.getTalk();
        Date startTime = new Date(slotDto.getFromTimeMillis());
        Date endTime = new Date(slotDto.getToTimeMillis());
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
