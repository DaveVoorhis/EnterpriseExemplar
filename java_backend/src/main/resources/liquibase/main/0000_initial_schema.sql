CREATE SEQUENCE roles_seq START WITH 10 INCREMENT BY 1;

CREATE TABLE roles (
    role_id INT NOT NULL PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    active BIT DEFAULT 1 NOT NULL
);

CREATE TABLE role_permissions (
    role_id INT,
    permission_name NVARCHAR(40),
    PRIMARY KEY(role_id, permission_name),
    FOREIGN KEY(role_id) REFERENCES roles(role_id) ON DELETE CASCADE
);

CREATE TABLE app_users (
	user_id int IDENTITY(1,1) NOT NULL PRIMARY KEY,
	email nvarchar(255) NOT NULL,
	enabled bit NOT NULL,
	last_login datetime NOT NULL
);

CREATE TABLE user_roles (
    user_id INT,
    role_id INT,
    PRIMARY KEY(user_id, role_id),
    FOREIGN KEY(user_id) REFERENCES app_users(user_id) ON DELETE CASCADE,
    FOREIGN KEY(role_id) REFERENCES roles(role_id)
);
