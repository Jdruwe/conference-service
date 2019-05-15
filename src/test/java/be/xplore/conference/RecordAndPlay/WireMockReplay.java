package be.xplore.conference.RecordAndPlay;

import com.github.tomakehurst.wiremock.common.SingleRootFileSource;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class WireMockReplay {
    private static final Logger log = LoggerFactory.getLogger(WireMockReplay.class);
    private static final String baseUrl = "http://localhost:9999/api";

    @ClassRule
    public static final WireMockClassRule WIRE_MOCK = new WireMockClassRule(options()
            .fileSource(new SingleRootFileSource(Paths.get("src", "test", "resources", "wiremock").toFile()))
            .port(9999));

    @Test
    public void WireMockCanReplay() {
        try {
            Scanner scanner = new Scanner(new File("record-url.txt"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                new RestTemplate().getForEntity(baseUrl + line, String.class);
                ResponseEntity<String> entity = new RestTemplate().getForEntity(baseUrl + line, String.class);
                log.error(entity.toString());
                Assert.assertEquals(HttpStatus.OK, entity.getStatusCode());
                Assert.assertNotNull(entity.getBody());
            }
        } catch (FileNotFoundException ignored) {
        }
    }
}
