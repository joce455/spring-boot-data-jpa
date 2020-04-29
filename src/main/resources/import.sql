INSERT INTO clientes (id,nombre,apellido,email,create_at,foto) VALUES (1,'Andres','Guzman','profesor@gmail.com','2017-08-28','');
INSERT INTO clientes (id,nombre,apellido,email,create_at,foto) VALUES (2,'John','Perez1','alumnojohn@gmail.com','2017-08-28','');
INSERT INTO clientes (id,nombre,apellido,email,create_at,foto) VALUES (3,'John','Perez2','alumnojohn@gmail.com','2017-08-28','');
INSERT INTO clientes (id,nombre,apellido,email,create_at,foto) VALUES (4,'John','Perez3','alumnojohn@gmail.com','2017-08-28','');
INSERT INTO clientes (id,nombre,apellido,email,create_at,foto) VALUES (5,'John','Perez4','alumnojohn@gmail.com','2017-08-28','');
INSERT INTO clientes (id,nombre,apellido,email,create_at,foto) VALUES (6,'John','Perez5','alumnojohn@gmail.com','2017-08-28','');
INSERT INTO clientes (id,nombre,apellido,email,create_at,foto) VALUES (7,'Andres','Guzman','profesor@gmail.com','2017-08-28','');
INSERT INTO clientes (id,nombre,apellido,email,create_at,foto) VALUES (8,'John','Perez1','alumnojohn@gmail.com','2017-08-28','');
INSERT INTO clientes (id,nombre,apellido,email,create_at,foto) VALUES (9,'John','Perez2','alumnojohn@gmail.com','2017-08-28','');
INSERT INTO clientes (id,nombre,apellido,email,create_at,foto) VALUES (10,'John','Perez3','alumnojohn@gmail.com','2017-08-28','');
INSERT INTO clientes (id,nombre,apellido,email,create_at,foto) VALUES (11,'John','Perez4','alumnojohn@gmail.com','2017-08-28','');
INSERT INTO clientes (id,nombre,apellido,email,create_at,foto) VALUES (12,'John','Perez5','alumnojohn@gmail.com','2017-08-28','');
INSERT INTO clientes (id,nombre,apellido,email,create_at,foto) VALUES (13,'Andres','Guzman','profesor@gmail.com','2017-08-28','');
INSERT INTO clientes (id,nombre,apellido,email,create_at,foto) VALUES (14,'John','Perez1','alumnojohn@gmail.com','2017-08-28','');
INSERT INTO clientes (id,nombre,apellido,email,create_at,foto) VALUES (15,'John','Perez2','alumnojohn@gmail.com','2017-08-28','');
INSERT INTO clientes (id,nombre,apellido,email,create_at,foto) VALUES (16,'John','Perez3','alumnojohn@gmail.com','2017-08-28','');
INSERT INTO clientes (id,nombre,apellido,email,create_at,foto) VALUES (17,'John','Perez4','alumnojohn@gmail.com','2017-08-28','');
INSERT INTO clientes (id,nombre,apellido,email,create_at,foto) VALUES (18,'John','Perez5','alumnojohn@gmail.com','2017-08-28','');
INSERT INTO clientes (id,nombre,apellido,email,create_at,foto) VALUES (19,'Andres','Guzman','profesor@gmail.com','2017-08-28','');
INSERT INTO clientes (id,nombre,apellido,email,create_at,foto) VALUES (20,'John','Perez1','alumnojohn@gmail.com','2017-08-28','');
INSERT INTO clientes (id,nombre,apellido,email,create_at,foto) VALUES (21,'John','Perez2','alumnojohn@gmail.com','2017-08-28','');
INSERT INTO clientes (id,nombre,apellido,email,create_at,foto) VALUES (22,'John','Perez3','alumnojohn@gmail.com','2017-08-28','');
INSERT INTO clientes (id,nombre,apellido,email,create_at,foto) VALUES (23,'John','Perez4','alumnojohn@gmail.com','2017-08-28','');
INSERT INTO clientes (id,nombre,apellido,email,create_at,foto) VALUES (24,'John','Perez5','alumnojohn@gmail.com','2017-08-28','');
/*productos*/
INSERT INTO productos (nombre,precio,cliente_id,create_at) VALUES ('bycicle',45641,NOW());
INSERT INTO productos (nombre,precio,create_at) VALUES ('motorbyke',55331,NOW());
INSERT INTO productos (nombre,precio,create_at) VALUES ('car movile',3451,NOW());
INSERT INTO productos (nombre,precio,create_at) VALUES ('toy power ranger',6422,NOW());
INSERT INTO productos (nombre,precio,create_at) VALUES ('pelucha',3421,NOW());

/*facturas*/
INSERT INTO facturas  (descripcion,observacion,cliente_id,create_at) VALUES ('Factura1 cliente1','observacion 1',1,NOW());
INSERT INTO facturas_items  (cantidad,factura_id,producto_id) VALUES (1,1,1);

INSERT INTO facturas  (descripcion,observacion,cliente_id,create_at) VALUES ('Factura2 cliente1','observacion 1',1,NOW());
INSERT INTO facturas_items  (cantidad,factura_id,producto_id) VALUES (1,2,2);

s