package be.xplore.conference.recordandplay;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.SingleRootFileSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.function.Consumer;

import static com.github.tomakehurst.wiremock.client.WireMock.recordSpec;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class WireMockRecord {
    private static final Logger LOG = LoggerFactory.getLogger(WireMockRecord.class);
    private static final int PORT = 9999;
    private static final String IP = "127.0.0.1";
    private static final long ONE_MB = 1024L * 1024L;

    /**
     * Start WireMock on port 9999. Configure WireMock to forward requests to https://dvbe18.confinabox.com. Start recording by sending requests to localhost:9999. You can make
     * any number of requests. They will all be recorded unless there are duplicates. Below there are 2 example recordings. The recordings can of course contain more complex code,
     * including loops.
     */
    public static void main(String[] args) {
        withServer(server -> {
            deletePreviousRecordings(server);

            server.startRecording(recordSpec()
                    .extractBinaryBodiesOver(ONE_MB)
                    .extractTextBodiesOver(ONE_MB)
                    .forTarget("https://dvbe18.confinabox.com")
                    .ignoreRepeatRequests()
                    .makeStubsPersistent(true));
            try {
                Scanner scanner = new Scanner(new File("record-url.txt"));
                while (scanner.hasNextLine()) {
                    new RestTemplate().getForEntity("http://localhost:9999/api" + scanner.nextLine(), String.class);
                }
            } catch (FileNotFoundException ignored) {
            }
            server.stopRecording();
        });
    }

    private static void withServer(Consumer<WireMockServer> consumer) {
        WireMockServer server = new WireMockServer(options()
                .bindAddress(IP)
                .port(PORT)
                .fileSource(new SingleRootFileSource(Paths.get("src", "test", "resources", "wiremock").toFile())));

        try {
            server.start();

            consumer.accept(server);
        } finally {
            server.stop();
        }
    }

    private static void deletePreviousRecordings(WireMockServer server) {
        LOG.info("Remove previous recordings");

        server.getStubMappings().forEach(server::removeStubMapping);
    }
}
