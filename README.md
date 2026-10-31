# Proyecto integrado con POO: sistema de biblioteca en Java

Actividad de evaluación #1, Fundamentos de la Programación Orientada a Objetos. Universidad Cooperativa de Colombia.
Autor: Richard Andrés Peña Ríos.

Amplía el sistema de biblioteca de la Actividad práctica #1 con **herencia** y **polimorfismo**:
la biblioteca presta libros, revistas y DVD a estudiantes y docentes, y cada tipo aplica sus propias reglas.

## Reglas del negocio
| Material | Días de préstamo | Multa por día de retraso |
|---|---|---|
| Libro | 15 | $1.000 |
| Revista | 7 | $500 |
| DVD | 3 | $2.000 |

| Usuario | Máximo de préstamos | Días extra | Descuento en multas |
|---|---|---|---|
| Estudiante | 3 | 0 | 0 % |
| Docente | 5 | 7 | 50 % |

## Clases
| Clase | Rol en POO |
|---|---|
| `MaterialBibliografico` (abstracta) | Superclase de `Libro`, `Revista` y `Dvd`; define métodos abstractos que cada una resuelve. |
| `Usuario` (abstracta) | Superclase de `Estudiante` y `Docente`; métodos con comportamiento por defecto que `Docente` sobrescribe. |
| `Prestamo` | Asocia un material con un usuario; calcula fecha límite y multa preguntándoles a ellos (polimorfismo). |
| `Biblioteca` | Agrega materiales y usuarios, compone el historial de préstamos y aplica las reglas. |
| `Main` | Demostración. |
| `PruebasBiblioteca` | 35 pruebas unitarias, incluida una de escalabilidad con un tipo nuevo (`AudioLibro`). |

Diagramas en `docs/`: `diagrama_clases.png` y `diagrama_secuencia.png` (fuentes editables `.mmd` en Mermaid, se abren en https://mermaid.live).

## Cómo ejecutarlo
Requiere Java 17 o superior.
```bash
javac -encoding UTF-8 -d out src/biblioteca/*.java
java -cp out biblioteca.Main               # demostración
java -cp out biblioteca.PruebasBiblioteca  # pruebas
```
En Windows, ejecuta antes `chcp 65001` para ver bien las tildes.

## Resultado de las pruebas
`Resultado: 35 pruebas aprobadas, 0 fallidas.`
