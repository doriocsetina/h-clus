-- CLEAN PREVIOUS TABLES --

DROP TABLE IF EXISTS MapDB.exampleTab;

-- CREATE TABLE --

CREATE TABLE MapDB.exampleTab(
    X1 float,
    X2 float,
    X3 float
);

-- POPULATING TABLE ---

INSERT INTO MapDB.exampleTab VALUES (1,2,0);
INSERT INTO MapDB.exampleTab VALUES (0,1,-1);
INSERT INTO MapDB.exampleTab VALUES (1,3,5);
INSERT INTO MapDB.exampleTab VALUES (1,3,4);
INSERT INTO MapDB.exampleTab VALUES (2,2,0);

COMMIT; 