-- CHECK DATABASE
CREATE DATABASE IF NOT EXISTS MapDB;

USE MapDB;

-- CLEAN PREVIOUS TABLES --
DROP TABLE IF EXISTS exampleTab;

-- CREATE TABLE --
CREATE TABLE exampleTab(
    X1 float,
    X2 float,
    X3 float
);

-- POPULATING TABLE ---
INSERT INTO exampleTab VALUES (1,2,0);
INSERT INTO exampleTab VALUES (0,1,-1);
INSERT INTO exampleTab VALUES (1,3,5);
INSERT INTO exampleTab VALUES (1,3,4);
INSERT INTO exampleTab VALUES (2,2,0);

COMMIT;

DROP TABLE IF EXISTS secondTab;

-- CREATE TABLE --
CREATE TABLE secondTab(
    X1 float,
    X2 float,
    X3 float,
    X5 float
);

-- POPULATING TABLE ---
INSERT INTO secondTab VALUES (2,1,7,0);
INSERT INTO secondTab VALUES (3,0,1,-1);
INSERT INTO secondTab VALUES (7,-3,2,5.3);
INSERT INTO secondTab VALUES (-5,1,3,3);
INSERT INTO secondTab VALUES (2,8,-5,0);
INSERT INTO secondTab VALUES (-1,1,1,3);
INSERT INTO secondTab VALUES (2,8,-5,1);

COMMIT;