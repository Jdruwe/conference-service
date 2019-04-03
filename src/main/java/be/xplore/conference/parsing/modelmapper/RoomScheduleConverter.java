package be.xplore.conference.parsing.modelmapper;

import be.xplore.conference.model.Room;
import be.xplore.conference.model.Schedule;
import be.xplore.conference.rest.dto.RoomScheduleDto;
import be.xplore.conference.rest.dto.TalkDto;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class RoomScheduleConverter extends AbstractConverter<Schedule, RoomScheduleDto> {
    private final ModelMapper modelMapper;

    public RoomScheduleConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    protected RoomScheduleDto convert(Schedule source) {
        Room room = source.getRooms().get(0);
        return new RoomScheduleDto(
                room.getId(),
                room.getName(),
                source.getDate(),
                source.getDay(),
                room.getTalks()
                        .stream()
                        .map(t -> modelMapper.map(t, TalkDto.class))
                        .collect(Collectors.toList()));
    }
}
