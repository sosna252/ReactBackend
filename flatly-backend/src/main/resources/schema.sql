DROP TABLE IF EXISTS public.bookings
CREATE TABLE public.bookings (
    id bigint NOT NULL,
    item_id bigint NOT NULL,
    start_date date NOT NULL,
    end_date date NOT NULL,
    name text NOT NULL,
    last_name text NOT NULL,
    email text NOT NULL,
    people integer NOT NULL
);
ALTER TABLE public.bookings OWNER TO postgres;

DROP SEQUENCE IF EXISTS public.bookings_id_seq
CREATE SEQUENCE public.bookings_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.bookings_id_seq OWNER TO postgres;
ALTER SEQUENCE public.bookings_id_seq OWNED BY public.bookings.id;


DROP TABLE IF EXISTS public.item_photos;
CREATE TABLE public.item_photos (
    id bigint NOT NULL,
    photo bytea NOT NULL,
    item_id integer NOT NULL
);
ALTER TABLE public.item_photos OWNER TO postgres;

DROP SEQUENCE IF EXISTS public.item_photos_id_seq;
CREATE SEQUENCE public.item_photos_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.item_photos_id_seq OWNER TO postgres;
ALTER SEQUENCE public.item_photos_id_seq OWNED BY public.item_photos.id;

DROP TABLE IF EXISTS public.items
CREATE TABLE public.items (
    id bigint NOT NULL,
    author_id bigint NOT NULL,
    start_date_time date NOT NULL,
    end_date_time date NOT NULL,
    title text NOT NULL,
    description text NOT NULL,
    room_number integer NOT NULL,
    price numeric NOT NULL,
    rating numeric NOT NULL,
    city text NOT NULL,
    address text NOT NULL,
    country text NOT NULL
);
ALTER TABLE public.items OWNER TO postgres;

DROP SEQUENCE IF EXISTS public.items_id_seq
CREATE SEQUENCE public.items_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.items_id_seq OWNER TO postgres;
ALTER SEQUENCE public.items_id_seq OWNED BY public.items.id;

DROP TABLE IF EXISTS public.users
CREATE TABLE public.users (
    id bigint NOT NULL,
    login text NOT NULL,
    password text NOT NULL,
    first_name text NOT NULL,
    last_name text NOT NULL,
    security_token uuid NOT NULL
);
ALTER TABLE public.users OWNER TO postgres;

DROP SEQUENCE IF EXISTS public.users_id_seq
CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.users_id_seq OWNER TO postgres;
ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;

ALTER TABLE ONLY public.bookings ALTER COLUMN id SET DEFAULT nextval('public.bookings_id_seq'::regclass);
ALTER TABLE ONLY public.items ALTER COLUMN id SET DEFAULT nextval('public.items_id_seq'::regclass);
ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


INSERT INTO public.users (id, login, password, first_name, last_name, security_token) VALUES (1, 'admin', 'admin', 'Adam', 'Abacki', '9fcbf4ff-5ea9-4027-ba82-5a7a7c59c156');

SELECT pg_catalog.setval('public.bookings_id_seq', 1, false);
SELECT pg_catalog.setval('public.items_id_seq', 1, false);
SELECT pg_catalog.setval('public.users_id_seq', 1, true);

ALTER TABLE ONLY public.bookings
    ADD CONSTRAINT bookings_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.items
    ADD CONSTRAINT items_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.bookings
    ADD CONSTRAINT bookings_item_id_fkey FOREIGN KEY (item_id) REFERENCES public.items(id);


ALTER TABLE ONLY public.items
    ADD CONSTRAINT items_author_id_fkey FOREIGN KEY (author_id) REFERENCES public.users(id);
