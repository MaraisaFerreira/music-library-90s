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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SongServiceTest {

    @Mock
    private SongRepository songRepository;

    @InjectMocks
    private SongService songService;

    @BeforeEach
    void setUp() {
    }


    @Test
    void findSongByIdTest() {
        when(songRepository.findById(1L)).thenReturn(Optional.of(getSingleSong()));

        SongResponseDto responseDto = songService.findSongById(1L);

        assertNotNull(responseDto);
        assertEquals("Stop", responseDto.name());
        assertEquals(1997, responseDto.release());
        assertEquals("Spice Girls", responseDto.artist());
        assertEquals("Spiceworld", responseDto.album());
        assertEquals(2, responseDto.track());
    }

    @Test
    void findSongByIdExceptionTest() {
        Exception ex = assertThrows(ResourceNotFoundException.class, () -> {
            when(songRepository.findById(anyLong())).thenReturn(Optional.empty());
            songService.findSongById(1L);
        });

        assertInstanceOf(ResourceNotFoundException.class, ex);
        assertEquals(GlobalMessages.RESOURCE_NOT_FOUND + "1", ex.getMessage());
    }

    @Test
    void deleteSongTest() {
        when(songRepository.findById(1L)).thenReturn(Optional.of(getSingleSong()));

        songService.deleteSong(1L);

        verify(songRepository, times(1)).findById(anyLong());
        verify(songRepository, times(1)).delete(any(Song.class));
        verifyNoMoreInteractions(songRepository);
    }

    @Test
    void deleteSongExceptionTest() {
        Exception ex = assertThrows(ResourceNotFoundException.class, () -> {
            when(songRepository.findById(anyLong())).thenReturn(Optional.empty());
            songService.findSongById(1L);
        });

        assertInstanceOf(ResourceNotFoundException.class, ex);
        assertEquals(GlobalMessages.RESOURCE_NOT_FOUND + "1", ex.getMessage());
    }

    private Song getSingleSong(){
        return new Song(
                1L,
                "Stop",
                1997,
                "Spice Girls",
                "Spiceworld",
                2);
    }
}