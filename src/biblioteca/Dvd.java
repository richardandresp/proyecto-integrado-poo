package biblioteca;

/** Un DVD ES UN material bibliográfico. Se presta 3 días y su multa es la más alta. */
public class Dvd extends MaterialBibliografico {

    public static final int DIAS_PRESTAMO = 3;
    public static final long MULTA_POR_DIA = 2000;

    private final String director;
    private final int duracionMinutos;

    public Dvd(String codigo, String titulo, String director, int anio, int duracionMinutos) {
        super(codigo, titulo, anio);
        this.director = director;
        this.duracionMinutos = duracionMinutos;
    }

    @Override public String getTipo()         { return "DVD"; }
    @Override public int getDiasPrestamo()    { return DIAS_PRESTAMO; }
    @Override public long getMultaPorDia()    { return MULTA_POR_DIA; }
    @Override protected String detalle()      { return "dir. " + director + ", " + duracionMinutos + " min"; }

    public String getDirector()      { return director; }
    public int getDuracionMinutos()  { return duracionMinutos; }
}
