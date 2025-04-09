--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.4

-- Started on 2025-04-07 14:55:30

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 224 (class 1255 OID 16425)
-- Name: sp_actualizar_joya(integer, character varying, character varying, double precision, double precision, integer); Type: PROCEDURE; Schema: public; Owner: postgres
--

CREATE PROCEDURE public.sp_actualizar_joya(IN _joya_id integer, IN _nombre character varying, IN _material character varying, IN _peso double precision, IN _precio double precision, IN _stock integer)
    LANGUAGE plpgsql
    AS $$
BEGIN
    UPDATE joya
    SET nombre = _nombre,
        material = _material,
        peso = _peso,
        precio = _precio,
        stock = _stock
    WHERE joya_id = _joya_id;
END;
$$;


ALTER PROCEDURE public.sp_actualizar_joya(IN _joya_id integer, IN _nombre character varying, IN _material character varying, IN _peso double precision, IN _precio double precision, IN _stock integer) OWNER TO postgres;

--
-- TOC entry 226 (class 1255 OID 16427)
-- Name: sp_agregar_cliente(character varying, character varying, character varying); Type: PROCEDURE; Schema: public; Owner: postgres
--

CREATE PROCEDURE public.sp_agregar_cliente(IN _nombre character varying, IN _correo character varying, IN _telefono character varying)
    LANGUAGE plpgsql
    AS $$
BEGIN
    INSERT INTO cliente (nombre, correo, telefono)
    VALUES (_nombre, _correo, _telefono);
END;
$$;


ALTER PROCEDURE public.sp_agregar_cliente(IN _nombre character varying, IN _correo character varying, IN _telefono character varying) OWNER TO postgres;

--
-- TOC entry 223 (class 1255 OID 16424)
-- Name: sp_agregar_joya(character varying, character varying, double precision, double precision, integer); Type: PROCEDURE; Schema: public; Owner: postgres
--

CREATE PROCEDURE public.sp_agregar_joya(IN _nombre character varying, IN _material character varying, IN _peso double precision, IN _precio double precision, IN _stock integer)
    LANGUAGE plpgsql
    AS $$
BEGIN
    INSERT INTO joya (nombre, material, peso, precio, stock)
    VALUES (_nombre, _material, _peso, _precio, _stock);
END;
$$;


ALTER PROCEDURE public.sp_agregar_joya(IN _nombre character varying, IN _material character varying, IN _peso double precision, IN _precio double precision, IN _stock integer) OWNER TO postgres;

--
-- TOC entry 227 (class 1255 OID 16428)
-- Name: sp_eliminar_cliente(integer); Type: PROCEDURE; Schema: public; Owner: postgres
--

CREATE PROCEDURE public.sp_eliminar_cliente(IN _cliente_id integer)
    LANGUAGE plpgsql
    AS $$
BEGIN
    DELETE FROM cliente WHERE cliente_id = _cliente_id;
END;
$$;


ALTER PROCEDURE public.sp_eliminar_cliente(IN _cliente_id integer) OWNER TO postgres;

--
-- TOC entry 225 (class 1255 OID 16426)
-- Name: sp_eliminar_joya(integer); Type: PROCEDURE; Schema: public; Owner: postgres
--

CREATE PROCEDURE public.sp_eliminar_joya(IN _joya_id integer)
    LANGUAGE plpgsql
    AS $$
BEGIN
    DELETE FROM joya WHERE joya_id = _joya_id;
END;
$$;


ALTER PROCEDURE public.sp_eliminar_joya(IN _joya_id integer) OWNER TO postgres;

--
-- TOC entry 228 (class 1255 OID 16429)
-- Name: sp_registrar_venta(integer, integer, integer); Type: PROCEDURE; Schema: public; Owner: postgres
--

CREATE PROCEDURE public.sp_registrar_venta(IN _cliente_id integer, IN _joya_id integer, IN _cantidad integer)
    LANGUAGE plpgsql
    AS $$
DECLARE
    _stock_actual INT;
