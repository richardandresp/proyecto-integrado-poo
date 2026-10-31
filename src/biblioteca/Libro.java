package biblioteca;

/** Un libro ES UN material bibliográfico (herencia). Se presta 15 días. */
public class Libro extends MaterialBibliografico {

    public static final int DIAS_PRESTAMO = 15;
    public static final long MULTA_POR_DIA = 1000;

    private final String autor;
    private final int paginas;

    public Libro(String codigo, String titulo, String autor, int anio, int paginas) {
        super(codigo, titulo, anio);          // el padre valida y guarda lo común
        this.autor = autor;
        this.paginas = paginas;
    }

    @Override public String getTipo()         { return "Libro"; }
    @Override public int getDiasPrestamo()    { return DIAS_PRESTAMO; }
    @Override public long getMultaPorDia()    { return MULTA_POR_DIA; }
    @Override protected String detalle()      { return "de " + autor + ", " + paginas + " págs."; }

    public String getAutor() { return autor; }
    public int getPaginas()  { return paginas; }
}
