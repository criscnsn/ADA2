import org.cris.AdaDos.models.Alumno;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestAlumnos {

    @Test
    public void testCalificacionVacia() {
        Alumno alumno = new Alumno("Cris-chan", "Dior", "Kirk", "Cris");
        assertFalse(alumno.tieneCalificacionValida());
    }

    @Test
    public void testCalificacionValida() {
        Alumno alumno = new Alumno("Cris-chan", "Dior", "Kirk", "Cris");
        alumno.setCalificacion("60");
        assertTrue(alumno.tieneCalificacionValida());
    }

    @Test
    public void testCalificacionInvalida() {
        Alumno alumno = new Alumno("Epstein", "Trump", "Donal", "Cristiano");
        alumno.setCalificacion("-20");
        assertFalse(alumno.tieneCalificacionValida());

        Alumno alumno2 = new Alumno("KOKO", "Tromp", "Donald", "Crisencio");
        alumno2.setCalificacion("500");
        assertFalse(alumno2.tieneCalificacionValida());
    }

    @Test
    public void testCalificacionNoNumerica() {
        Alumno alumno = new Alumno("Epstein", "Trump", "Donal", "Cristiano");
        alumno.setCalificacion("BURRO");
        assertFalse(alumno.tieneCalificacionValida());
    }

    @Test
    public void testCalificacionDecimal() {
        Alumno alumno = new Alumno("2120", "Perez", "Lopez", "Juan");
        alumno.setCalificacion("85.5");
        assertFalse(alumno.tieneCalificacionValida(), "Calificaciones con decimales no deben ser válidas como enteros");
    }

    @Test
    public void testLimitesCalificacion() {
        Alumno alumno = new Alumno("21201111", "Hernandez", "Garcia", "Carlos");

        // Límite inferior válido (0)
        alumno.setCalificacion("0");
        assertTrue(alumno.tieneCalificacionValida(), "0 debe ser calificación válida");

        // Límite superior válido (100)
        alumno.setCalificacion("100");
        assertTrue(alumno.tieneCalificacionValida(), "100 debe ser calificación válida");

        // Justo debajo del límite inferior (-1)
        alumno.setCalificacion("-1");
        assertFalse(alumno.tieneCalificacionValida(), "-1 debe ser inválido");

        // Justo arriba del límite superior (101)
        alumno.setCalificacion("101");
        assertFalse(alumno.tieneCalificacionValida(), "101 debe ser inválido");
    }

    @Test
    public void testGetCalificacionFormateada() {
        Alumno alumno = new Alumno("21202222", "Gomez", "Ruiz", "Maria");

        // Sin calificación establecida
        assertEquals("S/C", alumno.getCalificacionFormateada(), "Inicialmente debe retornar S/C");

        // Con calificación vacía o espacios
        alumno.setCalificacion("");
        assertEquals("S/C", alumno.getCalificacionFormateada());

        alumno.setCalificacion("   ");
        assertEquals("S/C", alumno.getCalificacionFormateada());

        // Con calificación válida
        alumno.setCalificacion("85");
        assertEquals("85", alumno.getCalificacionFormateada());
    }

    @Test
    public void testGettersYPropiedadesJavaFX() {
        Alumno alumno = new Alumno("21203333", "Aguilar", "Mendez", "Sofia");

        assertEquals("21203333", alumno.getMatricula());
        assertEquals("Aguilar", alumno.getPrimerApellido());
        assertEquals("Mendez", alumno.getSegundoApellido());
        assertEquals("Sofia", alumno.getNombres());

        assertNotNull(alumno.matriculaProperty());
        assertNotNull(alumno.primerApellidoProperty());
        assertNotNull(alumno.segundoApellidoProperty());
        assertNotNull(alumno.nombresProperty());
        assertNotNull(alumno.calificacionProperty());

        assertEquals("21203333", alumno.matriculaProperty().get());
    }
}
