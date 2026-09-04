CREATE TABLE albums (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    release_year INTEGER NOT NULL,
    tracks INT NOT NULL,
    cover VARCHAR(255) UNIQUE,
    artist_id BIGINT NOT NULL,

    CONSTRAINT fk_artist
        FOREIGN KEY (artist_id) REFERENCES artists(id),

    CONSTRAINT unique_title_and_artist
        UNIQUE (title, artist_id),

    CONSTRAINT check_release_year
        CHECK (release_year BETWEEN 1990 AND 1999),

    CONSTRAINT check_positive_tracks
        CHECK (tracks > 0)
);