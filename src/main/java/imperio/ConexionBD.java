package imperio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Proveedor de conexiones a PostgreSQL.
 * Los valores pueden sobrescribirse con variables de entorno
 * (IMPERIO_URL, IMPERIO_USER, IMPERIO_PASS) para no dejar credenciales en el código.
 */
public final class ConexionBD {

    private static final String URL  = System.getenv().getOrDefault("IMPERIO_URL",  "jdbc:postgresql://localhost:5432/prueba");
    private static final String USER = System.getenv().getOrDefault("IMPERIO_USER", "openpg");
    private static final String PASS = System.getenv().getOrDefault("IMPERIO_PASS", "openpgpwd");

    private ConexionBD() {
    }

    /** Devuelve una conexión nueva. Quien la reciba debe cerrarla (try-with-resources). */
    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
