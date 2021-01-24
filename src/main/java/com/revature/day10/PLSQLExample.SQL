DO
$$
DECLARE
	selected_film film%ROWTYPE;
	input_film_id film.film_id%TYPE := 100;
BEGIN
	SELECT * FROM film
	WHERE film_id = input_film_id
    INTO selected_film;

	IF NOT FOUND THEN
		RAISE NOTICE 'The film % could not be found', input_film_id;
	ELSE
		RAISE NOTICE 'The film title is %', selected_film.title;
	END IF;
END;
$$;

DO
$$
DECLARE
	v_film film%ROWTYPE;
	len_description VARCHAR(100);
BEGIN
	SELECT * FROM film
	WHERE film_id = 100
    INTO v_film;

	IF NOT FOUND THEN
		RAISE NOTICE 'Film Not Found';
	ELSE
		IF v_film.length > 0 AND v_film.length <= 50 THEN
			len_description := 'Short';
		ELSIF v_film.length > 50 and v_film.length < 120 THEN
			len_description := 'Medium';
		ELSIF v_film.length > 120 THEN
			len_description := 'Long';
		ELSE
			len_Description := 'N/A';
		END IF;

		RAISE NOTICE 'The % film is %', v_film.title, len_description;
	END IF;
END;
$$;

DO
$$
DECLARE
	rate			film.rental_rate%TYPE;
	price_segment	VARCHAR(50);
BEGIN
	--get the rental rate
	SELECT rental_rate INTO rate
	FROM film
	WHERE film_id = 100;

	--assign the price segment
	IF FOUND THEN
		CASE rate
			WHEN 0.99 THEN
				price_segment = 'Mass';
			WHEN 2.99 THEN
				price_segment = 'Mainstream';
			WHEN 4.99 THEN
				price_segment = 'High End';
			ELSE
				price_segment = 'Unspecified';
			END CASE;

			RAISE NOTICE '%', price_segment;
	END IF;
END;
$$;

DO
$$
DECLARE
	total_payment	NUMERIC;
	service_level VARCHAR(25);
BEGIN
	SELECT SUM(amount) INTO total_payment
	FROM Payment
	WHERE customer_id = 10;

	IF FOUND THEN
		CASE
			WHEN total_payment > 300 THEN
				service_level = 'Obsidian';
			WHEN total_payment > 200 THEN
				service_level = 'Platinum';
			WHEN total_payment > 100 THEN
				service_level = 'Gold';
			WHEN total_payment > 50 THEN
				service_level = 'Silver';
			ELSE
				service_level = 'Bronze';
		END CASE;

		RAISE NOTICE 'Service Level: %', service_level;
	ELSE
		RAISE NOTICE 'Customer Not Found';
	END IF;
END;
$$;

DO
$$
DECLARE
	n		INTEGER := 10;
	fib		INTEGER := 0;
	counter	INTEGER := 0;
	i		INTEGER := 0;
	j		INTEGER := 1;
BEGIN
	IF (n < 1) THEN
		fib := 0;
	END IF;
	LOOP
		EXIT WHEN counter = n;
		counter := counter + 1;
		SELECT j, i + j INTO i, j;
	END LOOP;
	fib := i;
	RAISE NOTICE '%', fib;
END;
$$;

DO
$$
DECLARE
	counter	INTEGER := 0;
BEGIN
	WHILE counter < 5 LOOP
		RAISE NOTICE 'Counter %', counter;
		counter := counter + 1;
	END LOOP;
END;
$$;

DO
$$
DECLARE
	f	RECORD;
BEGIN
	FOR f IN SELECT title, length
	FROM film
	ORDER BY length desc, title
	LIMIT 10

	LOOP
		RAISE NOTICE '%(% mins)', f.title, f.length;
	END LOOP;

	FOR f IN SELECT title, length
	FROM film
	ORDER BY length desc, title
	LIMIT 10

	LOOP
		RAISE NOTICE '%(% mins)', f.title, f.length;
	END LOOP;
END;
$$;

