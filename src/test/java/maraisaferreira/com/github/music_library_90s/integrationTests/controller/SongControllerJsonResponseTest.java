package maraisaferreira.com.github.music_library_90s.integrationTests.controller;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import maraisaferreira.com.github.music_library_90s.dto.response.SongResponseDto;
import maraisaferreira.com.github.music_library_90s.integrationTests.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SongControllerJsonResponseTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;

    @BeforeAll
    static void beforeAll() {
        specification = new RequestSpecBuilder()
                .addHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
                .setBasePath("/api/v1")
                .addFilters(List.of(
                        new RequestLoggingFilter(LogDetail.ALL),
                        new ResponseLoggingFilter(LogDetail.ALL)
                ))
                .build();
    }

    @BeforeEach
    void setUp() {
        specification.port(port);
    }

    @Test
    void findSongById() {

        SongResponseDto responseDto = given(specification)
                .when().get("/songs/7f3a2c91-6b84-4d17-9e52-c8a41f6b203d")
                .then().statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract().body().as(SongResponseDto.class);

        SongResponseDto expectedDto = new SongResponseDto(UUID.fromString("7f3a2c91-6b84-4d17-9e52-c8a41f6b203d"),
                "Viva Forever", 1997, "Spice Girls", "Spiceworld", null, 9, null);

        assertNotNull(responseDto);
        assertEquals(expectedDto, responseDto);
    }

    @Test
    void deleteSong() {

        given(specification)
                .when().delete("/songs/7f3a2c91-6b84-4d17-9e52-c8a41f6b203d")
                .then().statusCode(HttpStatus.NO_CONTENT.value());
    }
}