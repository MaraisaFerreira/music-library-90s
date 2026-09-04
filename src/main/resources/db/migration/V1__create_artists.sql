CREATE TABLE artists (
     id BIGINT PRIMARY KEY AUTO_INCREMENT,
     members TEXT,
     isBand BOOLEAN NOT NULL,

     CONSTRAINT check_members
         CHECK (isBand = (members IS NOT NULL))
);