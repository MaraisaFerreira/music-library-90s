package maraisaferreira.com.github.music_library_90s.repositories;

import maraisaferreira.com.github.music_library_90s.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SongRepository extends JpaRepository<Song, UUID> {
}
