package biblioteca;

import java.time.LocalDate;

/** Demostración: un día de trabajo en la biblioteca con distintos materiales y usuarios. */
public class Main {
    public static void main(String[] args) {
        Biblioteca biblio = new Biblioteca("Biblioteca Comunitaria Montería");

        // 1. Objetos de distintas subclases guardados como el tipo general
        biblio.agregarMaterial(new Libro("L-01", "Cien años de soledad", "Gabriel García Márquez", 1967, 471));
        biblio.agregarMaterial(new Libro("L-02", "La vorágine", "José Eustasio Rivera", 1924, 320));
        biblio.agregarMaterial(new Revista("R-01", "Ingeniería al Día", 2026, 45));
        biblio.agregarMaterial(new Dvd("D-01", "Los viajes del viento", "Ciro Guerra", 2009, 117));
        biblio.registrarUsuario(new Estudiante("1001", "Ana Gómez", "Ingeniería de Sistemas"));
        biblio.registrarUsuario(new Docente("2001", "Carlos Ruiz", "Ingeniería"));

        System.out.println("=== " + biblio.getNombre() + " ===");
        System.out.println("Inventario por tipo: " + biblio.inventarioPorTipo());
        System.out.println("\nCatálogo (polimorfismo: misma llamada, distinto resultado):");
        for (String linea : biblio.catalogo()) System.out.println("  " + linea);

        // 2. Préstamos: el plazo depende del material y del usuario
        LocalDate hoy = LocalDate.of(2026, 10, 31);
        System.out.println("\nPréstamos del " + hoy + ":");
        System.out.println("  " + biblio.prestar("L-01", "1001", hoy));   // libro 15 días
        System.out.println("  " + biblio.prestar("D-01", "1001", hoy));   // DVD 3 días
        System.out.println("  " + biblio.prestar("R-01", "2001", hoy));   // revista 7 + 7 extra docente

        // 3. Una regla que se cumple: no se presta algo que ya está prestado
        try {
            biblio.prestar("L-01", "2001", hoy);
        } catch (IllegalStateException e) {
            System.out.println("  Rechazado: " + e.getMessage());
        }

        // 4. Devoluciones (dos con 5 días de retraso y una a tiempo): la multa cambia según material y usuario
        System.out.println("\nDevoluciones (dos con 5 días de retraso y una a tiempo):");
        long m1 = biblio.devolver("D-01", hoy.plusDays(3 + 5));
        System.out.println("  DVD de Ana (estudiante):       5 × $2.000 = $" + m1);
        long m2 = biblio.devolver("R-01", hoy.plusDays(14 + 5));
        System.out.println("  Revista de Carlos (docente):   5 × $500 − 50 % = $" + m2);
        long m3 = biblio.devolver("L-01", hoy.plusDays(10));
        System.out.println("  Libro de Ana, a tiempo:        $" + m3);

        System.out.println("\nMateriales disponibles al cerrar: " + biblio.materialesDisponibles().size()
                + " de " + biblio.cantidadMateriales());
    }
}
