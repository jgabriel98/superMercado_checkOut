CREATE DATABASE super_mercado;

CREATE TABLE if not exists produto
(
    id text NOT NULL
        constraint produto_pk primary key,
    preco INTEGER NOT NULL
);

CREATE TABLE if not exists promocao
(
    id        serial           NOT NULL
        constraint promocao_pk primary key,
    preco     INTEGER          NOT NULL,
    min_itens INTEGER          NOT NULL,
    produto_id   TEXT
        constraint promocao_item_fk references produto (id)
        on delete cascade
);