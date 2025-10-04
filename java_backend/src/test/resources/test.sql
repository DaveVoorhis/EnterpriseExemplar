--
-- from user database
--
DELETE FROM user_roles;
DELETE FROM app_users;
DELETE FROM role_permissions;
DELETE FROM roles;

INSERT INTO roles (role_id, name, description, active)
VALUES (1, 'ADMIN', 'Administrator', 1),
       (2, 'USER', 'User', 1);

INSERT INTO role_permissions (role_id, permission_name)
VALUES (1, 'ADD_DEMO'),
       (1, 'ADMIN'),
       (1, 'CHECK_PERMISSION'),
       (1, 'DELETE_DEMO'),
       (1, 'FIND_USER'),
       (1, 'GET_ALL_DEMOS'),
       (1, 'GET_DEMO'),
       (1, 'UPDATE_DEMO'),
       (2, 'GET_ALL_DEMOS'),
       (2, 'ADD_DEMO');

INSERT INTO app_users (user_id, email, enabled, last_login)
VALUES (4, 'dave.voorhis@reldb.org', 1, CAST('2024-07-22T00:00:00.000' AS DateTime)), -- matches NOAUTH_SECURE_USERID
       (5, 'blah.blah@reldb.org', 1, CAST('2024-07-22T00:00:00.000' AS DateTime)),
       (6, 'zap.zot@reldb.org', 0, CAST('2024-07-22T00:00:00.000' AS DateTime)),
       (7, 'zaz.zoo@reldb.org', 1, CAST('2024-07-22T00:00:00.000' AS DateTime));

INSERT INTO user_roles (user_id, role_id)
VALUES (6, 1),
       (4, 1),
       (5, 2);

--
-- from demo database
--

DELETE FROM demo;

INSERT INTO demo (demo_id, name, address)
VALUES (1, 'demo1', 'here'),
       (2, 'demo2', 'there');

--
-- from extra database
--
