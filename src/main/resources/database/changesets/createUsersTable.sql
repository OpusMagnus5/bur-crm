CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       uuid UUID UNIQUE NOT NULL,
                       version INTEGER,
                       password VARCHAR NOT NULL,
                       email VARCHAR UNIQUE NOT NULL,
                       first_name VARCHAR NOT NULL,
                       last_name VARCHAR NOT NULL,
                       roles VARCHAR NOT NULL,
                       last_login TIMESTAMP,
                       created_at TIMESTAMP NOT NULL,
                       modified_at TIMESTAMP,
                       created_by BIGINT NOT NULL REFERENCES users(id),
                       modified_by BIGINT REFERENCES users(id)
);

ALTER SEQUENCE users_id_seq INCREMENT BY 50;
CREATE UNIQUE INDEX users_id_idx ON users (id);
CREATE UNIQUE INDEX users_email_idx ON users (email);