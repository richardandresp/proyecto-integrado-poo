package biblioteca;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Punto de entrada del sistema. Trabaja SIEMPRE con los tipos generales
 * (MaterialBibliografico y Usuario), nunca con Libro, Revista, Dvd, Estudiante
 * o Docente. Por eso se pueden agregar tipos nuevos sin cambiar esta clase.
 */
public class Biblioteca {

    private final String nombre;
    private final List<MaterialBibliografico> materiales = new ArrayList<>();  // agregación
    private final List<Usuario> usuarios = new ArrayList<>();                  // agregación
    private final List<Prestamo> historial = new ArrayList<>();                // composición

    public Biblioteca(String nombre) {
        this.nombre = nombre;
    }

    // ---------- Registro ----------
    public void agregarMaterial(MaterialBibliografico material) {
        if (buscarMaterial(material.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un material con código " + material.getCodigo());
        }
        materiales.add(material);
    }

    public void registrarUsuario(Usuario usuario) {
        if (buscarUsuario(usuario.getDocumento()) != null) {
            throw new IllegalArgumentException("Ya existe un usuario con documento " + usuario.getDocumento());
        }
        usuarios.add(usuario);
    }

    // ---------- Búsquedas ----------
    public MaterialBibliografico buscarMaterial(String codigo) {
        for (MaterialBibliografico m : materiales) {
            if (m.getCodigo().equals(codigo)) return m;
        }
        return null;
    }

    public Usuario buscarUsuario(String documento) {
        for (Usuario u : usuarios) {
            if (u.getDocumento().equals(documento)) return u;
        }
        return null;
    }

    public List<MaterialBibliografico> materialesDisponibles() {
        List<MaterialBibliografico> lista = new ArrayList<>();
        for (MaterialBibliografico m : materiales) {
            if (m.isDisponible()) lista.add(m);
        }
        return lista;
    }

    /** Cuenta los materiales por tipo. Cada objeto responde getTipo() a su manera. */
    public Map<String, Integer> inventarioPorTipo() {
        Map<String, Integer> conteo = new LinkedHashMap<>();
        for (MaterialBibliografico m : materiales) {
            conteo.merge(m.getTipo(), 1, Integer::sum);
        }
        return conteo;
    }

    /** Catálogo: la misma llamada getDescripcion() da un resultado distinto según el objeto. */
    public List<String> catalogo() {
        List<String> lineas = new ArrayList<>();
        for (MaterialBibliografico m : materiales) {
            lineas.add(m.getDescripcion());
        }
        return lineas;
    }

    // ---------- Operaciones principales ----------
    public Prestamo prestar(String codigo, String documento, LocalDate fecha) {
        MaterialBibliografico material = buscarMaterial(codigo);
        Usuario usuario = buscarUsuario(documento);
        if (material == null) throw new IllegalArgumentException("No existe el material " + codigo);
        if (usuario == null)  throw new IllegalArgumentException("No existe el usuario " + documento);
        if (!usuario.puedePrestar()) {
            throw new IllegalStateException(usuario.getNombre() + " ya tiene "
                    + usuario.getMaxPrestamos() + " préstamos activos");
        }
        material.marcarPrestado();
        Prestamo prestamo = new Prestamo(material, usuario, fecha);
        usuario.agregarPrestamo(prestamo);
        historial.add(prestamo);
        return prestamo;
    }

    /** Devuelve un material y retorna la multa a pagar (0 si llegó a tiempo). */
    public long devolver(String codigo, LocalDate fecha) {
        for (Prestamo p : historial) {
            if (p.estaActivo() && p.getMaterial().getCodigo().equals(codigo)) {
                p.registrarDevolucion(fecha);
                p.getMaterial().marcarDevuelto();
                p.getUsuario().quitarPrestamo(p);
                return p.calcularMulta(fecha);
            }
        }
        throw new IllegalStateException("El material " + codigo + " no tiene un préstamo activo");
    }

    public String getNombre()             { return nombre; }
    public int cantidadMateriales()       { return materiales.size(); }
    public int cantidadUsuarios()         { return usuarios.size(); }
    public List<Prestamo> getHistorial()  { return new ArrayList<>(historial); }
}
