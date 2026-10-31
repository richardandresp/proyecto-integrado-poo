package biblioteca;

import java.time.LocalDate;
import java.util.Map;

/**
 * Pruebas unitarias sin librerías externas, organizadas por clase.
 * Cada prueba prepara objetos, ejecuta un método y compara con el resultado esperado.
 * Ejecutar: java -cp out biblioteca.PruebasBiblioteca
 */
public class PruebasBiblioteca {

    private static int aprobadas = 0, fallidas = 0;
    private static final LocalDate HOY = LocalDate.of(2026, 10, 31);

    private static void verificar(String nombre, boolean condicion) {
        if (condicion) { aprobadas++; System.out.println("[OK]    " + nombre); }
        else           { fallidas++;  System.out.println("[FALLA] " + nombre); }
    }

    /** La prueba aprueba si la acción lanza una excepción (la regla se cumplió). */
    private static void verificarError(String nombre, Runnable accion) {
        try { accion.run(); verificar(nombre, false); }
        catch (RuntimeException e) { verificar(nombre + " -> \"" + e.getMessage() + "\"", true); }
    }

    private static void seccion(String titulo) {
        System.out.println("\n--- " + titulo + " ---");
    }

    /**
     * Tipo NUEVO creado solo en las pruebas para comprobar la escalabilidad:
     * la Biblioteca lo presta sin que se le cambie una sola línea.
     */
    static class AudioLibro extends MaterialBibliografico {
        AudioLibro(String codigo, String titulo, int anio) { super(codigo, titulo, anio); }
        @Override public String getTipo()      { return "Audiolibro"; }
        @Override public int getDiasPrestamo() { return 10; }
        @Override public long getMultaPorDia() { return 800; }
        @Override protected String detalle()   { return "formato MP3"; }
    }

    private static Biblioteca nuevaBiblioteca() {
        Biblioteca b = new Biblioteca("Prueba");
        b.agregarMaterial(new Libro("L1", "Libro A", "Autor A", 2000, 100));
        b.agregarMaterial(new Libro("L2", "Libro B", "Autor B", 2001, 200));
        b.agregarMaterial(new Revista("R1", "Revista A", 2025, 10));
        b.agregarMaterial(new Dvd("D1", "Película A", "Director A", 2010, 90));
        b.agregarMaterial(new Libro("L3", "Libro C", "Autor C", 2002, 300));
        b.registrarUsuario(new Estudiante("100", "Estudiante Uno", "Sistemas"));
        b.registrarUsuario(new Docente("200", "Docente Uno", "Ingeniería"));
        return b;
    }

