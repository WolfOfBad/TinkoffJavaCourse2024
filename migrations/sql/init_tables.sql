CREATE TABLE IF NOT EXISTS chat
(
    id         bigint generated always as identity,
    tg_chat_id bigint NOT NULL,

    primary key (id),
    unique (tg_chat_id)
);

CREATE TABLE IF NOT EXISTS link
(
    id          bigint                   generated always as identity,
    text        text                     NOT NULL,

    last_update timestamp with time zone NOT NULL,

    primary key (id),
    unique (text)
);

CREATE TABLE IF NOT EXISTS chat_link
(
    chat_id bigint REFERENCES chat(id),
    link_id bigint REFERENCES link(id)
)
