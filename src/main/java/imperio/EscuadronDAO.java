package imperio;

import java.sql.SQLException;
import java.util.List;

/**
 * Contrato de acceso a datos para los escuadrones imperiales.
 * La lógica de negocio solo conoce esta interfaz, nunca el SQL.
 */
public interface EscuadronDAO {

    void crearTabla() throws SQLException;

    int insertar(EscuadronDTO escuadron) throws SQLException;

    List<EscuadronDTO> obtenerTodos() throws SQLException;

    EscuadronDTO obtenerPorId(int id) throws SQLException;

    List<EscuadronDTO> buscarPorBase(String base) throws SQLException;

    boolean actualizar(EscuadronDTO escuadron) throws SQLException;

    boolean eliminar(int id) throws SQLException;
}
