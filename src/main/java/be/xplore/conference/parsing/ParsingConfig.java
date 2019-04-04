package be.xplore.conference.parsing;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParsingConfig {
//    private final RoomScheduleConverter roomScheduleConverter;
//
//    public ParsingConfig(RoomScheduleConverter roomScheduleConverter) {
//        this.roomScheduleConverter = roomScheduleConverter;
//    }

    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.addConverter(roomScheduleConverter);
        return modelMapper;
    }
}
