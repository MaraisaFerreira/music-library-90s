package maraisaferreira.com.github.music_library_90s.service;

import maraisaferreira.com.github.music_library_90s.contants.GlobalMessages;
import maraisaferreira.com.github.music_library_90s.dto.request.SaveNewSongRequestDto;
import maraisaferreira.com.github.music_library_90s.dto.response.LrclibResponseDto;
import maraisaferreira.com.github.music_library_90s.dto.response.PageResponseDto;
import maraisaferreira.com.github.music_library_90s.dto.response.SongResponseDto;
import maraisaferreira.com.github.music_library_90s.exceptions.BadRequestException;
import maraisaferreira.com.github.music_library_90s.exceptions.ResourceNotFoundException;
import maraisaferreira.com.github.music_library_90s.model.Song;
import maraisaferreira.com.github.music_library_90s.repositories.SongRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Service
public class SongService {

    private final SongRepository songRepository;
    private final RestClient restClient;

    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
        this.restClient = RestClient.create("https://lrclib.net");
    }

    @Transactional(readOnly = true)
    public PageResponseDto<SongResponseDto> findAllSongs(Pageable pageable){
        Page<Song> songPage = songRepository.findAll(pageable);
        Page<SongResponseDto> dtoPage = songPage.map(SongResponseDto::new);
        return new PageResponseDto<>(dtoPage);
    }

    @Transactional(readOnly = true)
    public SongResponseDto findSongById(UUID id) {
        return new SongResponseDto(songRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(GlobalMessages.RESOURCE_NOT_FOUND + id)));
    }

    @Transactional
    public SongResponseDto saveNewSong(SaveNewSongRequestDto requestDto) {
        boolean alreadyExist  = songRepository.existsByNameAndAlbum(requestDto.name(), requestDto.album());
        if (alreadyExist) {
            throw new BadRequestException(GlobalMessages.RESOURCE_ALREADY_EXIST);
        }

        String lyric = getLyric(requestDto.name(), requestDto.artist(), requestDto.album());

        Song toSave = new Song(
                requestDto.name(),
                requestDto.releaseYear(),
                requestDto.artist(),
                requestDto.album(),
                requestDto.track()
                );

        toSave.setLyrics(lyric);

        Song saved = songRepository.save(toSave);
        return new SongResponseDto(saved);
    }

    @Transactional
    public void deleteSong(UUID id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(GlobalMessages.RESOURCE_NOT_FOUND + id));

        songRepository.delete(song);
    }

    public String getLyric(String name, String artist, String album){
        int maxAttempts = 3;
        int attempts = 0;

        LrclibResponseDto lrclibResponseDto = null;
        while (attempts < maxAttempts){
            try {
                lrclibResponseDto = restClient.get().uri(uriBuilder ->
                                uriBuilder.path("/api/get")
                                        .queryParam("track_name", name.replace(" ", "+"))
                                        .queryParam("artist_name", artist.replace(" ", "+"))
                                        .queryParam("album_name", album.replace(" ", "+"))
                                        .build())
                        .retrieve().body(LrclibResponseDto.class);
                if (lrclibResponseDto != null) return lrclibResponseDto.plainLyrics();
            } catch (Exception ex) {
                attempts++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return null;
    }
}
