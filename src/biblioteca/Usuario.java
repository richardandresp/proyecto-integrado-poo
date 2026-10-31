package biblioteca;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase ABSTRACTA para las personas inscritas. Las reglas que cambian según el
 * tipo de usuario (cuántos préstamos, días extra, descuento en multas) se
 * definen en las subclases Estudiante y Docente (polimorfismo).
 */
public abstract class Usuario {

    private final String documento;
    private final String nombre;
    private final List<Prestamo> prestamosActivos = new ArrayList<>();

    protected Usuario(String documento, String nombre) {
        if (documento == null || documento.isBlank()) {
            throw new IllegalArgumentException("El documento es obligatorio");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        this.documento = documento;
        this.nombre = nombre;
    }

    // ---------- Lo que cada tipo de usuario define ----------
    public abstract String getTipo();
    public abstract int getMaxPrestamos();

    /** Días adicionales sobre el plazo normal. Por defecto, ninguno. */
    public int getDiasExtra() {
        return 0;
    }

    /** Aplica el descuento que corresponda a la multa. Por defecto, ninguno. */
    public long aplicarDescuento(long multa) {
        return multa;
    }

    // ---------- Comportamiento común ----------
    public boolean puedePrestar() {
        return prestamosActivos.size() < getMaxPrestamos();   // llamada polimórfica
    }

    // Visibilidad de paquete: solo la Biblioteca modifica los préstamos.
    void agregarPrestamo(Prestamo p) { prestamosActivos.add(p); }
    void quitarPrestamo(Prestamo p)  { prestamosActivos.remove(p); }

    public String getDocumento() { return documento; }
    public String getNombre()    { return nombre; }

    public List<Prestamo> getPrestamosActivos() {
        return Collections.unmodifiableList(prestamosActivos);
    }

    @Override
    public String toString() {
        return getTipo() + " " + nombre + " (doc. " + documento + ") - "
                + prestamosActivos.size() + "/" + getMaxPrestamos() + " préstamos";
    }
}
