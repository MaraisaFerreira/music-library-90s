CREATE TABLE musics (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    video_url VARCHAR(1000),
    track_number INT NOT NULL,
    lyrics TEXT,
    album_id BIGINT NOT NULL,

    CONSTRAINT fk_album
        FOREIGN KEY (album_id) REFERENCES albums(id)
);