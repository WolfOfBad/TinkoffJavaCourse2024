CREATE TABLE IF NOT EXISTS chat
(
    id         bigint generated always as identity,
    tg_chat_id bigint NOT NULL,

    primary key (id),
    unique (tg_chat_id)
);
CREATE INDEX tg_chat_id_index ON chat (tg_chat_id);

CREATE TABLE IF NOT EXISTS link
(
    id          bigint                   generated always as identity,
    uri         text                     NOT NULL,

    last_update timestamp with time zone NOT NULL,

    primary key (id),
    unique (uri)
);
CREATE INDEX uri_index ON link (uri);

CREATE TABLE IF NOT EXISTS chat_link
(
    chat_id bigint REFERENCES chat(id) NOT NULL,
    link_id bigint REFERENCES link(id) NOT NULL,

    unique (chat_id, link_id)
)
