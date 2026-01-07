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

    public void conectar() {
        try {
            conexion = DriverManager.getConnection(
                    "jdbc:mysql://"+ip+":3306/tienda_productos?allowMultiQueries=true",user, password);
            System.out.println("Conexión exitosa a tienda_productos");
        } catch (SQLException sqle) {
            System.out.println("Base de datos no encontrada. Creando base de datos...");
            try {
                conexion = DriverManager.getConnection(
                        "jdbc:mysql://"+ip+":3306/?allowMultiQueries=true",user, password);

                String code = leerFichero();

                // Ejecutar el script completo
                Statement statement = conexion.createStatement();

                // Dividir por punto y coma para ejecutar comando por comando
                String[] queries = code.split(";");

                for (String query : queries) {
                    query = query.trim();
                    if (!query.isEmpty() && !query.startsWith("--")) {
                        try {
                            statement.execute(query);
                            System.out.println("Ejecutado: " + query.substring(0, Math.min(50, query.length())) + "...");
                        } catch (SQLException e) {
                            // Ignorar errores de funciones que ya existen o comandos DELIMITER
                            if (!query.contains("DELIMITER")) {
                                System.out.println("Error en query: " + e.getMessage());
                            }
                        }
                    }
                }

                statement.close();
                System.out.println("Base de datos creada exitosamente");

                // Reconectar a la base de datos recién creada
                conexion.close();
                conexion = DriverManager.getConnection(
                        "jdbc:mysql://"+ip+":3306/tienda_productos?allowMultiQueries=true",user, password);

            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String leerFichero() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("tienda_productos.sql"));
        String linea;
        StringBuilder stringBuilder = new StringBuilder();
        while ((linea = reader.readLine()) != null) {
            stringBuilder.append(linea);
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    public void desconectar() {
        try {
            conexion.close();
            conexion = null;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }



    void insertarCompannia(String nombre, String email, String telefono, String plataforma, String web) {
        String sentenciaSql = "INSERT INTO COMPANNIA (nombre, email, telefono, plataforma, web) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, nombre);
            sentencia.setString(2, email);
            sentencia.setString(3, telefono);
            sentencia.setString(4, plataforma);
            sentencia.setString(5, web);
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

    void modificarCompannia(String nombre, String email, String telefono, String plataforma, String web, int idCompannia) {
        String sentenciaSql = "UPDATE COMPANNIA SET nombre = ?, email = ?, telefono = ?, plataforma = ?, web = ? " +
                "WHERE id_compannia = ?";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, nombre);
            sentencia.setString(2, email);
            sentencia.setString(3, telefono);
            sentencia.setString(4, plataforma);
            sentencia.setString(5, web);
            sentencia.setInt(6, idCompannia);
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

    void eliminarCompannia(int idCompannia) {
        String sentenciaSql = "DELETE FROM COMPANNIA WHERE id_compannia = ?";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setInt(1, idCompannia);
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

    ResultSet consultarCompannia() throws SQLException {
        String sentenciaSql = "SELECT id_compannia as 'ID', " +
                "nombre as 'Nombre', " +
                "email as 'Email', " +
                "telefono as 'Teléfono', " +
                "plataforma as 'Plataforma', " +
                "web as 'Web' " +
                "FROM COMPANNIA";
        PreparedStatement sentencia = null;
        ResultSet resultado = null;
        sentencia = conexion.prepareStatement(sentenciaSql);
        resultado = sentencia.executeQuery();
        return resultado;
    }

    void insertarVideojuego(String titulo, String compannia, String plataforma, String genero,
                            LocalDate fechaLanzamiento, int id, int precio) {
        String sentenciaSqlProducto = "INSERT INTO PRODUCTOS (titulo, genero, precio, fecha_lanzamiento, stock) VALUES (?, ?, ?, ?, ?)";
        String sentenciaSqlVideojuego = "INSERT INTO VIDEOJUEGOS (id_producto, id_compannia, plataforma, formato) " +
                "VALUES (?, ?, ?, ?)";
        PreparedStatement sentenciaProducto = null;
        PreparedStatement sentenciaVideojuego = null;

        int idCompannia = Integer.valueOf(compannia.split(" ")[0]);

        try {
            // Insertar producto
            sentenciaProducto = conexion.prepareStatement(sentenciaSqlProducto, Statement.RETURN_GENERATED_KEYS);
            sentenciaProducto.setString(1, titulo);
            sentenciaProducto.setString(2, genero);
            sentenciaProducto.setDouble(3, precio);
            sentenciaProducto.setDate(4, Date.valueOf(fechaLanzamiento));
            sentenciaProducto.setBoolean(5, stock);
            sentenciaProducto.executeUpdate();

            // Obtener el ID generado
            ResultSet rs = sentenciaProducto.getGeneratedKeys();
            int idProducto = 0;
            if (rs.next()) {
                idProducto = rs.getInt(1);
            }

            // Insertar videojuego
            sentenciaVideojuego = conexion.prepareStatement(sentenciaSqlVideojuego);
            sentenciaVideojuego.setInt(1, idProducto);
            sentenciaVideojuego.setInt(2, idCompannia);
            sentenciaVideojuego.setString(3, plataforma);
            sentenciaVideojuego.setString(4, formato);
            sentenciaVideojuego.executeUpdate();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentenciaProducto != null)
                try {
                    sentenciaProducto.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            if (sentenciaVideojuego != null)
                try {
                    sentenciaVideojuego.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    void modificarVideojuego(String titulo, String genero, double precio, LocalDate fechaLanzamiento,
                             boolean stock, String plataforma, String formato, String compannia, int idProducto) {
        String sentenciaSqlProducto = "UPDATE PRODUCTOS SET titulo = ?, genero = ?, precio = ?, " +
                "fecha_lanzamiento = ?, stock = ? WHERE id_producto = ?";
        String sentenciaSqlVideojuego = "UPDATE VIDEOJUEGOS SET id_compannia = ?, plataforma = ?, formato = ? " +
                "WHERE id_producto = ?";
        PreparedStatement sentenciaProducto = null;
        PreparedStatement sentenciaVideojuego = null;

        int idCompannia = Integer.valueOf(compannia.split(" ")[0]);

        try {
            // Actualizar producto
            sentenciaProducto = conexion.prepareStatement(sentenciaSqlProducto);
            sentenciaProducto.setString(1, titulo);
            sentenciaProducto.setString(2, genero);
            sentenciaProducto.setDouble(3, precio);
            sentenciaProducto.setDate(4, Date.valueOf(fechaLanzamiento));
            sentenciaProducto.setBoolean(5, stock);
            sentenciaProducto.setInt(6, idProducto);
            sentenciaProducto.executeUpdate();

            // Actualizar videojuego
            sentenciaVideojuego = conexion.prepareStatement(sentenciaSqlVideojuego);
            sentenciaVideojuego.setInt(1, idCompannia);
            sentenciaVideojuego.setString(2, plataforma);
            sentenciaVideojuego.setString(3, formato);
            sentenciaVideojuego.setInt(4, idProducto);
            sentenciaVideojuego.executeUpdate();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentenciaProducto != null)
                try {
                    sentenciaProducto.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            if (sentenciaVideojuego != null)
                try {
                    sentenciaVideojuego.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    void eliminarVideojuego(int idProducto) {
        String sentenciaSql = "DELETE FROM PRODUCTOS WHERE id_producto = ?";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setInt(1, idProducto);
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
        String sentenciaSql = "SELECT p.id_producto as 'ID', " +
                "p.titulo as 'Título', " +
                "p.genero as 'Género', " +
                "p.precio as 'Precio', " +
                "p.fecha_lanzamiento as 'Fecha Lanzamiento', " +
                "p.stock as 'Stock', " +
                "v.plataforma as 'Plataforma', " +
                "v.formato as 'Formato', " +
                "concat(c.id_compannia, ' - ', c.nombre) as 'Compañía' " +
                "FROM PRODUCTOS p " +
                "INNER JOIN VIDEOJUEGOS v ON p.id_producto = v.id_producto " +
                "INNER JOIN COMPANNIA c ON v.id_compannia = c.id_compannia";
        PreparedStatement sentencia = null;
        ResultSet resultado = null;
        sentencia = conexion.prepareStatement(sentenciaSql);
        resultado = sentencia.executeQuery();
        return resultado;
    }

    void insertarFigura(String titulo, String genero, double precio, LocalDate fechaLanzamiento,
                        boolean stock, double tamanio, String material, String compannia) {
        String sentenciaSqlProducto = "INSERT INTO PRODUCTOS (titulo, genero, precio, fecha_lanzamiento, stock) " +
                "VALUES (?, ?, ?, ?, ?)";
        String sentenciaSqlFigura = "INSERT INTO FIGURAS (id_producto, id_compannia, tamanio, material) " +
                "VALUES (?, ?, ?, ?)";
        PreparedStatement sentenciaProducto = null;
        PreparedStatement sentenciaFigura = null;

        int idCompannia = Integer.valueOf(compannia.split(" ")[0]);

        try {
            // Insertar producto
            sentenciaProducto = conexion.prepareStatement(sentenciaSqlProducto, Statement.RETURN_GENERATED_KEYS);
            sentenciaProducto.setString(1, titulo);
            sentenciaProducto.setString(2, genero);
            sentenciaProducto.setDouble(3, precio);
            sentenciaProducto.setDate(4, Date.valueOf(fechaLanzamiento));
            sentenciaProducto.setBoolean(5, stock);
            sentenciaProducto.executeUpdate();

            // Obtener el ID generado
            ResultSet rs = sentenciaProducto.getGeneratedKeys();
            int idProducto = 0;
            if (rs.next()) {
                idProducto = rs.getInt(1);
            }

            // Insertar figura
            sentenciaFigura = conexion.prepareStatement(sentenciaSqlFigura);
            sentenciaFigura.setInt(1, idProducto);
            sentenciaFigura.setInt(2, idCompannia);
            sentenciaFigura.setDouble(3, tamanio);
            sentenciaFigura.setString(4, material);
            sentenciaFigura.executeUpdate();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentenciaProducto != null)
                try {
                    sentenciaProducto.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            if (sentenciaFigura != null)
                try {
                    sentenciaFigura.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    void modificarFigura(String titulo, String genero, double precio, LocalDate fechaLanzamiento,
                         boolean stock, double tamanio, String material, String compannia, int idProducto) {
        String sentenciaSqlProducto = "UPDATE PRODUCTOS SET titulo = ?, genero = ?, precio = ?, " +
                "fecha_lanzamiento = ?, stock = ? WHERE id_producto = ?";
        String sentenciaSqlFigura = "UPDATE FIGURAS SET id_compannia = ?, tamanio = ?, material = ? " +
                "WHERE id_producto = ?";
        PreparedStatement sentenciaProducto = null;
        PreparedStatement sentenciaFigura = null;

        int idCompannia = Integer.valueOf(compannia.split(" ")[0]);

        try {
            // Actualizar producto
            sentenciaProducto = conexion.prepareStatement(sentenciaSqlProducto);
            sentenciaProducto.setString(1, titulo);
            sentenciaProducto.setString(2, genero);
            sentenciaProducto.setDouble(3, precio);
            sentenciaProducto.setDate(4, Date.valueOf(fechaLanzamiento));
            sentenciaProducto.setBoolean(5, stock);
            sentenciaProducto.setInt(6, idProducto);
            sentenciaProducto.executeUpdate();

            // Actualizar figura
            sentenciaFigura = conexion.prepareStatement(sentenciaSqlFigura);
            sentenciaFigura.setInt(1, idCompannia);
            sentenciaFigura.setDouble(2, tamanio);
            sentenciaFigura.setString(3, material);
            sentenciaFigura.setInt(4, idProducto);
            sentenciaFigura.executeUpdate();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (sentenciaProducto != null)
                try {
                    sentenciaProducto.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
            if (sentenciaFigura != null)
                try {
                    sentenciaFigura.close();
                } catch (SQLException sqle) {
                    sqle.printStackTrace();
                }
        }
    }

    void eliminarFigura(int idProducto) {
        String sentenciaSql = "DELETE FROM PRODUCTOS WHERE id_producto = ?";
        PreparedStatement sentencia = null;

        try {
            sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setInt(1, idProducto);
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

    ResultSet consultarFiguras() throws SQLException {
        String sentenciaSql = "SELECT p.id_producto as 'ID', " +
                "p.titulo as 'Título', " +
                "p.genero as 'Género', " +
                "p.precio as 'Precio', " +
                "p.fecha_lanzamiento as 'Fecha Lanzamiento', " +
                "p.stock as 'Stock', " +
                "f.tamanio as 'Tamaño (cm)', " +
                "f.material as 'Material', " +
                "concat(c.id_compannia, ' - ', c.nombre) as 'Compañía' " +
                "FROM PRODUCTOS p " +
                "INNER JOIN FIGURAS f ON p.id_producto = f.id_producto " +
                "INNER JOIN COMPANNIA c ON f.id_compannia = c.id_compannia";
        PreparedStatement sentencia = null;
        ResultSet resultado = null;
        sentencia = conexion.prepareStatement(sentenciaSql);
        resultado = sentencia.executeQuery();
        return resultado;
    }

    public boolean productoYaExiste(int idProducto) {
        String consultaExiste = "SELECT existeProducto(?)";
        PreparedStatement function;
        boolean existe = false;
        try {
            function = conexion.prepareStatement(consultaExiste);
            function.setInt(1, idProducto);
            ResultSet rs = function.executeQuery();
            rs.next();
            existe = rs.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existe;
    }

    public boolean tituloProductoYaExiste(String titulo) {
        String consultaTitulo = "SELECT existeTituloProducto(?)";
        PreparedStatement function;
        boolean existe = false;
        try {
            function = conexion.prepareStatement(consultaTitulo);
            function.setString(1, titulo);
            ResultSet rs = function.executeQuery();
            rs.next();
            existe = rs.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existe;
    }

    public boolean videojuegoYaExiste(int idProducto) {
        String consultaVideojuego = "SELECT existeVideojuego(?)";
        PreparedStatement function;
        boolean existe = false;
        try {
            function = conexion.prepareStatement(consultaVideojuego);
            function.setInt(1, idProducto);
            ResultSet rs = function.executeQuery();
            rs.next();
            existe = rs.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existe;
    }

    public boolean figuraYaExiste(int idProducto) {
        String consultaFigura = "SELECT existeFigura(?)";
        PreparedStatement function;
        boolean existe = false;
        try {
            function = conexion.prepareStatement(consultaFigura);
            function.setInt(1, idProducto);
            ResultSet rs = function.executeQuery();
            rs.next();
            existe = rs.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existe;
    }

    public boolean companniaYaExiste(String nombre) {
        String consultaCompannia = "SELECT existeNombreCompannia(?)";
        PreparedStatement function;
        boolean existe = false;
        try {
            function = conexion.prepareStatement(consultaCompannia);
            function.setString(1, nombre);
            ResultSet rs = function.executeQuery();
            rs.next();
            existe = rs.getBoolean(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existe;
    }

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
