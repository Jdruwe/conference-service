package be.xplore.conference.consumer.processor;

import be.xplore.conference.consumer.api.ApiCaller;
import be.xplore.conference.consumer.api.dto.SpeakerResponse;
import be.xplore.conference.consumer.dto.SpeakerDto;
import be.xplore.conference.model.Speaker;
import be.xplore.conference.service.SpeakerService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SpeakerProcessor {

    private final SpeakerService speakerService;

    private final ApiCaller apiCaller;

    public SpeakerProcessor(SpeakerService speakerService,
                            ApiCaller apiCaller) {
        this.speakerService = speakerService;
        this.apiCaller = apiCaller;
    }

    List<Speaker> generateForTalk(List<SpeakerDto> speakerList) {
        return createSpeakers(speakerList);
    }

    private List<Speaker> createSpeakers(List<SpeakerDto> speakers) {
        return speakers.stream()
                .map(this::createSpeaker)
                .collect(Collectors.toList());
    }

    private Speaker createSpeaker(SpeakerDto dto) {
        String href = dto.getLink().getHref();
        String uuid = href.substring(href.lastIndexOf('/'));
        String etag = getSpeakerEtag(uuid);

        SpeakerResponse response = apiCaller.getSpeaker(uuid, etag);
        Speaker s = response.getSpeakerInformation().toDomain();
        s.setEtag(response.getEtag());
        return speakerService.save(s);
    }

    private String getSpeakerEtag(String uuid) {
        Optional<Speaker> optionalSpeaker = speakerService.loadById(uuid);
        if (optionalSpeaker.isPresent()) {
            return optionalSpeaker.get().getEtag();
        }
        return "";
    }

}
