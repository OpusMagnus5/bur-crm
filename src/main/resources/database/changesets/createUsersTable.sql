CREATE TABLE users (
                       usr_id BIGSERIAL PRIMARY KEY,
                       usr_uuid UUID UNIQUE NOT NULL,
                       usr_version INTEGER NOT NULL DEFAULT 0,
                       usr_password VARCHAR NOT NULL,
                       usr_email VARCHAR UNIQUE NOT NULL,
                       usr_first_name VARCHAR NOT NULL,
                       usr_last_name VARCHAR NOT NULL,
                       usr_roles VARCHAR NOT NULL,
                       usr_last_login TIMESTAMP,
                       usr_created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp,
                       usr_modified_at TIMESTAMP WITH TIME ZONE,
                       usr_created_by BIGINT NOT NULL,
                       usr_modified_by BIGINT
);

CREATE UNIQUE INDEX users_email_idx ON users (usr_email);