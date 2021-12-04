package tests;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;


public class TestWireMock {

    private static int PORT = 8097;
    private static String HOST = "localhost";
    private static String BASE_URL = HOST + ":" + PORT;
    private static String jsonUserScore = "{\"name\":\"Test user\", \"score\": 78}";
    private static String jsonUserCources = "[{\"name\":\"QA java\", \"price\": 15000 }, {\"name\":\"Java\", \"price\": 12000 }]";
    private static String jsonUserAll="{\"name\":\"Test user\",\n" +
            " \"cource\":\"QA\",\n" +
            "  \"email\":\"test@test.test\",\n" +
            "  \"age\": 23}";

    private static WireMockServer wireMockServer =
            new WireMockServer(WireMockConfiguration.options()
                    .extensions(new ResponseTemplateTransformer(false))
                    .port(PORT));

    @BeforeAll
    public static void beforeAll() {
        wireMockServer.start();
        configureFor(HOST, PORT);

        MappingBuilder getUserScore = get(urlMatching("/user/get/2"));
        ResponseDefinitionBuilder responseGetUserScore = aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(jsonUserScore);

        MappingBuilder getUserCources = get(urlMatching("/cource/get/all"));
        ResponseDefinitionBuilder responseGetUserCources = aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(jsonUserCources);

        MappingBuilder getUserAll=get(urlMatching("/user/get/all"));
        ResponseDefinitionBuilder responseGetUserAll=aResponse()
                .withStatus(200)
                .withHeader("Content-Type","application/json")
                .withBody(jsonUserAll);

        stubFor(getUserScore.willReturn(responseGetUserScore));
        stubFor(getUserCources.willReturn(responseGetUserCources));
        stubFor(getUserAll.willReturn(responseGetUserAll));
    }

    @AfterAll
    public static void afterAll() {
        wireMockServer.stop();
    }

    @Test
    @DisplayName("Get user sore by id /user/get/{ID}")
    void testUserScore() {
        String getUserUrl = "/user/get/" + 2;

        RestAssured.
                when()
                .get("http://" + BASE_URL + getUserUrl)
                .then()
                .contentType(ContentType.JSON)
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("score", is(78));

    }

    @Test
    @DisplayName("Get cources all /cource/get/all")
    void testUserCources() {
        String getUserUrl = "/cource/get/all";
        RestAssured.
                when()
                .get("http://" + BASE_URL + getUserUrl)
                .then()
                .contentType(ContentType.JSON)
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("size()", is(2))
                .body(containsString(jsonUserCources))
                .body(matchesJsonSchemaInClasspath("schema/AllCource.json"));
    }

    @Test
    @DisplayName("Get user all /user/get/all")
    void testUserAll() {
        String getUserUrl = "/user/get/all";
        RestAssured.
                when()
                .get("http://" + BASE_URL + getUserUrl)
                .then()
                .contentType(ContentType.JSON)
                .log().all()
                .assertThat()
                .statusCode(200)
                //.body("size()", is(2))
                .body(containsString(jsonUserAll))
                .body(matchesJsonSchemaInClasspath("schema/AllUser.json"));
    }
}




