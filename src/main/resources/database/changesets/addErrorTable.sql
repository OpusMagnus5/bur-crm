CREATE TABLE error(
    err_uuid UUID,
    err_user_id BIGINT REFERENCES users(usr_id),
    err_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp,
    err_class VARCHAR NOT NULL,
    err_message VARCHAR,
    err_stacktrace TEXT NOT NULL,
    err_is_web BOOLEAN NOT NULL DEFAULT false
);