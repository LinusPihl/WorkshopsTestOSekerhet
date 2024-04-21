CREATE DATABASE joobyTest;
CREATE USER 'jooby' IDENTIFIED BY 'yrgoP4ssword';
GRANT CREATE, ALTER, DROP, INSERT, UPDATE, DELETE, SELECT, REFERENCES ON
joobyTest.* TO 'jooby';

-- user admin, password yrgoP4ssword
INSERT INTO user VALUES (DEFAULT, 'admin',
'$argon2i$v=19$m=16,t=2,p=1$MTIzNDU2Nzg5MDEyMzQ1NjA$LmFqTZeUWwqsnbZCS2E8XQ',
'admin');

INSERT INTO user (user, realname, password_hash) 
VALUES ('LinusPihl', 'Linus Pihl', '$argon2i$v=19$m=16,t=2,p=1$MTIzNDU2Nzg5MDEyMzQ1NjA$LmFqTZeUWwqsnbZCS2E8XQ');

