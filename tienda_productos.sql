CREATE DATABASE IF NOT EXISTS tienda_productos;
--
USE tienda_productos;
--
CREATE TABLE IF NOT EXISTS PRODUCTOS (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    genero VARCHAR(50) NOT NULL,
    precio DECIMAL(10,2) NOT NULL CHECK (precio >= 0),
    fecha_lanzamiento DATE NOT NULL,
    stock BOOLEAN NOT NULL DEFAULT false
);
--
CREATE TABLE IF NOT EXISTS COMPANNIA (
    id_compannia INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    telefono VARCHAR(15),
    plataforma VARCHAR(50),
    web VARCHAR(500)
);
--
CREATE TABLE IF NOT EXISTS VIDEOJUEGOS (
    id_videojuego INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    id_compannia INT NOT NULL,
    plataforma VARCHAR(50) NOT NULL,
    formato VARCHAR(20) DEFAULT 'Digital'
);
--
CREATE TABLE IF NOT EXISTS FIGURAS (
    id_figura INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    id_compannia INT NOT NULL,
    tamanio DECIMAL(5,2) NOT NULL CHECK (tamanio > 0),
    material VARCHAR(50) DEFAULT 'PVC'
);
--
ALTER TABLE VIDEOJUEGOS
ADD FOREIGN KEY (id_producto) REFERENCES PRODUCTOS(id_producto) ON DELETE CASCADE;
--
ALTER TABLE VIDEOJUEGOS
ADD FOREIGN KEY (id_compannia) REFERENCES COMPANNIA(id_compannia);
--
ALTER TABLE FIGURAS
ADD FOREIGN KEY (id_producto) REFERENCES PRODUCTOS(id_producto) ON DELETE CASCADE;
--
ALTER TABLE FIGURAS
ADD FOREIGN KEY (id_compannia) REFERENCES COMPANNIA(id_compannia);
--
DROP FUNCTION IF EXISTS existeProducto;
--
DELIMITER $$
CREATE FUNCTION existeProducto(f_id INT)
RETURNS BIT
DETERMINISTIC
BEGIN
   DECLARE i INT;
   SET i=0;
   WHILE (i<(SELECT MAX(id_producto) FROM PRODUCTOS)) DO
   IF ((SELECT id_producto FROM PRODUCTOS
       WHERE id_producto=(i+1)) = f_id)
   THEN RETURN 1;
   END IF;
   SET i=i+1;
   END WHILE;
   RETURN 0;
END$$
DELIMITER ;
--
DROP FUNCTION IF EXISTS existeTituloProducto;
--
DELIMITER $$
CREATE FUNCTION existeTituloProducto(f_titulo VARCHAR(100))
RETURNS BIT
DETERMINISTIC
BEGIN
   DECLARE i INT;
   SET i=0;
   WHILE (i<(SELECT MAX(id_producto) FROM PRODUCTOS)) DO
   IF ((SELECT titulo FROM PRODUCTOS
       WHERE id_producto=(i+1)) LIKE f_titulo)
   THEN RETURN 1;
   END IF;
   SET i=i+1;
   END WHILE;
   RETURN 0;
END$$
DELIMITER ;
--
DROP FUNCTION IF EXISTS existeVideojuego;
--
DELIMITER $$
CREATE FUNCTION existeVideojuego(f_id_producto INT)
RETURNS BIT
DETERMINISTIC
BEGIN
   DECLARE i INT;
   SET i=0;
   WHILE (i<(SELECT MAX(id_videojuego) FROM VIDEOJUEGOS)) DO
   IF ((SELECT id_producto FROM VIDEOJUEGOS
       WHERE id_videojuego=(i+1)) = f_id_producto)
   THEN RETURN 1;
   END IF;
   SET i=i+1;
   END WHILE;
   RETURN 0;
END$$
DELIMITER ;
--
DROP FUNCTION IF EXISTS existeFigura;
--
DELIMITER $$
CREATE FUNCTION existeFigura(f_id_producto INT)
RETURNS BIT
DETERMINISTIC
BEGIN
   DECLARE i INT;
   SET i=0;
   WHILE (i<(SELECT MAX(id_figura) FROM FIGURAS)) DO
   IF ((SELECT id_producto FROM FIGURAS
       WHERE id_figura=(i+1)) = f_id_producto)
   THEN RETURN 1;
   END IF;
   SET i=i+1;
   END WHILE;
   RETURN 0;
