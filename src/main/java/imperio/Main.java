package imperio;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Sistema de Inteligencia Táctica "Imperio" - aplicación de consola.
 * Solo habla con la interfaz EscuadronDAO (no contiene SQL).
 */
public class Main {

    public static void main(String[] args) {
        EscuadronDAO dao = new EscuadronDAOImpl();

        try {
            dao.crearTabla();
            System.out.println("[Imperio] Conexión establecida. Tabla escuadrones_imperiales lista.");
        } catch (SQLException ex) {
            System.err.println("[Imperio] No se pudo inicializar la base de datos: " + ex.getMessage());
            System.err.println("Comprueba que PostgreSQL está activo, que existe la BD 'imperio' y las credenciales.");
            return;
        }

        try (Scanner sc = new Scanner(System.in)) {
            int opcion;
            do {
                mostrarMenu();
                opcion = leerEntero(sc, "Opción: ");
                try {
                    switch (opcion) {
                        case 1 -> registrar(dao, sc);
                        case 2 -> listar(dao);
                        case 3 -> buscarPorId(dao, sc);
                        case 4 -> buscarPorBase(dao, sc);
                        case 5 -> actualizar(dao, sc);
                        case 6 -> eliminar(dao, sc);
                        case 0 -> System.out.println("Que la Fuerza te acompañe. Cerrando Imperio...");
                        default -> System.out.println("Opción no válida.");
                    }
                } catch (SQLException ex) {
                    System.err.println("[Error BD] " + ex.getMessage());
                }
            } while (opcion != 0);
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n===== IMPERIO - INTELIGENCIA TÁCTICA REBELDE =====");
        System.out.println("1. Registrar escuadrón imperial");
        System.out.println("2. Listar todos los escuadrones");
        System.out.println("3. Buscar por ID");
        System.out.println("4. Buscar por base");
        System.out.println("5. Actualizar escuadrón");
        System.out.println("6. Eliminar escuadrón");
        System.out.println("0. Salir");
    }

    private static void registrar(EscuadronDAO dao, Scanner sc) throws SQLException {
        EscuadronDTO e = pedirDatos(sc);
        int id = dao.insertar(e);
        System.out.println("Escuadrón registrado con ID " + id);
    }

    private static void listar(EscuadronDAO dao) throws SQLException {
        List<EscuadronDTO> lista = dao.obtenerTodos();
        if (lista.isEmpty()) {
            System.out.println("No hay informes de espionaje registrados.");
            return;
        }
        lista.forEach(System.out::println);
    }

    private static void buscarPorId(EscuadronDAO dao, Scanner sc) throws SQLException {
        int id = leerEntero(sc, "ID: ");
        EscuadronDTO e = dao.obtenerPorId(id);
        System.out.println(e != null ? e : "No existe ningún escuadrón con ese ID.");
    }

    private static void buscarPorBase(EscuadronDAO dao, Scanner sc) throws SQLException {
        System.out.print("Base: ");
        List<EscuadronDTO> lista = dao.buscarPorBase(sc.nextLine().trim());
        if (lista.isEmpty()) {
            System.out.println("Sin resultados para esa base.");
        } else {
            lista.forEach(System.out::println);
        }
    }

    private static void actualizar(EscuadronDAO dao, Scanner sc) throws SQLException {
        int id = leerEntero(sc, "ID del escuadrón a actualizar: ");
        if (dao.obtenerPorId(id) == null) {
            System.out.println("No existe ningún escuadrón con ese ID.");
            return;
        }
        EscuadronDTO e = pedirDatos(sc);
        e.setId(id);
        System.out.println(dao.actualizar(e) ? "Escuadrón actualizado." : "No se pudo actualizar.");
    }

    private static void eliminar(EscuadronDAO dao, Scanner sc) throws SQLException {
        int id = leerEntero(sc, "ID del escuadrón a eliminar: ");
        System.out.println(dao.eliminar(id) ? "Escuadrón eliminado." : "No existe ningún escuadrón con ese ID.");
    }

    private static EscuadronDTO pedirDatos(Scanner sc) {
        System.out.print("Nombre del escuadrón: ");
        String nombre = sc.nextLine().trim();
        System.out.print("Base: ");
        String base = sc.nextLine().trim();
        int naves = leerEnteroEnRango(sc, "Número de naves (>= 0): ", 0, Integer.MAX_VALUE);
        System.out.print("Comandante: ");
        String comandante = sc.nextLine().trim();
        int amenaza = leerEnteroEnRango(sc, "Nivel de amenaza (1-5): ", 1, 5);
        return new EscuadronDTO(nombre, base, naves, comandante, amenaza);
    }

    private static int leerEntero(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException ex) {
                System.out.println("Introduce un número entero válido.");
            }
        }
    }

    private static int leerEnteroEnRango(Scanner sc, String mensaje, int min, int max) {
        while (true) {
            int valor = leerEntero(sc, mensaje);
            if (valor >= min && valor <= max) {
                return valor;
            }
            System.out.println("El valor debe estar entre " + min + " y " + max + ".");
        }
    }
}
