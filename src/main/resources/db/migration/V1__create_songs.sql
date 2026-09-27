CREATE TABLE songs (
       id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
       name VARCHAR(150) NOT NULL,
       release_year INTEGER NOT NULL,
       artist VARCHAR(150) NOT NULL,
       album VARCHAR(100) NOT NULL,
       track INT,
       lyric TEXT,

       CONSTRAINT ch_valid_release
           CHECK (release_year BETWEEN 1990 AND 1999),

       CONSTRAINT unq_name_and_album
           UNIQUE (name, album)
);