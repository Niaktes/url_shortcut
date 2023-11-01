CREATE TABLE sites (
    id          serial          PRIMARY KEY     NOT NULL,
    domain      varchar(300)    UNIQUE          NOT NULL,
    login       varchar(100)    UNIQUE          NOT NULL,
    password    varchar(100)                    NOT NULL
);