DROP PROCEDURE IF EXISTS customer_create_new;
/*PROCEDURE customer_create_new*/
CREATE OR REPLACE PROCEDURE customer_create_new(
    IN _cst_uuid customer.cst_uuid%TYPE,
    IN _cst_name customer.cst_name%TYPE,
    IN _cst_nip customer.cst_nip%TYPE,
    IN _cst_created_by customer.cst_created_by%TYPE
)
LANGUAGE plpgsql
AS $$
BEGIN

    INSERT INTO customer(cst_uuid, cst_name, cst_nip, cst_created_by)
    VALUES(_cst_uuid, _cst_name, _cst_nip, _cst_created_by);

END$$;

DROP PROCEDURE IF EXISTS customer_get_by_nip;
/*PROCEDURE customer_get_by_nip*/
CREATE OR REPLACE PROCEDURE customer_get_by_nip(
    IN _cst_nip customer.cst_nip%TYPE,
    OUT _cursor REFCURSOR
)
    LANGUAGE plpgsql
AS $$
BEGIN

    OPEN _cursor FOR
        SELECT cst_id, cst_version, cst_name, cst_nip
        FROM customer WHERE cst_nip = _cst_nip;

END$$;

DROP PROCEDURE IF EXISTS customer_get_page;
/*PROCEDURE customer_get_page*/
CREATE OR REPLACE PROCEDURE customer_get_page(
    IN _offset NUMERIC,
    IN _max NUMERIC,
    IN _cst_name customer.cst_name%TYPE,
    IN _cst_nip customer.cst_nip%TYPE,
    OUT _cursor REFCURSOR,
    OUT _total_customers BIGINT
)
LANGUAGE plpgsql
AS $$
BEGIN

    OPEN _cursor FOR
        SELECT cst_id, cst_name, cst_nip
        FROM customer
        WHERE to_tsvector('simple', cst_name) @@ (phraseto_tsquery('simple', COALESCE(_cst_name, cst_name))::text || ':*')::tsquery
            AND cst_nip::varchar like '%' || COALESCE(_cst_nip, cst_nip)::varchar || '%'
        ORDER BY cst_name
        OFFSET _offset
        LIMIT _max;

    SELECT count(cst_id)
    INTO _total_customers
    FROM customer
    WHERE to_tsvector('simple', cst_name) @@ (phraseto_tsquery('simple', COALESCE(_cst_name, cst_name))::text || ':*')::tsquery
        AND cst_nip::varchar like '%' || COALESCE(_cst_nip, cst_nip)::varchar || '%';

END$$;