BEGIN
    SELECT stock INTO _stock_actual FROM joya WHERE joya_id = _joya_id;

    IF _stock_actual IS NULL THEN
        RAISE EXCEPTION 'La joya con ID % no existe', _joya_id;
    ELSIF _stock_actual < _cantidad THEN
        RAISE EXCEPTION 'Stock insuficiente. Disponible: %, solicitado: %', _stock_actual, _cantidad;
    ELSE
        INSERT INTO venta (cliente_id, joya_id, cantidad)
        VALUES (_cliente_id, _joya_id, _cantidad);

        UPDATE joya SET stock = stock - _cantidad WHERE joya_id = _joya_id;
    END IF;
END;
$$;


ALTER PROCEDURE public.sp_registrar_venta(IN _cliente_id integer, IN _joya_id integer, IN _cantidad integer) OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 220 (class 1259 OID 16397)
-- Name: cliente; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cliente (
    cliente_id integer NOT NULL,
    nombre character varying(100) NOT NULL,
    correo character varying(100),
    telefono character varying(20)
);


ALTER TABLE public.cliente OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16396)
-- Name: cliente_cliente_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.cliente_cliente_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.cliente_cliente_id_seq OWNER TO postgres;

--
-- TOC entry 4927 (class 0 OID 0)
-- Dependencies: 219
-- Name: cliente_cliente_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.cliente_cliente_id_seq OWNED BY public.cliente.cliente_id;


--
-- TOC entry 218 (class 1259 OID 16389)
-- Name: joya; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.joya (
    joya_id integer NOT NULL,
    nombre character varying(100) NOT NULL,
    material character varying(50),
    peso numeric(10,2),
    precio numeric(10,2),
    stock integer DEFAULT 0
);


ALTER TABLE public.joya OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 16388)
-- Name: joya_joya_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.joya_joya_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.joya_joya_id_seq OWNER TO postgres;

--
-- TOC entry 4928 (class 0 OID 0)
-- Dependencies: 217
-- Name: joya_joya_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.joya_joya_id_seq OWNED BY public.joya.joya_id;


--
-- TOC entry 222 (class 1259 OID 16404)
-- Name: venta; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.venta (
    venta_id integer NOT NULL,
    cliente_id integer NOT NULL,
    joya_id integer NOT NULL,
    cantidad integer NOT NULL,
    fecha date DEFAULT CURRENT_DATE
);


ALTER TABLE public.venta OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16403)
-- Name: venta_venta_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.venta_venta_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.venta_venta_id_seq OWNER TO postgres;

--
-- TOC entry 4929 (class 0 OID 0)
-- Dependencies: 221
-- Name: venta_venta_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.venta_venta_id_seq OWNED BY public.venta.venta_id;


--
-- TOC entry 4760 (class 2604 OID 16400)
-- Name: cliente cliente_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cliente ALTER COLUMN cliente_id SET DEFAULT nextval('public.cliente_cliente_id_seq'::regclass);


--
-- TOC entry 4758 (class 2604 OID 16392)
-- Name: joya joya_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.joya ALTER COLUMN joya_id SET DEFAULT nextval('public.joya_joya_id_seq'::regclass);


--
-- TOC entry 4761 (class 2604 OID 16407)
-- Name: venta venta_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venta ALTER COLUMN venta_id SET DEFAULT nextval('public.venta_venta_id_seq'::regclass);


--
-- TOC entry 4919 (class 0 OID 16397)
-- Dependencies: 220
-- Data for Name: cliente; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.cliente (cliente_id, nombre, correo, telefono) FROM stdin;
1	María González	maria.gonzalez@gmail.com	912345678
2	Carlos Pérez	carlos.perez@yahoo.com	911223344
3	Laura Martínez	laura.martinez@hotmail.com	913334455
4	José Ramírez	jose.ramirez@gmail.com	914445566
5	Ana Castillo	ana.castillo@gmail.com	915556677
6	Luis Soto	luis.soto@gmail.com	916667788
7	Camila Vega	camila.vega@gmail.com	917778899
8	Pedro Herrera	pedro.herrera@gmail.com	918889900
9	Valentina Rojas	valentina.rojas@gmail.com	919990011
10	Javier Morales	javier.morales@gmail.com	920001122
\.


