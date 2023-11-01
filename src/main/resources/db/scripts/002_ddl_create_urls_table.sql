CREATE TABLE urls (
    id              serial          PRIMARY KEY             NOT NULL,
    name            varchar(2085)   UNIQUE                  NOT NULL,
    code            varchar(100)    UNIQUE                  NOT NULL,
    site_id         integer         REFERENCES sites(id)    NOT NULL,
    call_number     bigint
);