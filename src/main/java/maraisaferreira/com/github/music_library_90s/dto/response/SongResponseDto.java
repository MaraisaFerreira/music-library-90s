package maraisaferreira.com.github.music_library_90s.dto.response;

import maraisaferreira.com.github.music_library_90s.model.Song;

public record SongResponseDto(
        String name,
        Integer release,
        String artist,
        String album,
        Integer track,
        String lyrics
) {
    public SongResponseDto(Song song) {
        this(
                song.getName(),
                song.getRelease_year(),
                song.getArtist(),
                song.getAlbum(),
                song.getTrack(),
                song.getLyrics()
        );
    }
}
