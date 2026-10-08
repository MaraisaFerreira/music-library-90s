package maraisaferreira.com.github.music_library_90s.controller;

import lombok.RequiredArgsConstructor;
import maraisaferreira.com.github.music_library_90s.dto.response.SongResponseDto;
import maraisaferreira.com.github.music_library_90s.service.SongService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/songs")
public class SongController {

    private final SongService songService;

    @GetMapping("/{id}")
    public ResponseEntity<SongResponseDto> findSongById(@PathVariable UUID id){
        return ResponseEntity.ok(songService.findSongById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable UUID id){
        songService.deleteSong(id);
        return ResponseEntity.noContent().build();
    }
}
