package maraisaferreira.com.github.music_library_90s.service;

import lombok.RequiredArgsConstructor;
import maraisaferreira.com.github.music_library_90s.contants.GlobalMessages;
import maraisaferreira.com.github.music_library_90s.dto.response.SongResponseDto;
import maraisaferreira.com.github.music_library_90s.exceptions.ResourceNotFoundException;
import maraisaferreira.com.github.music_library_90s.model.Song;
import maraisaferreira.com.github.music_library_90s.repositories.SongRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SongService {

    private final SongRepository songRepository;

    public SongResponseDto findSongById(Long id) {
        return new SongResponseDto(songRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(GlobalMessages.RESOURCE_NOT_FOUND + id)));
    }

    public void deleteSong(Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(GlobalMessages.RESOURCE_NOT_FOUND + id));

        songRepository.delete(song);
    }
}
