package biblioteca;

/** Un estudiante ES UN usuario: máximo 3 préstamos, sin días extra ni descuento. */
public class Estudiante extends Usuario {

    public static final int MAX_PRESTAMOS = 3;

    private final String programa;

    public Estudiante(String documento, String nombre, String programa) {
        super(documento, nombre);
        this.programa = programa;
    }

    @Override public String getTipo()        { return "Estudiante"; }
    @Override public int getMaxPrestamos()   { return MAX_PRESTAMOS; }

    public String getPrograma() { return programa; }
}
