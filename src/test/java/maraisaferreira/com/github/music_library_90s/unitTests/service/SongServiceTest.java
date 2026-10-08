package maraisaferreira.com.github.music_library_90s.unitTests.service;

import maraisaferreira.com.github.music_library_90s.contants.GlobalMessages;
import maraisaferreira.com.github.music_library_90s.dto.response.SongResponseDto;
import maraisaferreira.com.github.music_library_90s.exceptions.ResourceNotFoundException;
import maraisaferreira.com.github.music_library_90s.model.Song;
import maraisaferreira.com.github.music_library_90s.repositories.SongRepository;
import maraisaferreira.com.github.music_library_90s.service.SongService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SongServiceTest {

    @Mock
    private SongRepository songRepository;

    @InjectMocks
    private SongService songService;

    private static final String UUID_STR = "00cca7f6-43eb-4e28-aefa-ae45e92e88a4";
    private UUID uuidTest;

    @BeforeEach
    void setUp() {
        uuidTest = UUID.fromString(UUID_STR);
    }


    @Test
    void findSongByIdTest() {
        UUID uuid = UUID.fromString(UUID_STR);
        when(songRepository.findById(uuid)).thenReturn(Optional.of(getSingleSong()));

        SongResponseDto responseDto = songService.findSongById(uuid);

        assertNotNull(responseDto);
        assertEquals("Stop", responseDto.name());
        assertEquals(1997, responseDto.release());
        assertEquals("Spice Girls", responseDto.artist());
        assertEquals("Spiceworld", responseDto.album());
        assertEquals((short) 2, responseDto.track());
    }

    @Test
    void findSongByIdExceptionTest() {
        Exception ex = assertThrows(ResourceNotFoundException.class, () -> {
            when(songRepository.findById(any())).thenReturn(Optional.empty());
            songService.findSongById(uuidTest);
        });

        assertInstanceOf(ResourceNotFoundException.class, ex);
        assertEquals(GlobalMessages.RESOURCE_NOT_FOUND + UUID_STR, ex.getMessage());
    }

    @Test
    void deleteSongTest() {

        when(songRepository.findById(uuidTest)).thenReturn(Optional.of(getSingleSong()));

        songService.deleteSong(uuidTest);

        verify(songRepository, times(1)).findById(any());
        verify(songRepository, times(1)).delete(any(Song.class));
        verifyNoMoreInteractions(songRepository);
    }

    @Test
    void deleteSongExceptionTest() {
        Exception ex = assertThrows(ResourceNotFoundException.class, () -> {
            when(songRepository.findById(any())).thenReturn(Optional.empty());
            songService.findSongById(uuidTest);
        });

        assertInstanceOf(ResourceNotFoundException.class, ex);
        assertEquals(GlobalMessages.RESOURCE_NOT_FOUND + UUID_STR, ex.getMessage());
    }

    private Song getSingleSong(){
        return new Song(
                UUID.randomUUID(),
                "Stop",
                1997,
                "Spice Girls",
                "Spiceworld",
                2);
    }
}