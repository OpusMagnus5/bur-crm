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