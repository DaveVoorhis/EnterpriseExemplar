CREATE SEQUENCE roles_seq START WITH 10;
CREATE SEQUENCE app_users_id START WITH 10;

CREATE TABLE roles (
    role_id INTEGER PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE role_permissions (
    role_id INTEGER,
    permission_name VARCHAR(40),
    PRIMARY KEY (role_id, permission_name),
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE
);

CREATE TABLE app_users (
    user_id INTEGER PRIMARY KEY NOT NULL,
    email VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL,
    last_login TIMESTAMP NOT NULL
);

CREATE TABLE user_roles (
    user_id INTEGER,
    role_id INTEGER,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES app_users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(role_id)
);