DO
$$
BEGIN
	FOR counter IN 1..5 LOOP
		RAISE NOTICE 'counter: %', counter;
	END LOOP;

	FOR counter IN REVERSE 5..1 LOOP
		RAISE NOTICE 'reverse counter: %', counter;
	END LOOP;
END;
$$;

CREATE OR REPLACE FUNCTION get_film_count(len_from int, len_to int)
    RETURNS int
    LANGUAGE plpgsql
    AS
$$
DECLARE
    film_count  INTEGER;
BEGIN
    SELECT COUNT(*)
    INTO film_count
    FROM film
    WHERE length BETWEEN len_from AND len_to;

    RETURN film_count;
END;
$$;

select get_film_count(40, len_to => 90);

CREATE OR REPLACE FUNCTION get_film_stat(
    OUT min_len INTEGER,
    OUT max_len INTEGER,
    OUT avg_len NUMERIC
)
LANGUAGE plpgsql
as $$
BEGIN
    SELECT
           MIN(length),
           MAX(length),
           AVG(length)::NUMERIC(5, 1)
    INTO min_len, max_len, avg_len
    FROM film;
END;
$$;

select get_film_stat();

CREATE OR REPLACE FUNCTION swap(
    INOUT x INTEGER,
    INOUT y INTEGER
)
LANGUAGE 'plpgsql'
AS $$
BEGIN
    SELECT x, y INTO y, x;
END;
$$;

select swap(10, 5);

do
$$
DECLARE
    rec RECORD;
    v_film_id INTEGER = 2000;
BEGIN
    SELECT film_id, title
    INTO STRICT rec
    FROM film
    WHERE film_id = v_film_id;

    EXCEPTION
        WHEN no_data_found THEN
            RAISE EXCEPTION 'film % not found', v_film_id;
END;
$$;

do
$$
    DECLARE
        rec RECORD;
        v_film_id INTEGER = 2000;
    BEGIN
        SELECT film_id, title
        INTO STRICT rec
        FROM film
        WHERE film_id = v_film_id;

    EXCEPTION
        WHEN no_data_found THEN
            RAISE EXCEPTION 'film % not found', v_film_id;
    END;
$$
LANGUAGE 'plpgsql';

DO
$$
DECLARE
    rec record;
BEGIN
    SELECT film_id, title
    INTO STRICT rec
    FROM film
    WHERE title LIKE 'A%';

    EXCEPTION
        WHEN no_data_found THEN
            RAISE EXCEPTION 'film % not found', 0;
        WHEN too_many_rows THEN
            RAISE EXCEPTION 'Search query returns too many rows';
END;
$$;

do
$$
    declare
        rec record;
        v_length int = 90;
    begin
        -- select a film
        select film_id, title
        into strict rec
        from film
        where length = v_length;

        -- catch exception
    exception
        when sqlstate 'P0002' then
            raise exception 'The film with length % not found', v_length;
        when sqlstate 'P0003' then
            raise exception 'The film with length % is not unique', v_length;
    end;
$$
language plpgsql;

CREATE OR REPLACE PROCEDURE create_user
    (
        new_user_name TEXT,
        new_password TEXT,
        new_first_name TEXT,
        new_last_name TEXT,
        new_ssn INT
    )
LANGUAGE plpgsql
AS
$$
    BEGIN
        INSERT INTO accounts (user_name, password, first_name, last_name, ssn)
        VALUES (new_user_name, new_password, new_first_name, new_last_name, new_ssn);
    END;
$$;

CREATE TABLE accounts
(
    user_name TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    ssn INT PRIMARY KEY
);

CREATE TABLE users
(
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    user_id SERIAL PRIMARY KEY
);

CREATE TABLE customers
(
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    ssn CHAR(11) NOT NULL UNIQUE,
    user_id INT PRIMARY KEY
);

