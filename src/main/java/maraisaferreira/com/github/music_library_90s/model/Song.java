package maraisaferreira.com.github.music_library_90s.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Min(value = 1990, message = "The lowest year allowed is 1990.")
    @Max(value = 1990, message = "The biggest year allowed is 1999.")
    @Column(nullable = false)
    private Integer release_year;

    @Column(nullable = false, length = 150)
    private String artist;

    @Column(nullable = false, length = 150)
    private String album;

    private Integer track;

    @Column(columnDefinition = "TEXT")
    private String lyrics;
}
