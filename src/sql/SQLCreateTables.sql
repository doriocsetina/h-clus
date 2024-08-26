-- CREATE TABLE --

CREATE TABLE mapdb.exampleTab(
    X1 float,
    X2 float,
    X3 float,
)

-- POPULATING TABLE ---

INSERT INTO mapdb.exampleTab VALUES(1,2,0);
INSERT INTO mapdb.exampleTab VALUES(0,1,-1);
INSERT INTO mapdb.exampleTab VALUES(1,3,5);
INSERT INTO mapdb.exampleTab VALUES(1,3,4);
INSERT INTO mapdb.exampleTab VALUES(2,2,0);

COMMIT;