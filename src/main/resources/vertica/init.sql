CREATE TABLE hello_world
(
    id INTEGER
);

START TRANSACTION;
INSERT INTO hello_world(id) VALUES (1);
INSERT INTO hello_world(id) VALUES (2);
INSERT INTO hello_world(id) VALUES (3);
INSERT INTO hello_world(id) VALUES (4);
COMMIT;