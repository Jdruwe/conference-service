package be.xplore.conference.processor;

import be.xplore.conference.converter.model.TalkConverter;
import be.xplore.conference.dto.SlotDto;
import be.xplore.conference.model.Speaker;
import be.xplore.conference.model.Talk;
import be.xplore.conference.service.TalkService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TalkProcessor {

    private final TalkService talkService;

    private final SpeakerProcessor speakerProcessor;

    public TalkProcessor(TalkService talkService,
                         SpeakerProcessor speakerProcessor) {
        this.talkService = talkService;
        this.speakerProcessor = speakerProcessor;
    }

    public List<Talk> process(List<SlotDto> slotDtoList) {
        List<Talk> talks = new ArrayList<>();
        if (Objects.nonNull(slotDtoList)) {
            talks = createTalks(slotDtoList);
        }
        return talks;
    }

    private List<Talk> createTalks(List<SlotDto> slotDtoList) {
        return slotDtoList.stream()
                .map(dto -> {
                    Optional<Talk> t = getTalkFromSlot(dto);
                    return t.map(talkService::save).orElse(null);
                })
                .collect(Collectors.toList());
    }

    private Optional<Talk> getTalkFromSlot(SlotDto slot) {
        if (Objects.nonNull(slot.getTalk())) {
            List<Speaker> speakers = speakerProcessor.generateForTalk(slot.getTalk().getSpeakers());
            return Optional.of(TalkConverter.toTalk(slot, speakers));
        }
        return Optional.empty();
    }

}
