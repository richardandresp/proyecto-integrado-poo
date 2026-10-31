package biblioteca;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Une a un Usuario con un MaterialBibliografico durante un tiempo (asociación).
 * No sabe si el material es un Libro o un DVD, ni si el usuario es Estudiante o
 * Docente: les pregunta a ellos (polimorfismo) los días y la multa.
 */
public class Prestamo {

    private final MaterialBibliografico material;
    private final Usuario usuario;
    private final LocalDate fechaPrestamo;
    private final LocalDate fechaLimite;
    private LocalDate fechaDevolucion;           // null mientras esté activo

    // Visibilidad de paquete: solo la Biblioteca crea préstamos (composición).
    Prestamo(MaterialBibliografico material, Usuario usuario, LocalDate fechaPrestamo) {
        this.material = material;
        this.usuario = usuario;
        this.fechaPrestamo = fechaPrestamo;
        int dias = material.getDiasPrestamo() + usuario.getDiasExtra();
        this.fechaLimite = fechaPrestamo.plusDays(dias);
    }

    void registrarDevolucion(LocalDate fecha) {
        if (fecha.isBefore(fechaPrestamo)) {
            throw new IllegalArgumentException("La devolución no puede ser antes del préstamo");
        }
        this.fechaDevolucion = fecha;
    }

    public boolean estaActivo() {
        return fechaDevolucion == null;
    }

    public long diasDeRetraso(LocalDate fecha) {
        return Math.max(0, ChronoUnit.DAYS.between(fechaLimite, fecha));
    }

    /** Multa = días de retraso × tarifa del material, con el descuento del usuario. */
    public long calcularMulta(LocalDate fecha) {
        long bruta = diasDeRetraso(fecha) * material.getMultaPorDia();
        return usuario.aplicarDescuento(bruta);
    }

    public MaterialBibliografico getMaterial() { return material; }
    public Usuario getUsuario()                { return usuario; }
    public LocalDate getFechaPrestamo()        { return fechaPrestamo; }
    public LocalDate getFechaLimite()          { return fechaLimite; }
    public LocalDate getFechaDevolucion()      { return fechaDevolucion; }

    @Override
    public String toString() {
        return material.getTipo() + " '" + material.getTitulo() + "' -> "
                + usuario.getNombre() + " (hasta " + fechaLimite + ")";
    }
}
