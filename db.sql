
CREATE TABLE IF NOT EXISTS public.produkt_last_presi
(
    id serial NOT NULL,
    produkt_name character varying(300) COLLATE pg_catalog."default" NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_preis double precision NOT NULL,
    created timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT produkt_last_presi_pkey PRIMARY KEY (id)
)

CREATE TABLE IF NOT EXISTS public.rechnung
(
    id serial NOT NULL,
    nummer integer NOT NULL,
    rechnung_create character varying(10) COLLATE pg_catalog."default" NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lifer_date character varying(10) COLLATE pg_catalog."default" NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT rechnung_pkey PRIMARY KEY (id)
)

CREATE TABLE IF NOT EXISTS public.rechnung_item
(
    id serial NOT NULL,
    rechnung_id int NOT NULL,
    produkt character varying(300) COLLATE pg_catalog."default" NOT NULL DEFAULT CURRENT_TIMESTAMP,
    artikel_nummer character varying(300) COLLATE pg_catalog."default" NOT NULL DEFAULT CURRENT_TIMESTAMP,
    menge integer NOT NULL,
    preis double precision NOT NULL,
    created timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT rechnung_item_pkey PRIMARY KEY (id),
    CONSTRAINT rechnung_item_rechnung_fkey FOREIGN KEY (rechnung_id)
        REFERENCES public.rechnung (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)

