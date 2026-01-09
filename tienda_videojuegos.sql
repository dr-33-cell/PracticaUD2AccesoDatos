CREATE DATABASE IF NOT EXISTS tienda_videojuegos;
--
USE tienda_videojuegos;
--
CREATE TABLE IF NOT EXISTS desarrolladores (
                                               iddesarrollador INT AUTO_INCREMENT PRIMARY KEY,
                                               nombre VARCHAR(100) NOT NULL,
                                               pais VARCHAR(50) NOT NULL,
                                               fundacion DATE,
                                               web VARCHAR(500));
--
CREATE TABLE IF NOT EXISTS plataformas (
                                           idplataforma INT AUTO_INCREMENT PRIMARY KEY,
                                           nombre VARCHAR(50) NOT NULL,
                                           fabricante VARCHAR(50) NOT NULL,
                                           generacion INT,
                                           lanzamiento DATE,
                                           tipo VARCHAR(30));
--
CREATE TABLE IF NOT EXISTS videojuegos (
                                           idvideojuego INT AUTO_INCREMENT PRIMARY KEY,
                                           titulo VARCHAR(100) NOT NULL,
                                           codigo VARCHAR(40) NOT NULL UNIQUE,
                                           iddesarrollador INT NOT NULL,
                                           idplataforma INT NOT NULL,
                                           genero VARCHAR(50) NOT NULL,
                                           precio FLOAT NOT NULL,
                                           fechalanzamiento DATE,
                                           clasificacion VARCHAR(10),
                                           unidadesstock INT);
--
ALTER TABLE videojuegos
    ADD FOREIGN KEY (iddesarrollador) REFERENCES desarrolladores(iddesarrollador),
    ADD FOREIGN KEY (idplataforma) REFERENCES plataformas(idplataforma);
--
DELIMITER ||
CREATE FUNCTION existeCodigo(f_codigo VARCHAR(40))
    RETURNS BIT
BEGIN
    DECLARE i INT;
    SET i = 0;
    WHILE (i < (SELECT MAX(idvideojuego) FROM videojuegos)) DO
            IF ((SELECT codigo FROM videojuegos
                 WHERE idvideojuego = (i+1)) LIKE f_codigo)
            THEN RETURN 1;
            END IF;
            SET i = i + 1;
        END WHILE;
    RETURN 0;
END; ||
DELIMITER ;
--
DELIMITER ||
CREATE FUNCTION existeNombreDesarrollador(f_name VARCHAR(100))
    RETURNS BIT
BEGIN
    DECLARE i INT;
    SET i = 0;
    WHILE (i < (SELECT MAX(iddesarrollador) FROM desarrolladores)) DO
            IF ((SELECT nombre FROM desarrolladores
                 WHERE iddesarrollador = (i+1)) LIKE f_name)
            THEN RETURN 1;
            END IF;
            SET i = i + 1;
        END WHILE;
    RETURN 0;
END; ||
DELIMITER ;
--
DELIMITER ||
CREATE FUNCTION existeNombrePlataforma(f_name VARCHAR(50))
    RETURNS BIT
BEGIN
    DECLARE i INT;
    SET i = 0;
    WHILE (i < (SELECT MAX(idplataforma) FROM plataformas)) DO
            IF ((SELECT nombre FROM plataformas
                 WHERE idplataforma = (i+1)) LIKE f_name)
            THEN RETURN 1;
            END IF;
            SET i = i + 1;
        END WHILE;
    RETURN 0;
END; ||
DELIMITER ;
--
DELIMITER ||
CREATE FUNCTION obtenerPrecioVideojuego(f_idvideojuego INT)
    RETURNS FLOAT
BEGIN
    DECLARE v_precio FLOAT;
    SELECT precio INTO v_precio
    FROM videojuegos
    WHERE idvideojuego = f_idvideojuego;
    RETURN v_precio;
END; ||
DELIMITER ;
--
DELIMITER ||
CREATE PROCEDURE insertarVideojuego(
    IN p_titulo VARCHAR(100),
    IN p_codigo VARCHAR(40),
    IN p_iddesarrollador INT,
    IN p_idplataforma INT,
    IN p_genero VARCHAR(50),
    IN p_precio FLOAT,
    IN p_fechalanzamiento DATE,
    IN p_clasificacion VARCHAR(10),
    IN p_unidadesstock INT)
BEGIN
    IF NOT existeCodigo(p_codigo) THEN
        INSERT INTO videojuegos (titulo, codigo, iddesarrollador, idplataforma, genero, precio, fechalanzamiento, clasificacion, unidadesstock)
        VALUES (p_titulo, p_codigo, p_iddesarrollador, p_idplataforma, p_genero, p_precio, p_fechalanzamiento, p_clasificacion, p_unidadesstock);
        SELECT 'Videojuego insertado correctamente' AS Mensaje;
    ELSE
        SELECT 'Error: El código ya existe' AS Mensaje;
    END IF;
END; ||
DELIMITER ;
--
DELIMITER ||
CREATE PROCEDURE listarVideojuegosPorGenero(IN p_genero VARCHAR(50))
BEGIN
    SELECT v.titulo, v.codigo, d.nombre AS desarrollador, p.nombre AS plataforma, v.precio, v.fechalanzamiento
    FROM videojuegos v
             INNER JOIN desarrolladores d ON v.iddesarrollador = d.iddesarrollador
             INNER JOIN plataformas p ON v.idplataforma = p.idplataforma
    WHERE v.genero = p_genero
    ORDER BY v.titulo;
END; ||
DELIMITER ;