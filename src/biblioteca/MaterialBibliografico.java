package biblioteca;

/**
 * Clase ABSTRACTA: representa cualquier material que la biblioteca presta.
 * No se pueden crear objetos "MaterialBibliografico" directamente; solo de sus
 * subclases (Libro, Revista, Dvd). Aquí va lo que TODOS los materiales comparten
 * (herencia), y se declaran los métodos que cada tipo resuelve a su manera
 * (polimorfismo).
 */
public abstract class MaterialBibliografico {

    // ---------- Atributos comunes (encapsulados) ----------
    private final String codigo;     // identificador único (no cambia)
    private final String titulo;
    private final int anio;
    private boolean disponible;

    // protected: solo las subclases pueden llamar a este constructor
    protected MaterialBibliografico(String codigo, String titulo, int anio) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código es obligatorio");
        }
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El título es obligatorio");
        }
        this.codigo = codigo;
        this.titulo = titulo;
        this.anio = anio;
        this.disponible = true;
    }

    // ---------- Métodos abstractos: cada subclase decide ----------
    /** Nombre del tipo de material (Libro, Revista, DVD...). */
    public abstract String getTipo();

    /** Cuántos días se presta este tipo de material. */
    public abstract int getDiasPrestamo();

    /** Cuánto cuesta cada día de retraso (en pesos). */
    public abstract long getMultaPorDia();

    /** Datos propios de cada tipo, para la descripción. */
    protected abstract String detalle();

    // ---------- Métodos comunes (heredados por todas las subclases) ----------
    /** Marca el material como prestado. Falla si ya estaba prestado. */
    public void marcarPrestado() {
        if (!disponible) {
            throw new IllegalStateException("'" + titulo + "' ya está prestado");
        }
        disponible = false;
    }

    public void marcarDevuelto() {
        disponible = true;
    }

    /** Descripción armada con partes comunes y partes propias de cada tipo. */
    public String getDescripcion() {
        return getTipo() + ": " + titulo + " (" + anio + "), " + detalle()
                + " | " + getDiasPrestamo() + " días, multa $" + getMultaPorDia() + "/día";
    }

    public String getCodigo()     { return codigo; }
    public String getTitulo()     { return titulo; }
    public int getAnio()          { return anio; }
    public boolean isDisponible() { return disponible; }

    @Override
    public String toString() {
        return getDescripcion() + " - " + (disponible ? "disponible" : "prestado");
    }
}
