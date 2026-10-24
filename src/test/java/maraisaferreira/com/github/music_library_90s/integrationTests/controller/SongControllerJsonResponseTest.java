package maraisaferreira.com.github.music_library_90s.integrationTests.controller;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import maraisaferreira.com.github.music_library_90s.contants.GlobalMessages;
import maraisaferreira.com.github.music_library_90s.dto.request.SaveNewSongRequestDto;
import maraisaferreira.com.github.music_library_90s.dto.response.PageResponseDto;
import maraisaferreira.com.github.music_library_90s.dto.response.SongResponseDto;
import maraisaferreira.com.github.music_library_90s.exceptions.BadRequestException;
import maraisaferreira.com.github.music_library_90s.exceptions.ResourceNotFoundException;
import maraisaferreira.com.github.music_library_90s.integrationTests.AbstractIntegrationTest;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

class SongControllerJsonResponseTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;

    @Autowired
    private Flyway flyway;

    private static final UUID VALID_UUID = UUID.fromString("7f3a2c91-6b84-4d17-9e52-c8a41f6b203d");
    public static final UUID INVALID_UUID = UUID.fromString("ff77b576-337b-4fe7-826a-2fa4895b7231");

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

        flyway.clean();
        flyway.migrate();
    }

    @Test
    void findAllSongsTest() {
        PageResponseDto<SongResponseDto> pageResponseDto = given(specification)
                .when().get("/songs")
                .then().statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract().body().as(new TypeRef<PageResponseDto<SongResponseDto>>() {
                });

        assertNotNull(pageResponseDto);
        assertEquals(4, pageResponseDto.content().size());
        assertEquals(4, pageResponseDto.totalElements());
        assertEquals(0, pageResponseDto.page());

        List<SongResponseDto> expectedList = getDatabaseExpectedList();

        var receivedList = pageResponseDto.content();

        assertTrue(receivedList.containsAll(expectedList));
        assertEquals(expectedList.size(), receivedList.size());
    }

    @Test
    void findSongByIdTest() {

        SongResponseDto responseDto = given(specification)
                .when().get("/songs/" + VALID_UUID)
                .then().statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract().body().as(SongResponseDto.class);

        SongResponseDto expectedDto = new SongResponseDto(VALID_UUID, "Viva Forever",
                1997, "Spice Girls", "Spiceworld", null, 9, null);

        assertNotNull(responseDto);
        assertEquals(expectedDto, responseDto);
    }

    @Test
    void findSongByIdWithExceptionTest() {

        ResourceNotFoundException exception = given(specification)
                .when().get("/songs/" + INVALID_UUID)
                .then().statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract().body().as(ResourceNotFoundException.class);

        assertNotNull(exception);
        assertEquals(GlobalMessages.RESOURCE_NOT_FOUND + INVALID_UUID , exception.getMessage());
    }

    @Test
    void findSongsByArtistTest() {
        var songsPage = given(specification)
                .pathParam("artist", "Spice Girls")
                .when().get("/songs/artist/{artist}")
                .then().statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract().body().as(new TypeRef<PageResponseDto<SongResponseDto>>() {});

        var expectedList = getDatabaseExpectedList();

        assertNotNull(songsPage);
        assertEquals(expectedList.size(), songsPage.content().size());

        assertTrue(expectedList.containsAll(songsPage.content()));
    }

    @Test
    void findSongsByAlbumTest() {
        var songsPage = given(specification)
                .pathParam("album", "Spice")
                .when().get("/songs/album/{album}")
                .then().statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract().body().as(new TypeRef<PageResponseDto<SongResponseDto>>() {});

        var expectedList = getDatabaseExpectedList().stream().filter(song ->
                song.album().equalsIgnoreCase("Spice")).toList();

        assertNotNull(songsPage);
        assertEquals(expectedList.size(), songsPage.content().size());

        assertTrue(expectedList.containsAll(songsPage.content()));
    }

    @Test
    void saveNewSongTest() {
        var responseDto = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SaveNewSongRequestDto(
                        "Wannabe", 1996, "Spice Girls", "Spice", 1))
                .when().post("/songs")
                .then().statusCode(HttpStatus.CREATED.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract().body().as(SongResponseDto.class);

        assertNotNull(responseDto);
        assertNotNull(responseDto.id());
        assertNotNull(responseDto.lyrics());
        assertEquals("Wannabe", responseDto.name());
        assertEquals("Spice Girls", responseDto.artist());
        assertEquals("Spice", responseDto.album());
        assertEquals(1996, responseDto.release());

    }

    @Test
    void saveNewSongExceptionTest(){
        var ex = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SaveNewSongRequestDto(
                        "Stop", 1997, "Spice Girls", "Spiceworld", 1))
                .when().post("/songs")
                .then().statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract().body().as(BadRequestException.class);

        assertNotNull(ex);
        assertInstanceOf(BadRequestException.class, ex);
        assertEquals(GlobalMessages.RESOURCE_ALREADY_EXIST, ex.getMessage());
    }

    @Test
    void deleteSongTest() {

        given(specification)
                .when().delete("/songs/" + VALID_UUID)
                .then().statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void deleteSongWithExceptionTest() {

        ResourceNotFoundException exception = given(specification)
                .when().delete("/songs/" + INVALID_UUID)
                .then().statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract().body().as(ResourceNotFoundException.class);
    }

    private static List<SongResponseDto> getDatabaseExpectedList() {
        return List.of(
                new SongResponseDto(UUID.fromString("e42b7c09-51af-46d3-8e78-9a2f64c1b507"), "2 Become 1", 1996,
                        "Spice Girls", "Spice", null, 3, null),
                new SongResponseDto(UUID.fromString("c19e5a74-2d63-48bf-a731-5f902ce84d16"), "Say You'll Be There", 1996,
                        "Spice Girls", "Spice", null, 2, null),
                new SongResponseDto(UUID.fromString("a6d8f241-93bc-4e57-b0c6-17e529fa83d2"), "Stop", 1997, "Spice Girls",
                        "Spiceworld", null, 2, null),
                new SongResponseDto(UUID.fromString("7f3a2c91-6b84-4d17-9e52-c8a41f6b203d"), "Viva Forever", 1997,
                        "Spice Girls", "Spiceworld", null, 9, null)
                );
    }
}