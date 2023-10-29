package hw5;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;


public abstract class AbstractTest {
    private static WireMockServer wireMockServer = new WireMockServer();
    private static String baseUrl;

    private static final Logger logger
            = LoggerFactory.getLogger(AbstractTest.class);

    @BeforeAll
    static void setUp() {
        baseUrl = "http://localhost:" + 8080;
        wireMockServer.start();
        configureFor("localhost", 8080);
        logger.info("WiremockServer starts");
    }

    @AfterAll
    static void finish() {
        wireMockServer.stop();
        logger.info("WiremockServer stops");
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

}
