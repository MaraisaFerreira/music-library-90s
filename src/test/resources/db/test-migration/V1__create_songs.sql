CREATE TABLE songs (
       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
       name VARCHAR(150) NOT NULL,
       release_year INTEGER NOT NULL,
       artist VARCHAR(150) NOT NULL,
       album VARCHAR(150) NOT NULL,
       cover_album_url VARCHAR(500),
       track INT,
       lyrics TEXT,

       CONSTRAINT ch_valid_release
           CHECK (release_year BETWEEN 1990 AND 1999),

       CONSTRAINT ch_track_valid
           CHECK (track BETWEEN 0 AND 255),

       CONSTRAINT unq_name_and_album
           UNIQUE (name, album)
);