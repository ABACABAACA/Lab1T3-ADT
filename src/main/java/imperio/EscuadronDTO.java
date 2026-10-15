package imperio;

/**
 * DTO / JavaBean que representa una fila de la tabla escuadrones_imperiales.
 */
public class EscuadronDTO {

    private int id;
    private String nombre;
    private String base;
    private int numeroNaves;
    private String comandante;
    private int nivelAmenaza; // 1 (bajo) - 5 (crítico)

    public EscuadronDTO() {
    }

    // Constructor para nuevos escuadrones (el id lo genera la BD)
    public EscuadronDTO(String nombre, String base, int numeroNaves, String comandante, int nivelAmenaza) {
        this.nombre = nombre;
        this.base = base;
        this.numeroNaves = numeroNaves;
        this.comandante = comandante;
        this.nivelAmenaza = nivelAmenaza;
    }

    // Constructor completo (registros leídos de la BD)
    public EscuadronDTO(int id, String nombre, String base, int numeroNaves, String comandante, int nivelAmenaza) {
        this(nombre, base, numeroNaves, comandante, nivelAmenaza);
        this.id = id;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getBase() { return base; }
    public void setBase(String base) { this.base = base; }

    public int getNumeroNaves() { return numeroNaves; }
    public void setNumeroNaves(int numeroNaves) { this.numeroNaves = numeroNaves; }

    public String getComandante() { return comandante; }
    public void setComandante(String comandante) { this.comandante = comandante; }

    public int getNivelAmenaza() { return nivelAmenaza; }
    public void setNivelAmenaza(int nivelAmenaza) { this.nivelAmenaza = nivelAmenaza; }

    @Override
    public String toString() {
        return String.format(
            "[#%d] %-10s | Base: %-5s | Naves: %-3d | Comandante: %-5s | Amenaza: %d/5",
            id, nombre, base, numeroNaves, comandante, nivelAmenaza);
    }
}
