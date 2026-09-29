import org.cris.AdaDos.models.Alumno;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestAlumnos {
    @Test
    public void testCalificacionVacia(){
        Alumno alumno= new Alumno("Cris-chan","Dior","Kirk","Cris");
        assertFalse(alumno.tieneCalificacionValida());
    }

    @Test
    public void testCalificacionValida(){
        Alumno alumno= new Alumno("Cris-chan","Dior","Kirk","Cris");
        alumno.setCalificacion("60");
        assertTrue(alumno.tieneCalificacionValida());
    }
    @Test
    public void testCalificacionInvalida(){
        Alumno alumno= new Alumno("Epstein","Trump","Donal","Cristiano");
        alumno.setCalificacion("-20");
        assertFalse(alumno.tieneCalificacionValida());

        Alumno alumno2= new Alumno("KOKO","Tromp","Donald","Crisencio");
        alumno.setCalificacion("500");
        assertFalse(alumno2.tieneCalificacionValida());

    }
    @Test
    public void testCalificacionNoNumerica(){
        Alumno alumno= new Alumno("Epstein","Trump","Donal","Cristiano");
        alumno.setCalificacion("BURRO");
        assertFalse(alumno.tieneCalificacionValida());
    }
}
