package be.xplore.conference.consumer;

import org.springframework.web.client.RestTemplate;

public class devoxxConsumer {

    final static String url = "https://dvbe18.confinabox.com/api";

    private static void getConfernceDays() {
        final String uri = url + "/conferences/dvbe18/schedules/wednesday";

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);

        System.out.println(result);
    }
}
