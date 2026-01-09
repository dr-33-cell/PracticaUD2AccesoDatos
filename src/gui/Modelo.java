package gui;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.Properties;

public class Modelo {
    private String ip;
    private String user;
    private String password;
    private String adminPassword;

    public Modelo() {
        getPropValues();
    }

    public String getIp() {
        return ip;
    }
    public String getUser() {
        return user;
    }
    public String getPassword() {
        return password;
    }
    public String getAdminPassword() {
        return adminPassword;
    }

    private Connection conexion;

    void conectar() {
        try {
            conexion = DriverManager.getConnection(
                    "jdbc:mysql://"+ip+":3306/tienda_videojuegos",user, password);
        } catch (SQLException sqle) {
            try {
                conexion = DriverManager.getConnection(
                        "jdbc:mysql://"+ip+":3306/",user, password);

                PreparedStatement statement = null;

                String code = leerFichero();
                String[] query = code.split("--");
                for (String aQuery : query) {
                    statement = conexion.prepareStatement(aQuery);
                    statement.executeUpdate();
                }
                assert statement != null;
                statement.close();

            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String leerFichero() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("tienda_videojuegos.sql"));
        String linea;
        StringBuilder stringBuilder = new StringBuilder();
        while ((linea = reader.readLine()) != null) {
            stringBuilder.append(linea);
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    void desconectar() {
        try {
            conexion.close();
            conexion = null;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    // MÉTODOS PARA DESARROLLADORES
    void insertarDesarrollador(String nombre, String pais, LocalDate fundacion, String web, int empleados) {
        String sentenciaSql = "INSERT INTO desarrolladores (nombre, pais, fundacion, web, empleados) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, nombre);
            sentencia.setString(2, pais);
            sentencia.setDate(3, Date.valueOf(fundacion));
            sentencia.setString(4, web);
            sentencia.setInt(5, empleados);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    void modificarDesarrollador(String nombre, String pais, LocalDate fundacion, String web, int empleados, int iddesarrollador) {
        String sentenciaSql = "UPDATE desarrolladores SET nombre = ?, pais = ?, fundacion = ?, web = ?, empleados = ? " +
                "WHERE iddesarrollador = ?";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, nombre);
            sentencia.setString(2, pais);
            sentencia.setDate(3, Date.valueOf(fundacion));
            sentencia.setString(4, web);
            sentencia.setInt(5, empleados);
            sentencia.setInt(6, iddesarrollador);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    void eliminarDesarrollador(int iddesarrollador) {
        String sentenciaSql = "DELETE FROM desarrolladores WHERE iddesarrollador = ?";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setInt(1, iddesarrollador);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    ResultSet consultarDesarrolladores() throws SQLException {
        String sentenciaSql = "SELECT iddesarrollador as 'ID', " +
                "nombre as 'Nombre', " +
                "pais as 'País', " +
                "fundacion as 'Fecha de fundación', " +
                "web as 'Web', " +
                "empleados as 'Empleados' " +
                "FROM desarrolladores";
        PreparedStatement sentencia = null;
        ResultSet resultado = null;
        sentencia = conexion.prepareStatement(sentenciaSql);
        resultado = sentencia.executeQuery();
        return resultado;
    }

    // MÉTODOS PARA PLATAFORMAS
    void insertarPlataforma(String nombre, String fabricante, int generacion, LocalDate lanzamiento, String tipo) {
        String sentenciaSql = "INSERT INTO plataformas (nombre, fabricante, generacion, lanzamiento, tipo) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, nombre);
            sentencia.setString(2, fabricante);
            sentencia.setInt(3, generacion);
            sentencia.setDate(4, Date.valueOf(lanzamiento));
            sentencia.setString(5, tipo);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    void modificarPlataforma(String nombre, String fabricante, int generacion, LocalDate lanzamiento, String tipo, int idplataforma) {
        String sentenciaSql = "UPDATE plataformas SET nombre = ?, fabricante = ?, generacion = ?, lanzamiento = ?, tipo = ? " +
                "WHERE idplataforma = ?";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, nombre);
            sentencia.setString(2, fabricante);
            sentencia.setInt(3, generacion);
            sentencia.setDate(4, Date.valueOf(lanzamiento));
            sentencia.setString(5, tipo);
            sentencia.setInt(6, idplataforma);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    void eliminarPlataforma(int idplataforma) {
        String sentenciaSql = "DELETE FROM plataformas WHERE idplataforma = ?";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setInt(1, idplataforma);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    ResultSet consultarPlataformas() throws SQLException {
        String sentenciaSql = "SELECT idplataforma as 'ID', " +
                "nombre as 'Nombre', " +
                "fabricante as 'Fabricante', " +
                "generacion as 'Generación', " +
                "lanzamiento as 'Fecha de lanzamiento', " +
                "tipo as 'Tipo' " +
                "FROM plataformas";
        PreparedStatement sentencia = null;
        ResultSet resultado = null;
        sentencia = conexion.prepareStatement(sentenciaSql);
        resultado = sentencia.executeQuery();
        return resultado;
    }

    // MÉTODOS PARA VIDEOJUEGOS
    void insertarVideojuego(String titulo, String codigo, String desarrollador, String plataforma, String genero,
                            float precio, LocalDate fechalanzamiento, String clasificacion, int unidadesstock) {
        String sentenciaSql = "INSERT INTO videojuegos (titulo, codigo, iddesarrollador, idplataforma, genero, precio, " +
                "fechalanzamiento, clasificacion, unidadesstock) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement sentencia = null;

        int iddesarrollador = Integer.valueOf(desarrollador.split(" ")[0]);
        int idplataforma = Integer.valueOf(plataforma.split(" ")[0]);

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, titulo);
            sentencia.setString(2, codigo);
            sentencia.setInt(3, iddesarrollador);
            sentencia.setInt(4, idplataforma);
            sentencia.setString(5, genero);
            sentencia.setFloat(6, precio);
            sentencia.setDate(7, Date.valueOf(fechalanzamiento));
            sentencia.setString(8, clasificacion);
            sentencia.setInt(9, unidadesstock);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    void modificarVideojuego(String titulo, String codigo, String desarrollador, String plataforma, String genero,
                             float precio, LocalDate fechalanzamiento, String clasificacion, int unidadesstock, int idvideojuego) {
        String sentenciaSql = "UPDATE videojuegos SET titulo = ?, codigo = ?, iddesarrollador = ?, idplataforma = ?, " +
                "genero = ?, precio = ?, fechalanzamiento = ?, clasificacion = ?, unidadesstock = ? WHERE idvideojuego = ?";
        
        PreparedStatement sentencia = null;

        int iddesarrollador = Integer.valueOf(desarrollador.split(" ")[0]);
        int idplataforma = Integer.valueOf(plataforma.split(" ")[0]);

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, titulo);
            sentencia.setString(2, codigo);
            sentencia.setInt(3, iddesarrollador);
            sentencia.setInt(4, idplataforma);
            sentencia.setString(5, genero);
            sentencia.setFloat(6, precio);
            sentencia.setDate(7, Date.valueOf(fechalanzamiento));
            sentencia.setString(8, clasificacion);
            sentencia.setInt(9, unidadesstock);
            sentencia.setInt(10, idvideojuego);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    void eliminarVideojuego(int idvideojuego) {
        String sentenciaSql = "DELETE FROM videojuegos WHERE idvideojuego = ?";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setInt(1, idvideojuego);
            sentencia.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentencia != null)
                try {
                    sentencia.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    ResultSet consultarVideojuegos() throws SQLException {
        String sentenciaSql = "SELECT v.idvideojuego as 'ID', " +
                "v.titulo as 'Título', " +
                "v.codigo as 'Código', " +
                "concat(d.iddesarrollador, ' - ', d.nombre) as 'Desarrollador', " +
                "concat(p.idplataforma, ' - ', p.nombre) as 'Plataforma', " +
                "v.genero as 'Género', " +
                "v.precio as 'Precio', " +
                "v.fechalanzamiento as 'Fecha de lanzamiento', " +
                "v.clasificacion as 'Clasificación', " +
                "v.unidadesstock as 'Stock' " +
                "FROM videojuegos as v " +
                "inner join desarrolladores as d " +
                "on d.iddesarrollador = v.iddesarrollador " +
                "inner join plataformas as p " +
                "on p.idplataforma = v.idplataforma";
        PreparedStatement sentencia = null;
        ResultSet resultado = null;
        sentencia = conexion.prepareStatement(sentenciaSql);
        resultado = sentencia.executeQuery();
        return resultado;
    }

    // MÉTODOS DE VALIDACIÓN
    public boolean videojuegoCodigoYaExiste(String codigo) {
        String codigoConsult = "SELECT existeCodigo(?)";
        PreparedStatement function;
        boolean codigoExists = false;
        try {
            function = conexion.prepareStatement(codigoConsult);
            function.setString(1, codigo);
            ResultSet rs = function.executeQuery();
            rs.next();

            codigoExists = rs.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return codigoExists;
    }

    public boolean desarrolladorNombreYaExiste(String nombre) {
        String nombreConsult = "SELECT existeNombreDesarrollador(?)";
        PreparedStatement function;
        boolean nameExists = false;
        try {
            function = conexion.prepareStatement(nombreConsult);
            function.setString(1, nombre);
            ResultSet rs = function.executeQuery();
            rs.next();

            nameExists = rs.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nameExists;
    }

    public boolean plataformaNombreYaExiste(String nombre) {
        String nombreConsult = "SELECT existeNombrePlataforma(?)";
        PreparedStatement function;
        boolean nameExists = false;
        try {
            function = conexion.prepareStatement(nombreConsult);
            function.setString(1, nombre);
            ResultSet rs = function.executeQuery();
            rs.next();

            nameExists = rs.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nameExists;
    }

    public float obtenerPrecioVideojuego(int idvideojuego) {
        String precioConsult = "SELECT obtenerPrecioVideojuego(?)";
        PreparedStatement function;
        float precio = 0;
        try {
            function = conexion.prepareStatement(precioConsult);
            function.setInt(1, idvideojuego);
            ResultSet rs = function.executeQuery();
            rs.next();

            precio = rs.getFloat(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return precio;
    }

    // MÉTODOS DE CONFIGURACIÓN
    private void getPropValues() {
        InputStream inputStream = null;
        try {
            Properties prop = new Properties();
            String propFileName = "config.properties";

            inputStream = new FileInputStream(propFileName);

            prop.load(inputStream);
            ip = prop.getProperty("ip");
            user = prop.getProperty("user");
            password = prop.getProperty("pass");
            adminPassword = prop.getProperty("admin");

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            try {
                if (inputStream != null) inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void setPropValues(String ip, String user, String pass, String adminPass) {
        try {
            Properties prop = new Properties();
            prop.setProperty("ip", ip);
            prop.setProperty("user", user);
            prop.setProperty("pass", pass);
            prop.setProperty("admin", adminPass);
            OutputStream out = new FileOutputStream("config.properties");
            prop.store(out, null);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.ip = ip;
        this.user = user;
        this.password = pass;
        this.adminPassword = adminPass;
    }
}