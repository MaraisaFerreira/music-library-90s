package maraisaferreira.com.github.music_library_90s.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import maraisaferreira.com.github.music_library_90s.contants.GlobalMessages;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"name", "album"})
@Entity
@Table(
        name = "songs",
        uniqueConstraints = @UniqueConstraint(name = "unq_name_and_album", columnNames = {
                "name", "album"
        })
)
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String name;

    @Min(value = 1990, message = GlobalMessages.LOWEST_YEAR_ALLOWED)
    @Max(value = 1990, message = GlobalMessages.BIGGER_YEAR_ALLOWED)
    @Column(nullable = false)
    private Integer release_year;

    @Column(nullable = false, length = 150)
    private String artist;

    @Column(nullable = false, length = 150)
    private String album;

    @Column(length = 500)
    private String coverAlbumUrl;

    @Min(value = 0, message = GlobalMessages.LOWEST_TRACK_VALUE)
    @Max(value = 255, message = GlobalMessages.BIGGER_TRACK_VALUE)
    private Integer track;

    @Column(columnDefinition = "TEXT")
    private String lyrics;

    public Song(UUID id, String name, Integer release_year, String artist,
                String album, Integer track) {
        this.id = id;
        this.name = name;
        this.release_year = release_year;
        this.artist = artist;
        this.album = album;
        this.track = track;
    }
}
