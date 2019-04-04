package be.xplore.conference.parsing.modelmapper;

public class RoomScheduleConverter {
        //extends AbstractConverter<Schedule, RoomScheduleDto>
//    private final ModelMapper modelMapper;
//
//    public RoomScheduleConverter(ModelMapper modelMapper) {
//        this.modelMapper = modelMapper;
//    }
//
//    @Override
//    protected RoomScheduleDto convert(Schedule source) {
//        Room room = source.getRooms().get(0);
//        return new RoomScheduleDto(
//                room.getId(),
//                room.getName(),
//                source.getDate(),
//                source.getDay(),
//                room.getTalks()
//                        .stream()
//                        .map(t -> modelMapper.map(t, TalkDto.class))
//                        .collect(Collectors.toList()));
//    }
}
