package aprobar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBaseDatos {
    private static final String URL = "jdbc:postgresql://localhost:5432/ la base de datos bobo";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Tucontraseña pues oe";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
