package imperio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación JDBC/PostgreSQL del DAO.
 * - Todas las consultas con datos del usuario usan PreparedStatement y marcadores '?'.
 * - Todos los recursos (Connection, Statement, ResultSet) se cierran con try-with-resources.
 */
public class EscuadronDAOImpl implements EscuadronDAO {

    private static final String SQL_CREAR_TABLA =
        "CREATE TABLE IF NOT EXISTS imperio ("
      + " id SERIAL PRIMARY KEY,"
      + " nombre char2 NOT NULL,"
      + " base char2 NOT NULL,"
      + " numero_naves INT NOT NULL CHECK (numero_naves >= 0),"
      + " comandante char2,"
      + " nivel_amenaza INT NOT NULL CHECK (nivel_amenaza BETWEEN 1 AND 5)"
      + ")";

    private static final String SQL_INSERTAR =
        "INSERT INTO imperio (nombre, base, numero_naves, comandante, nivel_amenaza) "
      + "VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_OBTENER_TODOS =
        "SELECT id, nombre, base, numero_naves, comandante, nivel_amenaza "
      + "FROM imperio ORDER BY id";

    private static final String SQL_OBTENER_POR_ID =
        "SELECT id, nombre, base, numero_naves, comandante, nivel_amenaza "
      + "FROM imperio WHERE id = ?";

    private static final String SQL_BUSCAR_POR_BASE =
        "SELECT id, nombre, base, numero_naves, comandante, nivel_amenaza "
      + "FROM imperio WHERE base = ? ORDER BY id";

    private static final String SQL_ACTUALIZAR =
        "UPDATE imperio "
      + "SET nombre = ?, base = ?, numero_naves = ?, comandante = ?, nivel_amenaza = ? "
      + "WHERE id = ?";

    private static final String SQL_ELIMINAR =
        "DELETE FROM imperio WHERE id = ?";

    /** DDL: no lleva parámetros de usuario, por eso basta un Statement. */
    @Override
    public void crearTabla() throws SQLException {
        try (Connection con = ConexionBD.getConexion();
             Statement st = con.createStatement()) {
            st.executeUpdate(SQL_CREAR_TABLA);
        }
    }

    @Override
    public int insertar(EscuadronDTO e) throws SQLException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_INSERTAR, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, e.getNombre());
            ps.setString(2, e.getBase());
            ps.setInt(3, e.getNumeroNaves());
            ps.setString(4, e.getComandante());
            ps.setInt(5, e.getNivelAmenaza());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int idGenerado = keys.getInt(1);
                    e.setId(idGenerado);
                    return idGenerado;
                }
            }
            return -1;
        }
    }

    @Override
    public List<EscuadronDTO> obtenerTodos() throws SQLException {
        List<EscuadronDTO> lista = new ArrayList<>();
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_OBTENER_TODOS);
             ResultSet rs = ps.executeQuery()) {

            // El cursor empieza ANTES de la primera fila: rs.next() lo avanza.
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    @Override
    public EscuadronDTO obtenerPorId(int id) throws SQLException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_OBTENER_POR_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    @Override
    public List<EscuadronDTO> buscarPorBase(String base) throws SQLException {
        List<EscuadronDTO> lista = new ArrayList<>();
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_POR_BASE)) {

            ps.setString(1, base); // el driver escapa el valor: una inyección SQL queda como texto inofensivo
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    @Override
    public boolean actualizar(EscuadronDTO e) throws SQLException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR)) {

            ps.setString(1, e.getNombre());
            ps.setString(2, e.getBase());
            ps.setInt(3, e.getNumeroNaves());
            ps.setString(4, e.getComandante());
            ps.setInt(5, e.getNivelAmenaza());
            ps.setInt(6, e.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_ELIMINAR)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    /** Convierte la fila actual del ResultSet en un EscuadronDTO. */
    private EscuadronDTO mapear(ResultSet rs) throws SQLException {
        return new EscuadronDTO(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("base"),
            rs.getInt("numero_naves"),
            rs.getString("comandante"),
            rs.getInt("nivel_amenaza"));
    }
}
