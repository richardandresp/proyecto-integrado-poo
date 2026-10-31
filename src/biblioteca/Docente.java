package biblioteca;

/**
 * Un docente ES UN usuario con más beneficios: hasta 5 préstamos,
 * 7 días extra de plazo y 50 % de descuento en las multas.
 * Sobrescribe (@Override) los métodos que el padre trae por defecto.
 */
public class Docente extends Usuario {

    public static final int MAX_PRESTAMOS = 5;
    public static final int DIAS_EXTRA = 7;
    public static final int PORCENTAJE_DESCUENTO = 50;

    private final String facultad;

    public Docente(String documento, String nombre, String facultad) {
        super(documento, nombre);
        this.facultad = facultad;
    }

    @Override public String getTipo()        { return "Docente"; }
    @Override public int getMaxPrestamos()   { return MAX_PRESTAMOS; }
    @Override public int getDiasExtra()      { return DIAS_EXTRA; }

    @Override
    public long aplicarDescuento(long multa) {
        return multa * (100 - PORCENTAJE_DESCUENTO) / 100;
    }

    public String getFacultad() { return facultad; }
}