--
-- TOC entry 4917 (class 0 OID 16389)
-- Dependencies: 218
-- Data for Name: joya; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.joya (joya_id, nombre, material, peso, precio, stock) FROM stdin;
4	Anillo de Oro 18K	Oro	3.50	180000.00	15
5	Collar de Plata	Plata	12.00	95000.00	10
6	Pulsera de Oro Blanco	Oro Blanco	7.20	145000.00	8
7	Aros de Diamante	Oro + Diamante	2.10	300000.00	5
8	Cadena de Acero Inoxidable	Acero	15.00	35000.00	20
9	Anillo de Compromiso	Platino	4.00	500000.00	3
10	Reloj Elegante	Oro + Cuero	25.00	250000.00	6
11	Broche de Perla	Plata + Perla	5.50	130000.00	9
12	Tobillera de Plata	Plata	9.30	72000.00	7
13	Colgante de Zafiro	Oro + Zafiro	4.80	210000.00	4
14	Anillo de Rubí	Oro + Rubí	3.20	275000.00	6
15	Pendientes de Esmeralda	Oro + Esmeralda	2.80	320000.00	4
16	Collar de Perlas	Perla + Plata	14.50	180000.00	5
17	Anillo de Acero	Acero Inoxidable	4.50	40000.00	12
18	Pulsera de Cuarzo Rosa	Cuarzo + Plata	6.00	98000.00	7
19	Aros de Cristal	Cristal	1.50	35000.00	20
20	Cadena de Titanio	Titanio	11.30	125000.00	10
21	Colgante de Amatista	Amatista + Plata	3.00	89000.00	8
3	Collar	plata	300.00	90.30	0
22	Pulsera de Cuero Trenzado	Cuero	8.20	45000.00	9
2	Anillo	Oro	200.00	390.00	79
26	platinooscuro	amatista	200.00	24500.00	12
\.


--
-- TOC entry 4921 (class 0 OID 16404)
-- Dependencies: 222
-- Data for Name: venta; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.venta (venta_id, cliente_id, joya_id, cantidad, fecha) FROM stdin;
1	2	3	20	2025-04-07
2	1	3	2	2025-04-07
3	3	3	2	2025-04-07
4	2	3	6	2025-04-07
5	1	2	200	2025-04-07
6	2	2	20	2025-04-07
7	1	2	1	2025-04-07
\.


--
-- TOC entry 4930 (class 0 OID 0)
-- Dependencies: 219
-- Name: cliente_cliente_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.cliente_cliente_id_seq', 10, true);


--
-- TOC entry 4931 (class 0 OID 0)
-- Dependencies: 217
-- Name: joya_joya_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.joya_joya_id_seq', 26, true);


--
-- TOC entry 4932 (class 0 OID 0)
-- Dependencies: 221
-- Name: venta_venta_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.venta_venta_id_seq', 7, true);


--
-- TOC entry 4766 (class 2606 OID 16402)
-- Name: cliente cliente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cliente
    ADD CONSTRAINT cliente_pkey PRIMARY KEY (cliente_id);


--
-- TOC entry 4764 (class 2606 OID 16395)
-- Name: joya joya_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.joya
    ADD CONSTRAINT joya_pkey PRIMARY KEY (joya_id);


--
-- TOC entry 4768 (class 2606 OID 16410)
-- Name: venta venta_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venta
    ADD CONSTRAINT venta_pkey PRIMARY KEY (venta_id);


--
-- TOC entry 4769 (class 2606 OID 16411)
-- Name: venta venta_cliente_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venta
    ADD CONSTRAINT venta_cliente_id_fkey FOREIGN KEY (cliente_id) REFERENCES public.cliente(cliente_id);


--
-- TOC entry 4770 (class 2606 OID 16416)
-- Name: venta venta_joya_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.venta
    ADD CONSTRAINT venta_joya_id_fkey FOREIGN KEY (joya_id) REFERENCES public.joya(joya_id);


-- Completed on 2025-04-07 14:55:31

--
-- PostgreSQL database dump complete
--