END$$
DELIMITER ;
--
DROP FUNCTION IF EXISTS existeNombreCompannia;
--
DELIMITER $$
CREATE FUNCTION existeNombreCompannia(f_nombre VARCHAR(100))
RETURNS BIT
DETERMINISTIC
BEGIN
   DECLARE i INT;
   SET i=0;
   WHILE (i<(SELECT MAX(id_compannia) FROM COMPANNIA)) DO
   IF ((SELECT nombre FROM COMPANNIA
       WHERE id_compannia=(i+1)) LIKE f_nombre)
   THEN RETURN 1;
   END IF;
   SET i=i+1;
   END WHILE;
   RETURN 0;
END$$
DELIMITER ;
--
INSERT INTO COMPANNIA (nombre, email, telefono, plataforma, web) VALUES
('Nintendo', 'contacto@nintendo.com', '123456789', 'Nintendo Switch', 'https://www.nintendo.com');
--
INSERT INTO COMPANNIA (nombre, email, telefono, plataforma, web) VALUES
('FromSoftware', 'info@fromsoftware.jp', '987654321', 'Multiplataforma', 'https://www.fromsoftware.jp');
--
INSERT INTO COMPANNIA (nombre, email, telefono, plataforma, web) VALUES
('EA Sports', 'support@ea.com', '555123456', 'Multiplataforma', 'https://www.ea.com/sports');
--
INSERT INTO COMPANNIA (nombre, email, telefono, plataforma, web) VALUES
('Good Smile Company', 'info@goodsmile.com', '444555666', 'Figuras', 'https://www.goodsmile.info');
--
INSERT INTO COMPANNIA (nombre, email, telefono, plataforma, web) VALUES
('McFarlane Toys', 'contact@mcfarlane.com', '333222111', 'Figuras', 'https://www.mcfarlane.com');
--
INSERT INTO COMPANNIA (nombre, email, telefono, plataforma, web) VALUES
('Funko', 'support@funko.com', '777888999', 'Figuras', 'https://www.funko.com');
--
INSERT INTO PRODUCTOS (titulo, genero, precio, fecha_lanzamiento, stock) VALUES
('The Legend of Zelda: Tears of the Kingdom', 'Aventura', 59.99, '2023-05-12', true);
--
INSERT INTO PRODUCTOS (titulo, genero, precio, fecha_lanzamiento, stock) VALUES
('Elden Ring', 'RPG', 49.99, '2022-02-25', true);
--
INSERT INTO PRODUCTOS (titulo, genero, precio, fecha_lanzamiento, stock) VALUES
('FIFA 24', 'Deportes', 69.99, '2023-09-29', false);
--
INSERT INTO PRODUCTOS (titulo, genero, precio, fecha_lanzamiento, stock) VALUES
('Link Figura Coleccionable', 'Aventura', 34.99, '2023-06-15', true);
--
INSERT INTO PRODUCTOS (titulo, genero, precio, fecha_lanzamiento, stock) VALUES
('Master Chief Figura Premium', 'Acción', 89.99, '2023-08-20', true);
--
INSERT INTO PRODUCTOS (titulo, genero, precio, fecha_lanzamiento, stock) VALUES
('Pikachu Figura Deluxe', 'Fantasía', 44.99, '2023-07-10', false);
--
INSERT INTO VIDEOJUEGOS (id_producto, id_compannia, plataforma, formato) VALUES
(1, 1, 'Nintendo Switch', 'Físico');
--
INSERT INTO VIDEOJUEGOS (id_producto, id_compannia, plataforma, formato) VALUES
(2, 2, 'PlayStation 5', 'Digital');
--
INSERT INTO VIDEOJUEGOS (id_producto, id_compannia, plataforma, formato) VALUES
(3, 3, 'Xbox Series X', 'Digital');
--
INSERT INTO FIGURAS (id_producto, id_compannia, tamanio, material) VALUES
(4, 4, 25.50, 'PVC');
--
INSERT INTO FIGURAS (id_producto, id_compannia, tamanio, material) VALUES
(5, 5, 30.00, 'Resina');
--
INSERT INTO FIGURAS (id_producto, id_compannia, tamanio, material) VALUES
(6, 6, 15.75, 'PVC');