CREATE OR REPLACE FUNCTION user_name_doesnt_exist(target_username VARCHAR)
RETURNS BOOLEAN
LANGUAGE plpgsql
AS
$$
    DECLARE
        rec RECORD;
    BEGIN
        SELECT username INTO rec FROM users WHERE username = target_username;
        IF NOT FOUND THEN
            RETURN true;
        ELSE
            RETURN false;
        end if;
    END;
$$;

SELECT user_name_doesnt_exist('Matthew');

SELECT username FROM users WHERE username = 'Matthew';


create or replace function get_film_titles(p_year integer)
    returns text as $$
declare
    titles text default '';
    rec_film   record;
    cur_films cursor(p_year integer)
        for select title, release_year
            from film
            where release_year = p_year;
begin
    -- open the cursor
    open cur_films(p_year);

    --  loop

loop
    -- fetch row into the film
fetch cur_films into rec_film;
-- exit when no more row to fetch
exit when not found;

    -- build the output
if rec_film.title like '%ful%' then
         titles := titles || ',' || rec_film.title || ':' || rec_film.release_year;
end if;
end loop;

-- close the cursor
close cur_films;

return titles;
end; $$

language plpgsql;

SELECT get_film_titles(2006);

SELECT * from film where release_year != 2006;

DROP TABLE IF EXISTS employees;

CREATE TABLE employees( id INT GENERATED ALWAYS AS IDENTITY, first_name VARCHAR(40) NOT NULL, last_name VARCHAR(40) NOT NULL, PRIMARY KEY(id) );

CREATE TABLE employee_audits ( id INT GENERATED ALWAYS AS IDENTITY, employee_id INT NOT NULL, last_name VARCHAR(40) NOT NULL, changed_on TIMESTAMP(6) NOT NULL );

INSERT INTO employees (first_name, last_name) VALUES ('John', 'Doe'); INSERT INTO employees (first_name, last_name) VALUES ('Lily', 'Bush');

CREATE OR REPLACE FUNCTION log_last_name_changes()
    RETURNS TRIGGER
    LANGUAGE PLPGSQL
AS
$$
BEGIN
    IF NEW.last_name <> OLD.last_name THEN
        INSERT INTO employee_audits(employee_id,last_name,changed_on)
        VALUES(OLD.id,OLD.last_name,now());
    END IF;

    RETURN NEW;
END;
$$;

CREATE TRIGGER last_name_changes
    BEFORE UPDATE
    ON employees
    FOR EACH ROW
EXECUTE PROCEDURE log_last_name_changes();

UPDATE employees SET last_name = 'Brown' WHERE ID = 2;

SELECT * FROM employees;

SELECT * FROM employee_audits;

CREATE OR REPLACE PROCEDURE register_user
(
    p_username text,
    p_password text,
    p_first_name text,
    p_last_name text,
    p_ssn_text text
)
    LANGUAGE 'plpgsql'
AS $$
DECLARE
    v_user_id integer;
BEGIN
    INSERT INTO users (username, password)
    VALUES(p_username, crypt(p_password, "gen_salt"('bf')));

    SELECT user_id INTO v_user_id FROM users WHERE username = p_username;

    INSERT INTO customers (first_name, last_name, ssn, user_id)
    VALUES(p_first_name, p_last_name, p_ssn_text, v_user_id);
END;
$$;

CREATE OR REPLACE FUNCTION public.ssn_exists(
    p_ssn_text text)
    RETURNS boolean
    LANGUAGE 'plpgsql'
AS $BODY$
DECLARE
    rec RECORD;
BEGIN
    SELECT ssn INTO rec FROM customers WHERE ssn = p_ssn_text;

    IF NOT FOUND THEN
        RETURN false;
    ELSE
        RETURN true;
    END IF;
END;
$BODY$;

CREATE OR REPLACE FUNCTION public.check_password
(p_username text,
 p_password text)
    RETURNS INT
    LANGUAGE 'plpgsql'
AS $BODY$
DECLARE v_user_id INT := -1;
BEGIN
    SELECT user_id INTO v_user_id FROM users WHERE username = p_username AND password = crypt(p_password, password);
    RETURN v_user_id;
END;
$BODY$;