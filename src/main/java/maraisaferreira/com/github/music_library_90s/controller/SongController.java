package maraisaferreira.com.github.music_library_90s.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import maraisaferreira.com.github.music_library_90s.dto.request.SaveNewSongRequestDto;
import maraisaferreira.com.github.music_library_90s.dto.response.PageResponseDto;
import maraisaferreira.com.github.music_library_90s.dto.response.SongResponseDto;
import maraisaferreira.com.github.music_library_90s.service.SongService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/songs")
public class SongController {

    private final SongService songService;

    @GetMapping
    public ResponseEntity<PageResponseDto<SongResponseDto>> findAllSongs(
            @PageableDefault(sort = {"name"}) Pageable pageable
    ) {
        return ResponseEntity.ok(songService.findAllSongs(pageable));
    }

    @GetMapping("/artist/{artist}")
    public ResponseEntity<PageResponseDto<SongResponseDto>> findSongsByArtist(
            @PathVariable String artist,
            @PageableDefault(sort = {"name"}) Pageable pageable
    ) {
        return ResponseEntity.ok(songService.findSongsByArtist(artist, pageable));
    }

    @GetMapping("/album/{album}")
    public ResponseEntity<PageResponseDto<SongResponseDto>> findSongsByAlbum(
            @PathVariable String album,
            @PageableDefault(sort = {"name"}) Pageable pageable
    ) {
        return ResponseEntity.ok(songService.findSongsByAlbum(album, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongResponseDto> findSongById(@PathVariable UUID id){
        return ResponseEntity.ok(songService.findSongById(id));
    }

    @PostMapping
    public ResponseEntity<SongResponseDto> saveNewSong(@Valid @RequestBody SaveNewSongRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(songService.saveNewSong(requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable UUID id){
        songService.deleteSong(id);
        return ResponseEntity.noContent().build();
    }
}
