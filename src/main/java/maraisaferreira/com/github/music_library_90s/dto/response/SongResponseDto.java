package maraisaferreira.com.github.music_library_90s.dto.response;

import maraisaferreira.com.github.music_library_90s.model.Song;

import java.util.UUID;

public record SongResponseDto(
        UUID id,
        String name,
        Integer release,
        String artist,
        String album,
        String coverAlbumUrl,
        Integer track,
        String lyrics
) {
    public SongResponseDto(Song song) {
        this(
                song.getId(),
                song.getName(),
                song.getRelease_year(),
                song.getArtist(),
                song.getAlbum(),
                song.getCoverAlbumUrl(),
                song.getTrack(),
                song.getLyrics()
        );
    }
}
