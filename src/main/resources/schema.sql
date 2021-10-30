CREATE TABLE IF NOT EXISTS STOCK (
    id VARCHAR(60) DEFAULT RANDOM_UUID() PRIMARY KEY,
    name VARCHAR(30),
    risk VARCHAR(30),
    weight FLOAT(4)
);

INSERT INTO STOCK(risk,weight,name) VALUES (2,0.2,'CAKE');
INSERT INTO STOCK(risk,weight,name) VALUES (2,0.5,'PZZA');
INSERT INTO STOCK(risk,weight,name) VALUES (2,0.3,'EAT');
INSERT INTO STOCK(risk,weight,name) VALUES (4,0.4,'CAKE');
INSERT INTO STOCK(risk,weight,name) VALUES (4,0.35,'PZZA');
INSERT INTO STOCK(risk,weight,name) VALUES (4,0.25,'EAT');
INSERT INTO STOCK(risk,weight,name) VALUES (6,0.65,'CAKE');
INSERT INTO STOCK(risk,weight,name) VALUES (6,0.2,'PZZA');
INSERT INTO STOCK(risk,weight,name) VALUES (6,0.15,'EAT');
INSERT INTO STOCK(risk,weight,name) VALUES (8,0.8,'CAKE');
INSERT INTO STOCK(risk,weight,name) VALUES (8,0.1,'PZZA');
INSERT INTO STOCK(risk,weight,name) VALUES (8,0.1,'EAT');
INSERT INTO STOCK(risk,weight,name) VALUES (10,1,'CAKE');
INSERT INTO STOCK(risk,weight,name) VALUES (10,0,'PZZA');
INSERT INTO STOCK(risk,weight,name) VALUES (10,0,'EAT');
