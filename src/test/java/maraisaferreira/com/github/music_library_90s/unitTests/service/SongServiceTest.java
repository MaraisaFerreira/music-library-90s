package maraisaferreira.com.github.music_library_90s.unitTests.service;

import maraisaferreira.com.github.music_library_90s.contants.GlobalMessages;
import maraisaferreira.com.github.music_library_90s.dto.request.SaveNewSongRequestDto;
import maraisaferreira.com.github.music_library_90s.dto.response.PageResponseDto;
import maraisaferreira.com.github.music_library_90s.dto.response.SongResponseDto;
import maraisaferreira.com.github.music_library_90s.exceptions.BadRequestException;
import maraisaferreira.com.github.music_library_90s.exceptions.ResourceNotFoundException;
import maraisaferreira.com.github.music_library_90s.model.Song;
import maraisaferreira.com.github.music_library_90s.repositories.SongRepository;
import maraisaferreira.com.github.music_library_90s.service.SongService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
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
    void findAllSongsTest() {
        Pageable pageable = PageRequest.of(0, 4);
        Page<Song> songPage = new PageImpl<>(getSongList(), pageable, pageable.getPageSize());

        when(songRepository.findAll(pageable)).thenReturn(songPage);

        PageResponseDto<SongResponseDto> pageResponseDto = songService.findAllSongs(pageable);

        List<SongResponseDto> expectedList = getSongList().stream()
                .map(SongResponseDto::new).toList();

        assertNotNull(pageResponseDto);
        assertEquals(expectedList.size(), pageResponseDto.content().size());
        assertEquals(expectedList, pageResponseDto.content());
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
    void saveNewSongTest() {
        Song saved = getSingleSong();
        UUID uuid = UUID.randomUUID();
        saved.setId(uuid);
        when(songRepository.save(getSingleSong())).thenReturn(saved);

        SongResponseDto responseDto = songService.saveNewSong(new SaveNewSongRequestDto(
                "Stop", 1997, "Spice Girls", "Spiceworld", 2));

        assertNotNull(responseDto);
        assertEquals(uuid, responseDto.id());
        assertEquals("Stop", responseDto.name());
        assertEquals(1997, responseDto.release());
        assertEquals("Spice Girls", responseDto.artist());
        assertEquals("Spiceworld", responseDto.album());
        assertEquals(2, responseDto.track());
    }

    @Test
    void saveNewSongExceptionTest(){
        when(songRepository.existsByNameAndAlbum("Stop", "Spiceworld")).thenReturn(true);

        Exception ex = assertThrows(BadRequestException.class, () -> {
            songService.saveNewSong(new SaveNewSongRequestDto("Stop", 1997, "Spice Girls", "Spiceworld", 2));
        });

        assertInstanceOf(BadRequestException.class, ex);
        assertEquals(GlobalMessages.RESOURCE_ALREADY_EXIST, ex.getMessage());
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
                "Stop",
                1997,
                "Spice Girls",
                "Spiceworld",
                2);
    }

    private List<Song> getSongList() {
        List<Song> songList = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            int number = i + 1;
            songList.add(new Song(
                    "Stop_" + number,
                    1997,
                    "Spice Girls",
                    "Spiceworld",
                    2)
            );
        }

        return songList;
    }
}