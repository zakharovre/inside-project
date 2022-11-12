CREATE TABLE messages (
    id serial PRIMARY KEY,
    username varchar(20) NOT NULL,
    message varchar(200) NOT NULL,
    created_at timestamp NOT NULL
);

CREATE TABLE users (
    id serial PRIMARY KEY,
    username varchar(30) NOT NULL,
    password varchar(60) NOT NULL
);

CREATE TABLE roles (
    id serial PRIMARY KEY,
    role varchar(10)
);

CREATE TABLE user_role (
    user_id integer REFERENCES users(id),
    role_id integer REFERENCES roles(id),
    CONSTRAINT user_role_pk PRIMARY KEY (user_id, role_id)
);

INSERT INTO messages(username, message, created_at) VALUES
                            ('username1', 'message1', '2022-11-10 18:21'),
                            ('username2', 'message2', '2022-11-10 18:22'),
                            ('username3', 'message3', '2022-11-10 18:23'),
                            ('username4', 'message4', '2022-11-10 18:24'),
                            ('username5', 'message5', '2022-11-10 18:25'),
                            ('username6', 'message6', '2022-11-10 18:26'),
                            ('username7', 'message7', '2022-11-10 18:27'),
                            ('username8', 'message8', '2022-11-10 18:28'),
                            ('username9', 'message9', '2022-11-10 18:29'),
                            ('username10', 'message10', '2022-11-10 18:30');

INSERT INTO users (username, password) VALUES
                            ('admin', '$2a$04$LmjbLRC1mXLjVhCmzdGkeODncilsLTX8UD6VJL04h1hI3L2ozzm6q'),
                            ('username1', '$2a$04$EWB2RgoDtgF7uVd6K/.5CebfGovKobkD.LRsH2Xu.SkUMof6Yr84K');

INSERT INTO roles(role) VALUES ('USER'), ('ADMIN');

INSERT INTO user_role VALUES (1, 1), (1, 2), (2, 1);