    public static void main(String[] args) {
        System.out.println("=== Pruebas unitarias del Proyecto integrado con POO ===");

        seccion("1. Clase Libro (encapsulamiento y herencia)");
        Libro libro = new Libro("X1", "Prueba", "Autor", 2020, 150);
        verificar("Un libro nuevo empieza disponible", libro.isDisponible());
        verificar("Libro hereda de MaterialBibliografico", libro instanceof MaterialBibliografico);
        libro.marcarPrestado();
        verificar("Al prestarlo deja de estar disponible", !libro.isDisponible());
        verificarError("No se puede prestar dos veces el mismo libro", libro::marcarPrestado);
        libro.marcarDevuelto();
        verificar("Al devolverlo vuelve a estar disponible", libro.isDisponible());
        verificarError("No se puede crear un material sin título", () -> new Libro("X2", "", "A", 2020, 10));
        verificarError("No se puede crear una revista con edición 0", () -> new Revista("X3", "R", 2020, 0));

        seccion("2. Polimorfismo en los materiales");
        MaterialBibliografico[] tipos = {
            new Libro("P1", "L", "A", 2020, 1), new Revista("P2", "R", 2020, 1), new Dvd("P3", "D", "X", 2020, 1)
        };
        verificar("Libro se presta 15 días", tipos[0].getDiasPrestamo() == 15);
        verificar("Revista se presta 7 días", tipos[1].getDiasPrestamo() == 7);
        verificar("DVD se presta 3 días", tipos[2].getDiasPrestamo() == 3);
        verificar("Cada tipo tiene su propia multa diaria (1000, 500, 2000)",
                tipos[0].getMultaPorDia() == 1000 && tipos[1].getMultaPorDia() == 500 && tipos[2].getMultaPorDia() == 2000);
        verificar("getDescripcion() incluye los datos propios de cada tipo",
                tipos[0].getDescripcion().contains("págs.") && tipos[1].getDescripcion().contains("edición")
                && tipos[2].getDescripcion().contains("min"));

        seccion("3. Usuarios: Estudiante y Docente");
        Usuario est = new Estudiante("1", "E", "Sistemas");
        Usuario doc = new Docente("2", "D", "Ingeniería");
        verificar("Estudiante: máximo 3 préstamos", est.getMaxPrestamos() == 3);
        verificar("Docente: máximo 5 préstamos", doc.getMaxPrestamos() == 5);
        verificar("Docente tiene 7 días extra; estudiante 0", doc.getDiasExtra() == 7 && est.getDiasExtra() == 0);
        verificar("Docente paga 50 % de la multa; estudiante el 100 %",
                doc.aplicarDescuento(4000) == 2000 && est.aplicarDescuento(4000) == 4000);
        verificarError("No se puede crear un usuario sin documento", () -> new Estudiante("", "X", "Y"));

        seccion("4. Clase Prestamo (asociación)");
        Biblioteca b = nuevaBiblioteca();
        Prestamo pL = b.prestar("L1", "100", HOY);
        Prestamo pR = b.prestar("R1", "200", HOY);
        verificar("Préstamo une el material y el usuario correctos",
                pL.getMaterial().getCodigo().equals("L1") && pL.getUsuario().getDocumento().equals("100"));
        verificar("Libro a estudiante: fecha límite = hoy + 15", pL.getFechaLimite().equals(HOY.plusDays(15)));
        verificar("Revista a docente: fecha límite = hoy + 7 + 7", pR.getFechaLimite().equals(HOY.plusDays(14)));
        verificar("Sin retraso no hay multa", pL.calcularMulta(HOY.plusDays(15)) == 0);
        verificar("5 días tarde, libro a estudiante: $5.000", pL.calcularMulta(HOY.plusDays(20)) == 5000);
        verificar("5 días tarde, revista a docente: $1.250", pR.calcularMulta(HOY.plusDays(19)) == 1250);

        seccion("5. Clase Biblioteca (registro, reglas y composición)");
        Biblioteca c = nuevaBiblioteca();
        verificar("Registra 5 materiales y 2 usuarios", c.cantidadMateriales() == 5 && c.cantidadUsuarios() == 2);
        verificar("Buscar un código inexistente devuelve null", c.buscarMaterial("ZZ") == null);
        verificarError("No acepta dos materiales con el mismo código",
                () -> c.agregarMaterial(new Dvd("L1", "Repetido", "X", 2000, 60)));
        Map<String, Integer> inv = c.inventarioPorTipo();
        verificar("Inventario por tipo: 3 libros, 1 revista, 1 DVD",
                inv.get("Libro") == 3 && inv.get("Revista") == 1 && inv.get("DVD") == 1);
        c.prestar("L1", "100", HOY);
        c.prestar("L2", "100", HOY);
        c.prestar("R1", "100", HOY);
        verificarError("Estudiante no puede tener un 4.º préstamo", () -> c.prestar("D1", "100", HOY));
        c.agregarMaterial(new Libro("L4", "Libro D", "Autor D", 2003, 120));
        c.prestar("D1", "200", HOY);
        c.prestar("L3", "200", HOY);
        c.prestar("L4", "200", HOY);
        c.agregarMaterial(new Libro("L5", "Libro E", "Autor E", 2004, 140));
        c.prestar("L5", "200", HOY);
        verificar("Docente sí puede tener 4 préstamos (su límite es 5)",
                c.buscarUsuario("200").getPrestamosActivos().size() == 4 && c.buscarUsuario("200").puedePrestar());
        verificarError("No se presta a un usuario inexistente", () -> c.prestar("L1", "999", HOY));
        verificar("DVD devuelto 2 días tarde por docente: 2 × 2000 × 50 % = $2.000",
                c.devolver("D1", HOY.plusDays(3 + 7 + 2)) == 2000);
        verificar("El historial guarda los 7 préstamos", c.getHistorial().size() == 7);
        verificarError("No se devuelve algo que no está prestado", () -> c.devolver("D1", HOY));

        seccion("6. Escalabilidad: tipo nuevo sin modificar Biblioteca");
        Biblioteca d = nuevaBiblioteca();
        d.agregarMaterial(new AudioLibro("A1", "Audiolibro de prueba", 2024));
        Prestamo pa = d.prestar("A1", "100", HOY);
        verificar("Audiolibro se presta con SU plazo (10 días)", pa.getFechaLimite().equals(HOY.plusDays(10)));
        verificar("Audiolibro cobra SU multa (3 días × $800 = $2.400)", d.devolver("A1", HOY.plusDays(13)) == 2400);

        System.out.println("\nResultado: " + aprobadas + " pruebas aprobadas, " + fallidas + " fallidas.");
        if (fallidas > 0) System.exit(1);
    }
}
