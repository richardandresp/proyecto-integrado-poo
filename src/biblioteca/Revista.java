package biblioteca;

/** Una revista ES UN material bibliográfico. Se presta solo 7 días y su multa es menor. */
public class Revista extends MaterialBibliografico {

    public static final int DIAS_PRESTAMO = 7;
    public static final long MULTA_POR_DIA = 500;

    private final int numeroEdicion;

    public Revista(String codigo, String titulo, int anio, int numeroEdicion) {
        super(codigo, titulo, anio);
        if (numeroEdicion <= 0) {
            throw new IllegalArgumentException("El número de edición debe ser positivo");
        }
        this.numeroEdicion = numeroEdicion;
    }

    @Override public String getTipo()         { return "Revista"; }
    @Override public int getDiasPrestamo()    { return DIAS_PRESTAMO; }
    @Override public long getMultaPorDia()    { return MULTA_POR_DIA; }
    @Override protected String detalle()      { return "edición n.º " + numeroEdicion; }

    public int getNumeroEdicion() { return numeroEdicion; }
}
